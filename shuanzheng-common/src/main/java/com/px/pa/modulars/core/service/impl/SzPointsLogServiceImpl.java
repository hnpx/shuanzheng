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
package com.px.pa.modulars.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.basic.alone.security.service.PigUser;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.pa.constants.OrgTypeEnum;
import com.px.pa.constants.UserTypeEnum;
import com.px.pa.modulars.core.entity.SzCompany;
import com.px.pa.modulars.core.entity.SzPointsLog;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.mapper.SzPointsLogMapper;
import com.px.pa.modulars.core.service.SzCompanyService;
import com.px.pa.modulars.core.service.SzPointsLogService;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.entity.SysUserDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.modulars.upms.service.SysUserDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Service
@Transactional
public class SzPointsLogServiceImpl extends ServiceImpl<SzPointsLogMapper, SzPointsLog> implements SzPointsLogService {

    @Autowired
    private SzUserService userService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SysUserDeptService userDeptService;
    @Autowired
    private SzCompanyService companyService;

    @Override
    public boolean saveLog(SzPointsLog log, Integer sysUser) {
        log.setUid(sysUser);
        if (StrUtil.isNotEmpty(log.getIds())) {
            //TODO 批量添加？？
            if (log.getType().equals(OrgTypeEnum.HOUSE.getValue())) {
                List<SzUser> users = userService.lambdaQuery().in(SzUser::getId, log.getIds().split(",")).list();
                users.forEach(u -> copyLog(log, u.getScore(), u.getId()));
            } else if (log.getType().equals(OrgTypeEnum.VILLAGE.getValue())) {
                List<SysDept> users = deptService.lambdaQuery().in(SysDept::getDeptId, log.getIds().split(",")).list();
                users.forEach(u -> copyLog(log, u.getScore(), u.getDeptId()));
            } else if (log.getType().equals(OrgTypeEnum.COMPANY.getValue())) {
                List<SzCompany> users = companyService.lambdaQuery().in(SzCompany::getId, log.getIds().split(",")).list();
                users.forEach(u -> copyLog(log, u.getScore(), u.getId()));
            }
        } else {
            saveSingle(log);
        }
        return false;
    }

    @Override
    public Integer getUserTodayPoints(Integer uid) {
        QueryWrapper query=new QueryWrapper();
        query.eq("oid", uid);
        query.eq("type", OrgTypeEnum.HOUSE.getValue());
        query.eq("operator", 1);
        query.apply("date_format(create_time,'%Y-%m-%d') = {0}", DateUtil.today());
        return super.baseMapper.getUserTodayPoints(query);
    }

    @Override
    public Page<SzPointsLog> getLeaderPointsLog(Page page,Integer uid) {
        QueryWrapper query=new QueryWrapper();
        query.eq("a.type", 1);
        query.eq("b.login", 1);
        query.eq("b.del_flag", 0);
        Integer role=userService.getById(uid).getRole();
        List<SysUserDept> list=userDeptService.lambdaQuery().eq(SysUserDept::getUserId,uid).list();
        if(role.equals(UserTypeEnum.LEADER.getValue())){
            if(list.size()==1){
                SysUserDept dept=list.get(0);
                if(dept.getChildId()!=null){
                    query.eq("b.cdid", dept.getDeptId());
                }else{
                    query.eq("b.did", dept.getDeptId());
                }
            }else{
                return null;
            }
        }else if(role.equals(UserTypeEnum.TOPLEADER.getValue())){
            query.in("b.did",list.stream().map(SysUserDept::getDeptId).collect(Collectors.toList()));
        }
        return super.baseMapper.getLeaderPointsLog(page,uid);
    }

    @Override
    public boolean saveLog(SzPointsLog log) {
        PigUser p=SecurityUtils.getUser();
        Integer uid=1;
        if(p!=null){
            uid=p.getId();
        }
        return this.saveLog(log, uid);
    }

    public void copyLog(SzPointsLog log, Integer points, Integer id) {
        SzPointsLog l = new SzPointsLog();
        BeanUtil.copyProperties(log, l, true);
        l.setPev(points);
        if (l.getOperator() == 1) {
            l.setNext(points + l.getPoints());
        } else {
            l.setNext(points - l.getPoints());
        }
        l.setOid(id);
        saveSingle(l);
    }

    public void saveSingle(SzPointsLog log) {
        if (log.getOid() != null && super.save(log)) {
            String op = log.getOperator() == 1 ? "+" : "-";
            String sql = "all_score=all_score" + op + log.getPoints() + ",score=score" + op + log.getPoints();
            if (log.getType().equals(OrgTypeEnum.HOUSE.getValue())) {
                userService.lambdaUpdate()
                        .eq(SzUser::getId, log.getOid())
                        .setSql(sql)
                        .update();
            } else if (log.getType().equals(OrgTypeEnum.VILLAGE.getValue())) {
                deptService.lambdaUpdate()
                        .eq(SysDept::getDeptId, log.getOid())
                        .setSql(sql)
                        .update();
            } else if (log.getType().equals(OrgTypeEnum.COMPANY.getValue())) {
                companyService.lambdaUpdate()
                        .eq(SzCompany::getId, log.getOid())
                        .setSql(sql)
                        .update();
            }
        }
    }
}
