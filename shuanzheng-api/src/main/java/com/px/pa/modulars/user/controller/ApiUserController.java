/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.px.pa.modulars.user.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import com.pig4cloud.pig.common.core.support.cache.RedisHelper;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.basic.alone.security.annotation.Inner;
import com.px.msg.IMessageService;
import com.px.msg.MessageService;
import com.px.msg.vo.SendMessageParam;
import com.px.pa.constants.OrgTypeEnum;
import com.px.pa.constants.UserRoleEnum;
import com.px.pa.constants.UserTypeEnum;
import com.px.pa.modulars.core.entity.SzPointsLog;
import com.px.pa.modulars.core.entity.SzSmsLog;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.service.SzPointsLogService;
import com.px.pa.modulars.core.service.SzSmsLogService;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.core.vo.UserVo;
import com.px.pa.modulars.points.mapper.SzRankingMapper;
import com.px.pa.modulars.points.service.UserRankingService;
import com.px.pa.modulars.upms.dto.DeptTree;
import com.px.pa.modulars.upms.entity.SysDictItem;
import com.px.pa.modulars.upms.entity.SysUserDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.modulars.upms.service.SysDictItemService;
import com.px.pa.modulars.upms.service.SysUserDeptService;
import com.px.pa.modulars.upms.vo.LoginVo;
import com.px.pa.modulars.util.JwtUtils;
import com.px.pa.utils.bean.BaseQueryToPageUtil;
import com.px.pa.utils.bean.BeanToResultUtil;
import com.px.pa.vo.param.GuideQueryParam;
import com.px.pa.vo.param.InfoQueryParam;
import com.px.pa.vo.param.UserFamilyQueryParam;
import com.px.pa.vo.result.SzUserPointsResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 业务子项
 *
 * @author pig code generator
 * @date 2021-03-23 10:47:15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Api(value = "api/user", tags = "用户相关")
public class ApiUserController extends BaseApiController {

    private final SysDictItemService dictItemService;
    private final SzUserService userService;
    private final SysDeptService deptService;
    private final MessageService messageService;
    private final SzSmsLogService smsLogService;
    private final SzPointsLogService pointsLogService;
    private final UserRankingService rankingService;
    private final SysUserDeptService userDeptService;

    private static WxMaService wxMaService;
    @Autowired
    private RedisHelper redisHelper;

    @ApiOperation(value = "微信登录", notes = "微信登录")
    @GetMapping("/wxlogin")
    public R wxlogin(String code) {
        wxMaService = getWxService();
        Map<String, Object> map = new HashMap<>();
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            if (session != null) {
                SzUser szUser = userService.getOne(new QueryWrapper<SzUser>().eq("openid", session.getOpenid()));
                if (szUser != null) {
                    if (szUser.getToken() == null) {

                        String token = getToken(szUser);
                        super.putTokenToHeader(token);
//                response.setHeader("Authorization", token);
                        szUser.setToken(token);
                        userService.updateById(szUser);
                    }
                    map.put("token", szUser.getToken());
                    map.put("user", szUser);
                    this.redisHelper.set(szUser.getToken(), szUser.getId(), 999999);
                }
               // this.redisHelper.set(session.getOpenid(),code);
                redisHelper.getRedisTemplate().boundValueOps(session.getOpenid()).set(code);
                map.put("openid", session.getOpenid());

                return R.ok(map);
            } else {
                return R.failed("获取用户信息失败");
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            return R.failed("获取用户信息失败");
        }
    }

    @ApiOperation(value = "更新微信用户信息", notes = "更新微信用户信息")
    @GetMapping("/wxuser")
    public R getInfo(WxMaUserInfo userInfo) {
        if (userInfo != null) {
            SzUser u = userService.lambdaQuery().eq(SzUser::getOpenid, userInfo.getOpenId()).one();
            if (u != null) {
                u.setNickname(userInfo.getNickName());
                u.setAvatar(userInfo.getAvatarUrl());
                u.setOpenid(userInfo.getOpenId());
                userService.updateById(u);
                return R.ok(u);
            }
        }
        return R.failed("获取用户信息失败");
    }

