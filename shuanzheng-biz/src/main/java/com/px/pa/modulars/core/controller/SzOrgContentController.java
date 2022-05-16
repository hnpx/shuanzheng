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
import com.px.pa.modulars.core.entity.SzOrgContent;
import com.px.pa.modulars.core.service.SzOrgContentService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 政务信息内容
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szorgcontent" )
@Api(value = "szorgcontent", tags = "政务信息内容管理")
public class SzOrgContentController {

    private final  SzOrgContentService szOrgContentService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szOrgContent 政务信息内容
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szorgcontent_get')" )
    public R getSzOrgContentPage(Page page, SzOrgContent szOrgContent) {
        return R.ok(szOrgContentService.lambdaQuery()
                .select(SzOrgContent::getId,SzOrgContent::getName,SzOrgContent::getImage,SzOrgContent::getState,SzOrgContent::getSort,SzOrgContent::getCid,SzOrgContent::getVideo,SzOrgContent::getCreateTime)
                .eq(szOrgContent.getState()!=null, SzOrgContent::getState,szOrgContent.getState())
                .eq(szOrgContent.getCid()!=null, SzOrgContent::getCid,szOrgContent.getCid())
                .like(StrUtil.isNotEmpty(szOrgContent.getName()),SzOrgContent::getName,szOrgContent.getName())
                .orderByDesc(SzOrgContent::getCreateTime)
                .page(page));
    }


    /**
     * 通过id查询政务信息内容
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szorgcontent_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szOrgContentService.getById(id));
    }

    /**
     * 新增政务信息内容
     * @param szOrgContent 政务信息内容
     * @return R
     */
    @ApiOperation(value = "新增政务信息内容", notes = "新增政务信息内容")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szorgcontent_add')" )
    public R save(@RequestBody SzOrgContent szOrgContent) {
        return R.ok(szOrgContentService.save(szOrgContent));
    }

    /**
     * 修改政务信息内容
     * @param szOrgContent 政务信息内容
     * @return R
     */
    @ApiOperation(value = "修改政务信息内容", notes = "修改政务信息内容")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szorgcontent_edit')" )
    public R updateById(@RequestBody SzOrgContent szOrgContent) {
        return R.ok(szOrgContentService.updateById(szOrgContent));
    }

    /**
     * 通过id删除政务信息内容
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除政务信息内容", notes = "通过id删除政务信息内容")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szorgcontent_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szOrgContentService.removeById(id));
    }

}
