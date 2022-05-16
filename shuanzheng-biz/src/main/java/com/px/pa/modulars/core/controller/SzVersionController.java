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
import com.px.basic.alone.security.annotation.Inner;
import com.px.pa.modulars.core.entity.SzVersion;
import com.px.pa.modulars.core.service.SzVersionService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 
 *
 * @author pig code generator
 * @date 2021-04-29 17:10:16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szversion" )
@Api(value = "szversion", tags = "管理")
public class SzVersionController {

    private final  SzVersionService szVersionService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szVersion 
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    //@PreAuthorize("@pms.hasPermission('szVersion_szversion_get')" )
    public R getSzVersionPage(Page page, SzVersion szVersion) {
        return R.ok(szVersionService.page(page, Wrappers.query(szVersion)));
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    //@PreAuthorize("@pms.hasPermission('szVersion_szversion_get')" )
    @Inner(value = false)
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szVersionService.getById(id));
    }

    /**
     * 新增
     * @param szVersion 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping
   // @PreAuthorize("@pms.hasPermission('szVersion_szversion_add')" )
    public R save(@RequestBody SzVersion szVersion) {
        return R.ok(szVersionService.save(szVersion));
    }

    /**
     * 修改
     * @param szVersion 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @PutMapping
    //@PreAuthorize("@pms.hasPermission('szVersion_szversion_edit')" )
    public R updateById(@RequestBody SzVersion szVersion) {
        return R.ok(szVersionService.updateById(szVersion));
    }

    /**
     * 通过id删除
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @DeleteMapping("/{id}" )
   // @PreAuthorize("@pms.hasPermission('szVersion_szversion_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szVersionService.removeById(id));
    }

}
