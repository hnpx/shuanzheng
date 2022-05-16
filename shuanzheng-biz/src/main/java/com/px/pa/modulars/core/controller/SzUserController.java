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

package com.px.pa.modulars.core.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.pa.modulars.core.entity.SzShopGoods;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.entity.SysUserDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.modulars.upms.service.SysUserDeptService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 居民信息
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szuser" )
@Api(value = "szuser", tags = "居民信息管理")
public class SzUserController {

    private final  SzUserService szUserService;
    private final SysDeptService deptService;
    private final SysUserDeptService userDeptService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szUser 居民信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szuser_get')" )
    public R getSzUserPage(Page page, SzUser szUser) {
        return R.ok(szUserService.lambdaQuery()
                .eq(SzUser::getLogin,1)
                .eq(SzUser::getDelFlag,"0")
                .eq(szUser.getDid()!=null, SzUser::getDid,szUser.getDid())
                .eq(szUser.getCdid()!=null, SzUser::getCdid,szUser.getCdid())
                .eq(szUser.getRole()!=null, SzUser::getRole,szUser.getRole())
                .like(StrUtil.isNotEmpty(szUser.getName()),SzUser::getName,szUser.getName())
                .like(StrUtil.isNotEmpty(szUser.getCode()),SzUser::getCode,szUser.getCode())
                .like(StrUtil.isNotEmpty(szUser.getIdcard()),SzUser::getIdcard,szUser.getIdcard())
                .like(StrUtil.isNotEmpty(szUser.getPhone()),SzUser::getPhone,szUser.getPhone())
                .orderByDesc(SzUser::getCreateTime)
                .page(page));
    }

    /**
     * 分页查询
     * @param page 分页对象
     * @param szUser 居民信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page1" )
    @PreAuthorize("@pms.hasPermission('core_szuser_get')" )
    public R getSzUserPage1(Page page, SzUser szUser) {
        return R.ok(szUserService.lambdaQuery()
                .eq(SzUser::getDelFlag,"0")
                .eq(szUser.getDid()!=null, SzUser::getDid,szUser.getDid())
                .eq(szUser.getCdid()!=null, SzUser::getCdid,szUser.getCdid())
                .eq(szUser.getRole()!=null, SzUser::getRole,szUser.getRole())
                .like(StrUtil.isNotEmpty(szUser.getName()),SzUser::getName,szUser.getName())
                .like(StrUtil.isNotEmpty(szUser.getCode()),SzUser::getCode,szUser.getCode())
                .like(StrUtil.isNotEmpty(szUser.getIdcard()),SzUser::getIdcard,szUser.getIdcard())
                .like(StrUtil.isNotEmpty(szUser.getPhone()),SzUser::getPhone,szUser.getPhone())
                .orderByAsc(SzUser::getDid)
                .orderByAsc(SzUser::getCode)
                .orderByDesc(SzUser::getCreateTime)
                .page(page));
    }
    @ApiOperation(value = "获取村", notes = "获取村")
    @GetMapping("/villagetree" )
    @PreAuthorize("@pms.hasPermission('core_szuser_get')" )
    public R getVillageTree() {
        return R.ok(deptService.getVillagesTree());
    }

    @ApiOperation(value = "获取村", notes = "获取村")
    @GetMapping("/village" )
    @PreAuthorize("@pms.hasPermission('core_szuser_get')" )
    public R getVillage() {
        return R.ok(deptService.getVillages());
    }

    @ApiOperation(value = "获取家庭成员", notes = "获取家庭成员")
    @GetMapping("/home" )
    @PreAuthorize("@pms.hasPermission('core_szuser_get')" )
    public R getHome(String code,Integer did,Integer cdid) {
        return R.ok(szUserService.lambdaQuery()
                .eq(SzUser::getCode,code)
                .eq(SzUser::getDid,did)
                .eq(SzUser::getDelFlag,"0")
                .orderByAsc(SzUser::getCreateTime)
                .list());
    }

    @ApiOperation(value = "获取家庭成员", notes = "获取家庭成员")
    @GetMapping("/user_dept" )
    public R userDept(Integer uid) {
        return R.ok(userDeptService.lambdaQuery().eq(SysUserDept::getUserId,uid).list());
    }

    @ApiOperation(value = "办事指南选人审核", notes = "办事指南选人审核")
    @GetMapping("/village_user" )
    public R villageUser(Integer vid) {
        return R.ok(szUserService.villageUser(vid));
    }

    /**
     * 通过id查询居民信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szuser_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szUserService.getById(id));
    }

    /**
     * 新增居民信息
     * @param szUser 居民信息
     * @return R
     */
    @ApiOperation(value = "新增居民信息", notes = "新增居民信息")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szuser_add')" )
    public R save(@RequestBody SzUser szUser) {
        if(StrUtil.isNotEmpty(szUser.getPhone())){
            int c=szUserService.lambdaQuery().eq(SzUser::getPhone,szUser.getPhone()).count();
            if(c>0){
                return R.failed("手机号重复");
            }
        }
        if(StrUtil.isNotEmpty(szUser.getIdcard())){
            int c=szUserService.lambdaQuery().eq(SzUser::getIdcard,szUser.getIdcard()).count();
            if(c>0){
                return R.failed("身份证号重复");
            }
        }
        if(szUser.getRelation().equals("户主")){
            szUser.setLogin(1);
        }
        int c=szUserService.lambdaQuery()
                .eq(SzUser::getDid,szUser.getDid())
                .eq(SzUser::getCdid,szUser.getCdid())
                .eq(SzUser::getCode,szUser.getCode())
                .count();
        if(c==0){
            szUser.setRelation("户主");
            szUser.setLogin(1);
        }
        return R.ok(szUserService.save(szUser));
    }

