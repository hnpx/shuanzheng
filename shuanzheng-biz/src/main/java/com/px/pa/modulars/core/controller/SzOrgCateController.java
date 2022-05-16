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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.pa.modulars.core.entity.SzFarmCate;
import com.px.pa.modulars.core.entity.SzOrgCate;
import com.px.pa.modulars.core.entity.SzOrgContent;
import com.px.pa.modulars.core.service.SzOrgCateService;
import com.px.pa.modulars.core.service.SzOrgContentService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 政务信息分类
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szorgcate" )
@Api(value = "szorgcate", tags = "政务信息分类管理")
public class SzOrgCateController {

    private final  SzOrgCateService szOrgCateService;
    private final SzOrgContentService szOrgContentService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szOrgCate 政务信息分类
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szorgcate_get')" )
    public R getSzOrgCatePage(Page page, SzOrgCate szOrgCate) {
        return R.ok(szOrgCateService.lambdaQuery()
                .eq(szOrgCate.getState()!=null, SzOrgCate::getState,szOrgCate.getState())
                .like(StrUtil.isNotEmpty(szOrgCate.getName()),SzOrgCate::getName,szOrgCate.getName())
                .orderByDesc(SzOrgCate::getCreateTime)
                .page(page));
    }

    @ApiOperation(value = "获取列表", notes = "获取列表")
    @GetMapping("/list" )
    @PreAuthorize("@pms.hasPermission('core_szorgcate_get')" )
    public R getSzOrgCateList() {
        return R.ok(szOrgCateService.lambdaQuery()
                .eq(SzOrgCate::getState,1)
                .orderByDesc(SzOrgCate::getCreateTime)
                .list());
    }

    /**
     * 通过id查询政务信息分类
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szorgcate_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szOrgCateService.getById(id));
    }

    /**
     * 新增政务信息分类
     * @param szOrgCate 政务信息分类
     * @return R
     */
    @ApiOperation(value = "新增政务信息分类", notes = "新增政务信息分类")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szorgcate_add')" )
    public R save(@RequestBody SzOrgCate szOrgCate) {
        return R.ok(szOrgCateService.save(szOrgCate));
    }

    /**
     * 修改政务信息分类
     * @param szOrgCate 政务信息分类
     * @return R
     */
    @ApiOperation(value = "修改政务信息分类", notes = "修改政务信息分类")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szorgcate_edit')" )
    public R updateById(@RequestBody SzOrgCate szOrgCate) {
        return R.ok(szOrgCateService.updateById(szOrgCate));
    }

    /**
     * 通过id删除政务信息分类
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除政务信息分类", notes = "通过id删除政务信息分类")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szorgcate_del')" )
    public R removeById(@PathVariable Integer id) {

      Integer count =  szOrgContentService.count(new QueryWrapper<SzOrgContent>().eq("cid",id));
      if(count>0){
          return R.failed("此栏目下绑定有内容，请先修改内容所属栏目再删除");
      }
        return R.ok(szOrgCateService.removeById(id));
    }

}
