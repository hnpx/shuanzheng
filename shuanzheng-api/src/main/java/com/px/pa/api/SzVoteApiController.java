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

package com.px.pa.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.support.cache.RedisHelper;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.security.annotation.Inner;
import com.px.pa.constants.DelFlagEnum;
import com.px.pa.constants.TimeStatusEnum;
import com.px.pa.constants.VoteStatusEnum;
import com.px.pa.modulars.core.Enum.ObjectTypeEnum;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.entity.SzVote;
import com.px.pa.modulars.core.entity.SzVoteItem;
import com.px.pa.modulars.core.entity.SzVoteLog;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.core.service.SzVoteItemService;
import com.px.pa.modulars.core.service.SzVoteLogService;
import com.px.pa.modulars.core.service.SzVoteService;
import com.px.pa.modulars.core.vo.ImagesVo;
import com.px.pa.modulars.core.vo.ObjectVo;
import com.px.pa.modulars.core.vo.VoteVo;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


/**
 * 投票信息
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/szvote")
@Api(value = "szvote", tags = "投票信息管理")
public class SzVoteApiController {

    @Resource
    private final SzVoteService szVoteService;
    @Resource
    private final SzVoteLogService szVoteLogService;
    @Resource
    private final SzVoteItemService szVoteItemService;
    @Resource
    private final SzUserService szUserService;
    @Resource
    private final SysDeptService sysDeptService;
    @Resource
    private RedisHelper redisHelper;

    private static Map<String, String> infoCheck = new HashMap<>();

    /**
     * 投票列表
     * type(1.村民2.村庄)
     *
     * @return
     */
    @ApiOperation(value = "投票列表", notes = "投票列表")
    @GetMapping("/page/list")
    @Inner(value = false)
    public R getSzVotePage(@RequestParam(required = false, defaultValue = "1") Integer page,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) Integer type) {
        Page page1 = new Page(page, pageSize);
        Page<SzVote> szVotePage = szVoteService.getPageList(page1, name, type);
        szVotePage.getRecords().forEach(szVote -> {
            //判断时间状态
            Date startDate = Date.from(szVote.getStime().toInstant(ZoneOffset.of("+8")));
            Date endDate = Date.from(szVote.getEtime().toInstant(ZoneOffset.of("+8")));
            if (startDate.compareTo(new Date()) == 1) {
                szVote.setTimeStatus(TimeStatusEnum.TIME_STATUS_ENUM_ONE.getValue());
            } else if (endDate.compareTo(new Date()) == -1) {
                szVote.setTimeStatus(TimeStatusEnum.TIME_STATUS_ENUM_THREE.getValue());
            } else {
                szVote.setTimeStatus(TimeStatusEnum.TIME_STATUS_ENUM_TWO.getValue());
            }
            //投票数
            Integer voteNum = szVoteItemService.getVoteNum(szVote.getId());
            if (voteNum == null) {
                szVote.setVoteNum(0);
            } else {
                szVote.setVoteNum(voteNum);
            }
            //参与人数
            int participateNum = szVoteItemService.count(new QueryWrapper<SzVoteItem>().eq("vid", szVote.getId()));

            szVote.setParticipateNum(participateNum);
        });
        return R.ok(szVotePage);
    }

    /**
     * 投票详情
     * type (1.按编号2.按票数排序)
     *
     * @return
     */
    @ApiOperation(value = "投票详情", notes = "投票详情")
    @GetMapping("/page/details")
    @Inner(value = false)
    public R getDetails(@RequestParam("vid") Integer vid, @RequestParam(required = false) String key,
                        @RequestParam(required = false, defaultValue = "1") Integer page,
                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                        @RequestParam("openid") String openid,
                        @RequestParam(required = false) Integer type,
                        @RequestParam(required = false, defaultValue = "2") Integer state) {
        Page page1 = new Page(page, pageSize);
        SzVote szVote = szVoteService.getById(vid);
        //更新访问量
        szVoteService.run1(vid, state);
        //判断时间状态
        Date startDate1 = Date.from(szVote.getStime().toInstant(ZoneOffset.of("+8")));
        Date endDate1 = Date.from(szVote.getEtime().toInstant(ZoneOffset.of("+8")));
        if (startDate1.compareTo(new Date()) == 1) {
            szVote.setTimeStatus(TimeStatusEnum.TIME_STATUS_ENUM_ONE.getValue());
        } else if (endDate1.compareTo(new Date()) == -1) {
            szVote.setTimeStatus(TimeStatusEnum.TIME_STATUS_ENUM_THREE.getValue());
        } else {
            szVote.setTimeStatus(TimeStatusEnum.TIME_STATUS_ENUM_TWO.getValue());
        }
        // int voteNum = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", szVote.getId()));
        Integer voteNum = szVoteItemService.getVoteNum(szVote.getId());
        if (voteNum == null) {
            szVote.setVoteNum(0);
        } else {
            szVote.setVoteNum(voteNum);
        }
        int participateNum = szVoteItemService.count(new QueryWrapper<SzVoteItem>().eq("vid", szVote.getId()));
        szVote.setParticipateNum(participateNum);
        Page<SzVoteItem> szVoteItemPage = new Page<SzVoteItem>();
        if (type == 1) { //按编号排序
            szVoteItemPage = szVoteItemService.lambdaQuery().eq(SzVoteItem::getVid, vid)
                    .and(
                            szVoteItemLambdaQueryWrapper -> szVoteItemLambdaQueryWrapper
                                    .eq(SzVoteItem::getVid, vid)
                                    .like(StringUtils.isNotEmpty(key), SzVoteItem::getName, key).or()
                                    .like(StringUtils.isNotEmpty(key), SzVoteItem::getNumber, key).or()
                                    .like(StringUtils.isNotEmpty(key), SzVoteItem::getDname, key))
                    .orderByAsc(type == 1, SzVoteItem::getNumber)
                    .orderByAsc(SzVoteItem::getSort)
                    .orderByDesc(SzVoteItem::getCreateTime).page(page1);
        } else if (type == 2) { //按票数排序
            szVoteItemPage = szVoteItemService.getListPage(page1, vid, key);

        }

        //查询排名
        for (SzVoteItem szVoteItem : szVoteItemPage.getRecords()) {
            if (type == 1) {
                Map<String, Object> map = szVoteItemService.getrankingByNumber(vid, szVoteItem.getId());
                if (map.size() > 0) {
                    Double f = Double.valueOf(map.get("rank").toString());
                    szVoteItem.setRank((int) Math.ceil(f));
                }
            } else if (type == 2) {
                Map<String, Object> map = szVoteItemService.getranking(vid, szVoteItem.getId());
                if (map.size() > 0) {
                    Double f = Double.valueOf(map.get("rank").toString());
                    szVoteItem.setRank((int) Math.ceil(f));
                }
            }

            //查询头像
            SzVote szVote1 = szVoteService.getById(szVoteItem.getVid());
            if (szVote1.getType().equals(ObjectTypeEnum.ObjectTypeEnumONE.getValue())) {
                SzUser szUser = szUserService.getById(szVoteItem.getDid());
                szVoteItem.setAvatar(szUser.getAvatar());
            } else {
                SysDept sysDept = sysDeptService.getById(szVoteItem.getDid());
                if (StringUtils.isNotEmpty(sysDept.getImages())) {
                    List<ImagesVo> imagesVoList = szVoteItemService.getJson(sysDept.getImages());
                    if (imagesVoList.size() > 0) {
                        szVoteItem.setAvatar(imagesVoList.get(0).getKpath());
                    }
                }

            }
        }

        if (StringUtils.isNotEmpty(openid)) {
            //投票限制天数和一次可投几票
            if (szVote.getNum() == 0) {
                //判断用户针对vote是否可以投
                Integer count = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", vid).eq("openid", openid));
                if (count > 0) {
                    szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue());
                } else {
                    szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());
                }
                //查询是否为村民
                SzUser szUser = szUserService.getOne(new QueryWrapper<SzUser>().eq("openid", openid)
                        .eq("del_flag", DelFlagEnum.DEL_FLAG_ENUM_TWO).eq("login", 1));

                if (szVote.getArea() == 1) { // 限制人员：1-仅村民，0-不限
                    if (szUser != null) {
                        szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());
                    } else {
                        szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue());
                    }
                } else {
                    szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());
                }
                for (SzVoteItem szVoteItem : szVoteItemPage.getRecords()) {
                    // int num = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", szVoteItem.getVid()).eq("viid", szVoteItem.getId()));
                    szVoteItem.setVoteNum(szVoteItem.getNum());
                    //查询是否已投票
                   /* Integer count1 = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", vid).eq("openid", openid).eq("viid", szVoteItem.getId()));
                    if (szVote.getState().equals(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue())) {
                        if (count1 > 0) {
                            szVoteItem.setVoteStatus(1);  //已经投过
                        } else {
                            szVoteItem.setVoteStatus(2);   //未投
                        }
                    }*/
                }

                szVote.setSzVoteItemPage(szVoteItemPage);
            } else {
                //todo 投票限制人数和几票
                Integer count = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", vid).eq("openid", openid)
                        .between("create_time", DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date())));
                if (count < szVote.getNum()) {
                    szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());
                } else {
                    szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue());

                }

                //查询是否为村民
                SzUser szUser = szUserService.getOne(new QueryWrapper<SzUser>().eq("openid", openid)
                        .eq("del_flag", DelFlagEnum.DEL_FLAG_ENUM_TWO).eq("login", 1));

                if (szVote.getArea() == 1) { // 限制人员：1-仅村民，0-不限
                    if (szUser != null) {
                        szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());
                    } else {
                        szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue());
                    }
                } else {
                    szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());
                }
                for (SzVoteItem szVoteItem : szVoteItemPage.getRecords()) {
                    //  int num = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", szVoteItem.getVid()).eq("viid", szVoteItem.getId()));
                    szVoteItem.setVoteNum(szVoteItem.getNum());
                    //查询是否已投票
                    /*List<SzVoteLog> szVoteLogList = szVoteLogService.list(new QueryWrapper<SzVoteLog>().eq("vid", vid).eq("openid", openid).eq("viid", szVoteItem.getId()));
                    for (SzVoteLog szVoteLog : szVoteLogList) {
                        if (szVote.getState().equals(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue())) {
                            if (szVoteLog != null) {
                                szVoteItem.setVoteStatus(1);  //已经投过
                            } else {
                                szVoteItem.setVoteStatus(2);   //未投
                            }
                        }
                    }*/
                }
                szVote.setSzVoteItemPage(szVoteItemPage);

            }

        } else {
            szVote.setStatus(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());
            szVoteItemPage.getRecords().forEach(szVoteItem -> {
                szVoteItem.setVoteStatus(2);
            });
            szVote.setSzVoteItemPage(szVoteItemPage);
        }

        //是否授权
        Boolean b = this.redisHelper.exists(openid);
        if(b){
            szVote.setIsAuthor(1); //1.已授权
        }else {
            szVote.setIsAuthor(2); // 2.未授权
        }
        return R.ok(szVote);
    }

    /**
     * 投票选项详情
     *
     * @return
     */
    @ApiOperation(value = "投票选项详情", notes = "投票选项详情")
    @GetMapping("/voteitem/details")
    @Inner(value = false)
    public R getVoteItem(@RequestParam("viid") Integer viid, @RequestParam("vid") Integer vid, @RequestParam("openid") String openid) {


        SzVote szVote = szVoteService.getById(vid);
        SzVoteItem szVoteItem = szVoteItemService.getById(viid);
        szVoteItem.setSzVote(szVote);
        //判断时间状态
        Date startDate1 = Date.from(szVote.getStime().toInstant(ZoneOffset.of("+8")));
        Date endDate1 = Date.from(szVote.getEtime().toInstant(ZoneOffset.of("+8")));
        if (startDate1.compareTo(new Date()) == 1) {
            szVoteItem.setTimeStatus(TimeStatusEnum.TIME_STATUS_ENUM_ONE.getValue());
        } else if (endDate1.compareTo(new Date()) == -1) {
            szVoteItem.setTimeStatus(TimeStatusEnum.TIME_STATUS_ENUM_THREE.getValue());
        } else {
            szVoteItem.setTimeStatus(TimeStatusEnum.TIME_STATUS_ENUM_TWO.getValue());
        }

        //投票数
        int num = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", szVoteItem.getVid()).eq("viid", szVoteItem.getId()));
        szVoteItem.setVoteNum(num);
        //排名
        Map<String, Object> map = szVoteItemService.getranking(vid, viid);
        if (map.size() > 0) {
            Double f = Double.valueOf(map.get("rank").toString());
            szVoteItem.setRank((int) Math.ceil(f));
        }
        if (szVote.getNum() == 0) {
            Integer count = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", vid).eq("openid", openid));
            if (count > 0) {
                szVoteItem.setIsVote(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue());
            } else {
                szVoteItem.setIsVote(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());
            }

            //判断此活动是否限制投票对象
            if (szVote.getArea() == 1) { //限制人员：1-仅村民，0-不限

                SzUser szUser = szUserService.getOne(new QueryWrapper<SzUser>().eq("openid", openid));
                if (szUser == null) {

                    szVoteItem.setIsVote(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue());  //不可以投
                    szVoteItem.setIslimit(1); //不是村民不可以投票
                    return R.ok().setCode(401);
                }
            } else {
                if (StringUtils.isNotEmpty(openid)) {
                    //分享投票状态
                    Integer count1 = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("openid", openid).eq("vid", vid));
                    if (count1 == 0) {
                        szVoteItem.setIsVote(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());  //可以投
                    } else {
                        szVoteItem.setIsVote(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue());  //不可以投
                   /* if (szVoteLog.getViid().equals(viid)) {
                        szVoteItem.setVoteStatus(1);  // 已经投过
                    } else {
                        szVoteItem.setVoteStatus(2);  //未投过
                    }*/
                    }
                }
            }

        } else {
            Integer count = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", vid).eq("openid", openid)
                    .between("create_time", DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date())));
            if (count < szVote.getNum()) {
                szVoteItem.setIsVote(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());
            } else {
                szVoteItem.setIsVote(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue());

            }

            //判断此活动是否限制投票对象
            if (szVote.getArea() == 1) { //限制人员：1-仅村民，0-不限

                SzUser szUser = szUserService.getOne(new QueryWrapper<SzUser>().eq("openid", openid));
                if (szUser == null) {
                    szVoteItem.setIsVote(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue());  //不可以投
                    szVoteItem.setIslimit(1); //不是村民不可以投票
                    return R.ok().setCode(401);
                }


            } else {
                if (StringUtils.isNotEmpty(openid)) {
                    //分享投票状态
                    Integer count1 = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", vid).eq("openid", openid)
                            .between("create_time", DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date())));
                    if (count1 < szVote.getNum()) {
                        szVoteItem.setIsVote(VoteStatusEnum.VOTE_STATUS_ENUM_ONE.getValue());  //可以投
                    } else {
                        szVoteItem.setIsVote(VoteStatusEnum.VOTE_STATUS_ENUM_TWO.getValue());  //不可以投
                   /* if (szVoteLog.getViid().equals(viid)) {
                        szVoteItem.setVoteStatus(1);  // 已经投过
                    } else {
                        szVoteItem.setVoteStatus(2);  //未投过
                    }*/
                    }
                }
            }

        }

        ThreadUtil.execute(()->{
            szVote.setViews(szVote.getViews()+1);
            this.szVoteService.updateById(szVote);
        });

        //是否授权
        Boolean b = this.redisHelper.exists(openid);
        if(b){
            szVoteItem.setIsAuthor(1); //1.已授权
        }else {
            szVoteItem.setIsAuthor(2); // 2.未授权
        }


        return R.ok(szVoteItem);
    }

    private boolean check(String id, String name, String photo) {
        if (infoCheck.get(name) != null && infoCheck.get(name).equals(photo)) {
            return false;
        }
        if(infoCheck.size()>1000){
            infoCheck=new HashMap<>();
        }
        infoCheck.put(id, name + photo);

        return true;
    }

    /**
     * 给他投票
     */
    @ApiOperation(value = "给他投票", notes = "给他投票")
    @PostMapping("/voteitem/vote")
    @Inner(value = false)
    public R getVote(@RequestBody VoteVo voteVo) {

//        if (voteVo.getOpenid() == null || StrUtil.isEmpty(voteVo.getAvatar()) || StrUtil.isEmpty(voteVo.getNickname()) || !this.check(voteVo.getViid().toString(), voteVo.getNickname(), voteVo.getAvatar())) {

        if (voteVo.getOpenid() == null ) {
            return R.failed("您授权失败不能投票").setCode(2);
        } else {
            Boolean b = this.redisHelper.exists(voteVo.getOpenid());
            if (b) {
                Object code = this.redisHelper.get(voteVo.getOpenid());
                if (code == null) {
                    R r = R.failed("您授权信息不对，请重新授权");
                    r.setCode(2);
                    return r;
                }
            } else {
                R r = R.failed("您授权信息不对，请重新授权");
                r.setCode(2);
                return r;
            }
        }

        SzVote szVote = szVoteService.getById(voteVo.getVid());
        //判断时间状态
        Date startDate1 = Date.from(szVote.getStime().toInstant(ZoneOffset.of("+8")));
        Date endDate1 = Date.from(szVote.getEtime().toInstant(ZoneOffset.of("+8")));
        if (startDate1.compareTo(new Date()) == 1) {
            return R.failed("此投票活动还未开始不能投票");
        } else if (endDate1.compareTo(new Date()) == -1) {
            return R.failed("此投票活动已结束不能投票");
        }
        //获取手机的ip地址
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    /*    if (!ServletUtil.getClientIP(request).equals("127.0.0.1") && !ServletUtil.getClientIP(request).equals("0:0:0:0:0:0:0:1")) {
            return R.failed("投票异常").setCode(2);
        }*/

        if (szVote.getArea() == 1) { //限制人员：1-仅村民，0-不限
            SzUser szUser = szUserService.getOne(new QueryWrapper<SzUser>().eq("openid", voteVo.getOpenid()));
            if (szUser == null) {
                return R.failed("此投票活动只有村民才能投票");
            }
        }
        if (szVote.getNum() == 0) {
            Integer count = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", voteVo.getVid())
                    .eq("viid", voteVo.getViid())
                    .eq("openid", voteVo.getOpenid()));
            if (count > 0) {
                return R.failed("您已经为它投过票不能再次投票");
            } else {
                Integer count1 = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", voteVo.getVid())
                        .eq("openid", voteVo.getOpenid()));
                if (count1 > 0) {
                    return R.failed("您已经为此活动投过票不能再次投票");
                } else {
                    szVoteService.run(voteVo);

                }
            }
            return R.ok(0, "您还有0次投票的机会");
        } else {
            Integer count1 = 0;
            synchronized (this) {
                 count1 = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", voteVo.getVid())
                        .eq("openid", voteVo.getOpenid())
                        .between("create_time", DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date())));
            }
            if (count1 >= szVote.getNum()) {
                return R.failed("您已经为此活动投过票不能再次投票");
            } else {
                szVoteService.run(voteVo);
            }
            Integer count2 = szVoteLogService.count(new QueryWrapper<SzVoteLog>().eq("vid", voteVo.getVid())
                    .eq("openid", voteVo.getOpenid())
                    .between("create_time", DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date())));
            Integer num = szVote.getNum() - count2;
            return R.ok(num, "您今天还有" + num + "次投票的机会");

        }


    }

    /**
     * 投票记录接口
     */
    @ApiOperation(value = "投票记录接口", notes = "投票记录接口")
    @GetMapping("/voteitem/vote/record")
    @Inner(value = false)
    public R getRecord(@RequestParam("openid") String openid, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        Page page = new Page(pageNo, pageSize);
        Page<SzVoteLog> szVoteLogPage = szVoteLogService.lambdaQuery().eq(SzVoteLog::getOpenid, openid).orderByDesc(SzVoteLog::getCreateTime).page(page);
        szVoteLogPage.getRecords().forEach(szVoteLog -> {
            SzVote szVote = szVoteService.getById(szVoteLog.getVid());
            if (szVote != null) {
                szVoteLog.setVoteName(szVote.getName());

            }
            SzVoteItem szVoteItem = szVoteItemService.getById(szVoteLog.getViid());
            if (szVoteItem != null) {
                szVoteLog.setVoteItem(szVoteItem.getName());
                szVoteLog.setImage(szVoteItem.getImage());
                szVoteLog.setNumber(szVoteItem.getNumber());
                szVoteLog.setVoteNum(szVoteItem.getNum());
            }

        });

        return R.ok(szVoteLogPage);
    }


}