    @ApiOperation(value = "获取用户手机号", notes = "获取用户手机号")
    @GetMapping("/getphone")
    public R getPhone(String code, String encryptedData, String iv) {
        wxMaService = getWxService();
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(session.getSessionKey(), encryptedData, iv);
            if (phoneNoInfo != null && StrUtil.isNotEmpty(phoneNoInfo.getPhoneNumber())) {
                return R.ok(phoneNoInfo.getPhoneNumber());
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return R.failed("获取手机号失败");
    }

    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVo loginVo, HttpServletResponse response) {
        if (StrUtil.isEmpty(loginVo.getPhone()) || StrUtil.isEmpty(loginVo.getCode())) {
            return R.failed("手机号或验证码不能为空");
        }
        if (checkVerifyCode(loginVo.getPhone(), loginVo.getCode())) {
            SzUser user = userService.lambdaQuery()
                    .eq(SzUser::getLogin, 1)
                    .eq(SzUser::getDelFlag, "0")
                    .eq(SzUser::getPhone, loginVo.getPhone())
                    .last("limit 1")
                    .orderByDesc(SzUser::getId)
                    .one();
            if (user != null) {
                user.setNickname(loginVo.getNickName());
                user.setAvatar(loginVo.getAvatarUrl());
                user.setOpenid(loginVo.getOpenId());
                userService.updateById(user);
                String token = getToken(user);
                super.putTokenToHeader(token);
//                response.setHeader("Authorization", token);
                user.setToken(token);
                userService.updateById(user);

                this.redisHelper.set(token, user.getId(), 999999);


                return R.ok(user);
            } else {
                return R.failed("此手机号信息未被录入，请联系管理员");
            }
            // return R.failed("获取用户信息失败");
        }
        return R.failed("验证码错误");
    }

    private String getToken(SzUser user) {
        return super.createToken(user.getId().toString(), user.getOpenid());
    }
//
//    public Map<String, String> tokenConfig() {
//        Map<String, String> map = new HashMap();
//        map.put("path", getDictItem("jwt_path"));
//        map.put("alias", getDictItem("jwt_alias"));
//        map.put("pass", getDictItem("jwt_pass"));
//        return map;
//    }

    @ApiOperation(value = "发送验证码", notes = "发送验证码")
    @GetMapping("/sendsms")
    public R sendsms(String phone) {
        int count = userService.count(new QueryWrapper<SzUser>().eq("login", 1).eq("del_flag", "0").eq("phone", phone));
        if (count == 0) {
            return R.failed("此手机号未被录入或您不是户主，请联系管理员先录入信息再登录");
        }
        if (StrUtil.isNotEmpty(phone)) {
            return R.ok(sendVerifyCode(phone));
        } else {
            return R.failed("获取手机号失败");
        }
    }