    @ApiOperation(value = "保存管辖区域", notes = "保存管辖区域")
    @PostMapping("/save_dept")
    public R save_dept(Integer uid,String did,Integer pid) {
        return R.ok(userDeptService.saveDept(uid,did,pid));
    }

    /**
     * 修改居民信息
     * @param szUser 居民信息
     * @return R
     */
    @ApiOperation(value = "修改居民信息", notes = "修改居民信息")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szuser_edit')" )
    public R updateById(@RequestBody SzUser szUser) {
        if(StrUtil.isNotEmpty(szUser.getPhone())){
            int c=szUserService.lambdaQuery()
                    .eq(SzUser::getPhone,szUser.getPhone())
                    .ne(SzUser::getId,szUser.getId()).count();
            if(c>0){
                return R.failed("手机号重复");
            }
        }
        if(StrUtil.isNotEmpty(szUser.getIdcard())){
            int c=szUserService.lambdaQuery()
                    .eq(SzUser::getIdcard,szUser.getIdcard())
                    .ne(SzUser::getId,szUser.getId()).count();
            if(c>0){
                return R.failed("身份证号重复");
            }
        }
        int c=szUserService.lambdaQuery()
                .eq(SzUser::getDid,szUser.getDid())
                .eq(SzUser::getCdid,szUser.getCdid())
                .eq(SzUser::getCode,szUser.getCode())
                .ne(SzUser::getId,szUser.getId())
                .count();
        if(c==0){
            szUser.setRelation("户主");
            szUser.setLogin(1);
        }
        if(!szUser.getRelation().equals("户主")){
            c=szUserService.lambdaQuery()
                    .eq(SzUser::getDid,szUser.getDid())
                    .eq(SzUser::getCdid,szUser.getCdid())
                    .eq(SzUser::getCode,szUser.getCode())
                    .eq(SzUser::getRelation,"户主")
                    .eq(SzUser::getLogin,1)
                    .ne(SzUser::getId,szUser.getId())
                    .count();
            if(c==0){
                szUser.setRelation("户主");
                szUser.setLogin(1);
            }else{
                szUser.setLogin(0);
            }
        }
        return R.ok(szUserService.updateById(szUser));
    }

    /**
     * 通过id删除居民信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除居民信息", notes = "通过id删除居民信息")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szuser_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szUserService.removeById(id));
    }

}
