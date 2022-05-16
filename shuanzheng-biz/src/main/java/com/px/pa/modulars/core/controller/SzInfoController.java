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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.pa.modulars.core.entity.SzInfo;
import com.px.pa.modulars.core.service.SzInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 栓正释义
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szinfo" )
@Api(value = "szinfo", tags = "栓正释义管理")
public class SzInfoController {

    private final  SzInfoService szInfoService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szInfo 栓正释义
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szinfo_get')" )
    public R getSzInfoPage(Page page, SzInfo szInfo) {
        return R.ok(szInfoService.lambdaQuery().one());
    }


    /**
     * 通过id查询栓正释义
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szinfo_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szInfoService.getById(id));
    }

    /**
     * 新增栓正释义
     * @param szInfo 栓正释义
     * @return R
     */
    @ApiOperation(value = "新增栓正释义", notes = "新增栓正释义")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szinfo_add')" )
    public R save(@RequestBody SzInfo szInfo) {
        return R.ok(szInfoService.save(szInfo));
    }

    /**
     * 修改栓正释义
     * @param szInfo 栓正释义
     * @return R
     */
    @ApiOperation(value = "修改栓正释义", notes = "修改栓正释义")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szinfo_edit')" )
    public R updateById(@RequestBody SzInfo szInfo) {
        return R.ok(szInfoService.updateById(szInfo));
    }

    /**
     * 通过id删除栓正释义
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除栓正释义", notes = "通过id删除栓正释义")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szinfo_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szInfoService.removeById(id));
    }

}
