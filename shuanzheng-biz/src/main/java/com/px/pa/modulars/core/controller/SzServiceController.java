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
import com.px.pa.modulars.core.entity.SzService;
import com.px.pa.modulars.core.service.SzServiceService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 便民服务
 *
 * @author pig code generator
 * @date 2021-05-12 14:40:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szservice" )
@Api(value = "szservice", tags = "便民服务管理")
public class SzServiceController {

    private final  SzServiceService szServiceService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szService 便民服务
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szservice_get')" )
    public R getSzServicePage(Page page, SzService szService) {
        return R.ok(szServiceService.lambdaQuery()
                .eq(szService.getState()!=null, SzService::getState,szService.getState())
                .eq(szService.getType()!=null, SzService::getType,szService.getType())
                .like(StrUtil.isNotEmpty(szService.getName()),SzService::getName,szService.getName())
                .orderByDesc(SzService::getCreateTime)
                .page(page));
    }


    /**
     * 通过id查询便民服务
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szservice_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szServiceService.getById(id));
    }

    /**
     * 新增便民服务
     * @param szService 便民服务
     * @return R
     */
    @ApiOperation(value = "新增便民服务", notes = "新增便民服务")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szservice_add')" )
    public R save(@RequestBody SzService szService) {
        return R.ok(szServiceService.save(szService));
    }

    /**
     * 修改便民服务
     * @param szService 便民服务
     * @return R
     */
    @ApiOperation(value = "修改便民服务", notes = "修改便民服务")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szservice_edit')" )
    public R updateById(@RequestBody SzService szService) {
        return R.ok(szServiceService.updateById(szService));
    }

    /**
     * 通过id删除便民服务
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除便民服务", notes = "通过id删除便民服务")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szservice_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szServiceService.removeById(id));
    }

}
