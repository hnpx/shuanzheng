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
import com.px.pa.modulars.core.entity.SzFarmCate;
import com.px.pa.modulars.core.service.SzFarmCateService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 汇聚三农分类
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szfarmcate" )
@Api(value = "szfarmcate", tags = "汇聚三农分类管理")
public class SzFarmCateController {

    private final  SzFarmCateService szFarmCateService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szFarmCate 汇聚三农分类
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szfarmcate_get')" )
    public R getSzFarmCatePage(Page page, SzFarmCate szFarmCate) {

        return R.ok(szFarmCateService.lambdaQuery()
                .eq(szFarmCate.getState()!=null,SzFarmCate::getState,szFarmCate.getState())
                .like(StrUtil.isNotEmpty(szFarmCate.getName()),SzFarmCate::getName,szFarmCate.getName())
                .orderByDesc(SzFarmCate::getCreateTime)
                .page(page));
    }

    @ApiOperation(value = "获取列表", notes = "获取列表")
    @GetMapping("/list" )
   // @PreAuthorize("@pms.hasPermission('core_szfarmcate_get')" )
    public R getSzFarmCateList() {
        return R.ok(szFarmCateService.lambdaQuery()
                .eq(SzFarmCate::getState,1)
                .eq(SzFarmCate::getDelFlag,0)
                .orderByDesc(SzFarmCate::getCreateTime)
                .list());
    }


    /**
     * 通过id查询汇聚三农分类
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szfarmcate_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szFarmCateService.getById(id));
    }

    /**
     * 新增汇聚三农分类
     * @param szFarmCate 汇聚三农分类
     * @return R
     */
    @ApiOperation(value = "新增汇聚三农分类", notes = "新增汇聚三农分类")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szfarmcate_add')" )
    public R save(@RequestBody SzFarmCate szFarmCate) {
        return R.ok(szFarmCateService.save(szFarmCate));
    }

    /**
     * 修改汇聚三农分类
     * @param szFarmCate 汇聚三农分类
     * @return R
     */
    @ApiOperation(value = "修改汇聚三农分类", notes = "修改汇聚三农分类")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szfarmcate_edit')" )
    public R updateById(@RequestBody SzFarmCate szFarmCate) {
        szFarmCate.setUpdateTime(LocalDateTime.now());
        return R.ok(szFarmCateService.updateById(szFarmCate));
    }

    /**
     * 通过id删除汇聚三农分类
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除汇聚三农分类", notes = "通过id删除汇聚三农分类")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szfarmcate_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szFarmCateService.removeById(id));
    }

}
