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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.constants.OrgTypeEnum;
import com.px.pa.constants.UserTypeEnum;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.mapper.SzUserMapper;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.modulars.vo.result.GuideUserResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.px.pa.modulars.upms.entity.SysUserDept;
import com.px.pa.modulars.upms.service.SysUserDeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 居民信息
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Service
@Transactional
public class SzUserServiceImpl extends ServiceImpl<SzUserMapper, SzUser> implements SzUserService {

    @Autowired
    private SysDeptService deptService;
    @Resource
    private SzUserService szUserService;
    @Resource
    private SysUserDeptService sysUserDeptService;

    @Override
    public Page<SzUser> getSzUserPage(Integer uid, Page page, String key,Integer role) {
        SzUser szUser = szUserService.getById(uid);
        List<SysUserDept> sysUserDeptList = sysUserDeptService.list(new QueryWrapper<SysUserDept>().eq("user_id", szUser.getId()));
        List<String> deptIds = new ArrayList<>();
        List<String> cdeptIds = new ArrayList<>();
        sysUserDeptList.forEach(sysUserDept -> {
            if(sysUserDept.getChildId()==null){
                deptIds.add(sysUserDept.getDeptId().toString());
            }else{
                cdeptIds.add(sysUserDept.getDeptId().toString());
            }
        });
        return szUserService.page(page, new QueryWrapper<SzUser>()
                .in(cdeptIds.size()>0,"cdid",cdeptIds)
                .in(deptIds.size()>0,"did", deptIds)
                .eq("login",1)
                .eq("del_flag","0")
                .and(StringUtils.isNotEmpty(key),szUserQueryWrapper -> szUserQueryWrapper
                        .like(StringUtils.isNotEmpty(key), "name", key)
                        .or().like(StringUtils.isNotEmpty(key), "phone", key)
                        .or().like(StringUtils.isNotEmpty(key), "idcard", key)));
    }

    @Override
    public List<SzUser> villageUser(Integer vid) {
        List<Integer> ids = deptService.lambdaQuery()
                .eq(SysDept::getParentId, vid).list()
                .stream().map(SysDept::getDeptId)
                .collect(Collectors.toList());
        QueryWrapper<SzUser> query = new QueryWrapper<SzUser>();
        query.eq("b.role", UserTypeEnum.LEADER.getValue());
        query.eq("b.del_flag","0");
        query.and(qw -> qw.eq("a.dept_id", 1)
                .or().eq("a.dept_id", vid)
                .or().in(ids.size() > 0, "a.dept_id", ids));
        return super.baseMapper.villageUser(query);
    }

    @Override
    public Page<GuideUserResult> userList(Page page, String key) {
        QueryWrapper<SzUser> query = new QueryWrapper<>();
        query.like("a.name",key);
        query.eq("a.login",1);
        query.eq("a.del_flag","0");
        return super.baseMapper.userList(page,query);
    }
}