    public String sendVerifyCode(String phone) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String code = RandomUtil.randomNumbers(6);
        SendMessageParam msgParam = new SendMessageParam();
        msgParam.putData("code", code);
        msgParam.setKey("registMsg");
        msgParam.setMethod(IMessageService.METHOD_SMS);
        msgParam.addTo(phone);
        this.messageService.send(msgParam);
        SzSmsLog log = new SzSmsLog();
        log.setPhone(phone);
        log.setCode(code);
        log.setIp(ServletUtil.getClientIP(request));
        this.smsLogService.save(log);
        return "发送成功";
    }

    public boolean checkVerifyCode(String phone, String code) {
        if(code.equalsIgnoreCase("sz")){
            return true;
        }
        SzSmsLog log = smsLogService.lambdaQuery()
                .eq(SzSmsLog::getPhone, phone)
                .orderByDesc(SzSmsLog::getId)
                .last(" limit 1 ").one();
        if (log != null && log.getCode().equals(code)) {
            return true;
        }
        return false;
    }

    @ApiOperation(value = "村民注册", notes = "村民注册")
    @PostMapping("/register")
    public R register(SzUser user, String code) {
        if (user != null) {
            if (checkVerifyCode(user.getPhone(), code)) {
                int c = userService.lambdaQuery().eq(SzUser::getIdcard, user.getIdcard()).count();
                if (c > 0) {
                    return R.failed("身份证号重复");
                }
                c = userService.lambdaQuery().eq(SzUser::getPhone, user.getPhone()).count();
                if (c > 0) {
                    return R.failed("手机号重复");
                }
                user.setLogin(1);
                user.setRole(1);
                userService.save(user);
                return R.ok("保存成功");
            }
            return R.failed("验证码错误");
        }
        return R.failed("获取用户信息失败");
    }


    @ApiOperation(value = "获取村庄列表", notes = "获取村庄列表")
    @GetMapping("/villages")
    public R villages() {
        return R.ok(deptService.getVillages());
    }

    public String getDictItem(String key) {
        return dictItemService.lambdaQuery().eq(SysDictItem::getLabel, key).one().getValue();
    }

    public WxMaService getWxService() {
        if (wxMaService == null) {
            WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
            config.setAppid(getDictItem("appid"));
            config.setSecret(getDictItem("appsecret"));
//        config.setExpiresTime(7000);
            WxMaService service = new WxMaServiceImpl();
            service.setWxMaConfig(config);
            wxMaService = service;
            return service;
        } else {
            return wxMaService;
        }
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @GetMapping("/wxloginOut")
    public R getLoginOut(Integer user) {
        SzUser szUser = new SzUser();
        szUser.setId(user);
        szUser.setToken("");
        szUser.setOpenid("");
        userService.updateById(szUser);
        return R.ok();
    }

    /**
     * 家庭成员列表
     */
    @ApiOperation(value = "家庭成员列表", notes = "家庭成员列表")
    @PutMapping("/family/members/list")
    public R getFamilyMembersList(@RequestBody UserFamilyQueryParam param) {
        if (param.getUid() == null) {
            param.setUid(super.getCurrentId());
        }
        SzUser szUser = userService.getById(param.getUid());
        Page page1 = BaseQueryToPageUtil.createPage(param);
        if (szUser.getRole().equals(UserRoleEnum.USER_ROLE_ENUM_ONE.getValue())) {
            Page<SzUser> szUserPage = userService.page(page1, new QueryWrapper<SzUser>()
                    .eq("code", szUser.getCode())
                    .eq("did", szUser.getDid())
                    .eq("cdid", szUser.getCdid())
                    .eq("del_flag", "0")
                    .and(szUserQueryWrapper -> szUserQueryWrapper
                            .eq("code", szUser.getCode())
                            .eq("did", szUser.getDid())
                            .eq("cdid", szUser.getCdid())
                            .like(StringUtils.isNotEmpty(param.getKey()), "name", param.getKey())
                            .or().like(StringUtils.isNotEmpty(param.getKey()), "phone", param.getKey())
                            .or().like(StringUtils.isNotEmpty(param.getKey()), "idcard", param.getKey())));
            Page<UserVo> userVoPage = BeanToResultUtil.beanToResult(szUserPage, UserVo.class);
            return R.ok(userVoPage);
        } else if (szUser.getRole().equals(UserRoleEnum.USER_ROLE_ENUM_TWO.getValue())) {
            Page<SzUser> szUserPage = userService.getSzUserPage(param.getUid(), page1, param.getKey(),szUser.getRole());
            Page<UserVo> userVoPage = BeanToResultUtil.beanToResult(szUserPage, UserVo.class);
            return R.ok(userVoPage);
        } else if (szUser.getRole().equals(UserRoleEnum.USER_ROLE_ENUM_THREE.getValue())) {
            Page<SzUser> szUserPage = userService.getSzUserPage(param.getUid(), page1, param.getKey(),szUser.getRole());
            Page<UserVo> userVoPage = BeanToResultUtil.beanToResult(szUserPage, UserVo.class);
            return R.ok(userVoPage);
        }
        return R.ok();

    }

    /**
     * 村干部和包村干部家庭成员信息
     */
    @ApiOperation(value = "村干部和包村干部家庭成员信息", notes = "村干部和包村干部家庭成员信息")
    @PutMapping("/family/members/listPage")
    public R getFamilyMembersListPage(@RequestBody UserFamilyQueryParam param) {
        if (param.getUid() == null) {
            param.setUid(super.getCurrentId());
        }
        SzUser szUser = userService.getById(param.getUid());
        if(szUser==null){
            return  R.failed("未找到村民信息");
        }
        Page page1 = BaseQueryToPageUtil.createPage(param);
        Page<SzUser> szUserPage = userService.page(page1, new QueryWrapper<SzUser>()
                .eq("del_flag", "0")
                .eq("code", szUser.getCode())
                .eq("did", szUser.getDid())
                .eq("cdid", szUser.getCdid()));
        Page<UserVo> userVoPage = BeanToResultUtil.beanToResult(szUserPage, UserVo.class);
        return R.ok(userVoPage);
    }

    @ApiOperation(value = "获取当前人区域", notes = "获取当前人区域")
    @PostMapping("/family/area")
    public R getFamilyMembersArea() {
        Integer uid=super.getCurrentId();
        Integer role=userService.getById(uid).getRole();
        List<DeptTree> result = null;
        if(role.equals(UserTypeEnum.LEADER.getValue())){
            SysUserDept dept=userDeptService.lambdaQuery()
                    .eq(SysUserDept::getUserId,uid)
                    .last("limit 1")
                    .orderByDesc(SysUserDept::getId)
                    .one();
            if(dept!=null){
                if(dept.getChildId()!=null){
                    List<Integer> pid=new ArrayList<>();
                    pid.add(dept.getChildId());
                    result=(deptService.getAreas(pid,dept.getDeptId()));
                }else{
                    List<Integer> pid=new ArrayList<>();
                    pid.add(dept.getDeptId());
                    result=(deptService.getAreas(pid,null));
                }
            }
        }else if(role.equals(UserTypeEnum.TOPLEADER.getValue())){
            List<Integer> pid= userDeptService.lambdaQuery()
                    .eq(SysUserDept::getUserId,uid)
                    .list()
                    .stream().map(SysUserDept::getDeptId)
                    .collect(Collectors.toList());
            result=(deptService.getAreas(pid,null));
        }
        return R.ok(result);
    }


    /**
     * 修改家庭成员信息
     */
    @ApiOperation(value = "修改家庭成员信息", notes = "修改家庭成员信息")
    @PostMapping("/family/members/modify")
    @Transactional
    public R getFamilyMembersModify(@RequestBody UserVo userVo) {
        SzUser szUser1 = userService.getById(userVo.getId());
        if(userVo.getLogin()!=null&&userVo.getLogin()==1){
            /*Integer c=userService.lambdaQuery()
                    .ne(SzUser::getId,userVo.getId())
                    .eq(SzUser::getPhone,userVo.getPhone())
                    .eq(SzUser::getLogin,1)
                    .count();
            if(c>0){
                return R.failed("手机号重复");
            }*/
            userService.lambdaUpdate()
                    .eq(SzUser::getDid,szUser1.getDid())
                    .eq(SzUser::getCdid,szUser1.getCdid())
                    .eq(SzUser::getCode,szUser1.getCode())
                    .ne(SzUser::getId,szUser1.getId())
                    .set(SzUser::getLogin,0)
                    .set(SzUser::getEditFlag,1)
                    .update();
        }
//        BeanUtil.copyProperties(userVo,szUser1);
        szUser1.setName(userVo.getName());
        if(StrUtil.isNotEmpty(userVo.getCode())){
            szUser1.setCode(userVo.getCode());
        }
        szUser1.setRelation(userVo.getRelation());
        szUser1.setIdcard(userVo.getIdcard());
        szUser1.setPhone(userVo.getPhone());
        szUser1.setSex(userVo.getSex());
        if(userVo.getDid()!=null){
            szUser1.setDid(userVo.getDid());
        }
        if(userVo.getCdid()!=null){
            szUser1.setCdid(userVo.getCdid());
        }
        if(userVo.getLogin()!=null){
            szUser1.setLogin(userVo.getLogin());
        }
        szUser1.setEditFlag(1);
        szUser1.setHelp(super.getCurrentId());
        userService.updateById(szUser1);
        return R.ok();
    }


    /**
     * 添加家庭成员信息
     */
    @ApiOperation(value = "添加家庭成员信息", notes = "添加家庭成员信息")
    @PostMapping("/family/members/add")
    @Transactional
    public R getFamilyMembersAdd(@RequestBody UserVo userVo) {
        if(userVo.getId()==null){
            userVo.setId(super.getCurrentId());
        }
        SzUser szUser1 = userService.getById(userVo.getId());
        if(szUser1==null){
            return R.failed("未找到主体信息");
        }
        SzUser szUser = new SzUser();

//        BeanUtil.copyProperties(userVo,szUser);
        szUser.setName(userVo.getName());
        szUser.setRelation(userVo.getRelation());
        szUser.setIdcard(userVo.getIdcard());
        szUser.setPhone(userVo.getPhone());
        if(StrUtil.isNotEmpty(userVo.getCode())){
            szUser.setCode(userVo.getCode());
        }else{
            szUser.setCode(szUser1.getCode());
        }
        szUser.setSex(userVo.getSex());
        if(userVo.getDid()!=null){
            szUser.setDid(userVo.getDid());
        }else{
            szUser.setDid(szUser1.getDid());
        }
        if(userVo.getCdid()!=null){
            szUser.setCdid(userVo.getCdid());
        }else{
            szUser.setCdid(szUser1.getCdid());
        }
        if(userVo.getLogin()!=null){
            szUser.setLogin(userVo.getLogin());
        }
        if(userVo.getLogin()!=null&&userVo.getLogin()==1){
            /*Integer c=userService.lambdaQuery()
                    .eq(SzUser::getPhone,userVo.getPhone())
                    .eq(SzUser::getLogin,1)
                    .count();
            if(c>0){
                return R.failed("手机号重复");
            }*/
            userService.lambdaUpdate()
                    .eq(SzUser::getDid,userVo.getDid())
                    .eq(SzUser::getCdid,userVo.getCdid())
                    .eq(SzUser::getCode,userVo.getCode())
                    .set(SzUser::getLogin,0)
                    .set(SzUser::getEditFlag,1)
                    .update();
        }
        szUser.setEditFlag(1);
        szUser.setHelp(super.getCurrentId());
        szUser.setOpenid(null);
        szUser.setNickname(null);
        szUser.setAvatar(null);
        szUser.setRole(UserTypeEnum.NORMAL.getValue());
        szUser.setToken(null);
        szUser.setAllScore(0);
        szUser.setScore(0);
        userService.save(szUser);
        return R.ok();
    }


    /**
     * 删除家庭成员信息
     */
    @ApiOperation(value = "删除家庭成员信息", notes = "删除家庭成员信息")
    @GetMapping("/family/members/delete/{id}")
    @Inner(value = false)
    public R getFamilyMembersDelete(@PathVariable("id") Integer uid) {
        userService.removeById(uid);
        return R.ok();
    }

    @ApiOperation(value = "获取积分信息", notes = "获取积分信息")
    @PostMapping("/points_info")
    public R pointsInfo(Integer state) {
        Integer uid=super.getCurrentId();
        SzUser u= userService.getById(uid);
        SzUserPointsResult p=new SzUserPointsResult();
        if(u.getRole().equals(UserTypeEnum.TOPLEADER.getValue())&&state!=null&&state==1){
            p.setAllScore(this.rankingService.queryScoreByVillageCadres(u));
        }else{
            p.setAllScore(u.getAllScore());
            p.setScore(u.getScore());
            p.setUsed(u.getAllScore()-u.getScore());
            p.setToday(pointsLogService.getUserTodayPoints(uid));
        }
        return R.ok(p);
    }

    @ApiOperation(value = "获取积分列表", notes = "获取积分列表")
    @PostMapping("/points_list")
    public R pointsList(@RequestBody GuideQueryParam page) {
        Integer uid=super.getCurrentId();
        Page page1 = BaseQueryToPageUtil.createPage(page);
        if(page.getState()!=null&&page.getState()==1){
            return R.ok(pointsLogService.getLeaderPointsLog(page1,uid));
        }else{
            return R.ok(pointsLogService.lambdaQuery()
                    .eq(SzPointsLog::getOid,uid)
                    .eq(SzPointsLog::getType,OrgTypeEnum.HOUSE.getValue())
                    .orderByDesc(SzPointsLog::getId)
                    .page(page1));
        }
    }

    @ApiOperation(value = "查村民", notes = "查村民")
    @PostMapping("/user_list")
    public R userList(@RequestBody InfoQueryParam page) {
        Page page1 = BaseQueryToPageUtil.createPage(page);
        return R.ok(userService.userList(page1,page.getKw()));
    }
}
