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
import com.px.pa.modulars.core.entity.SzGuideRecord;
import com.px.pa.modulars.core.service.SzGuideRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 办事指南申请记录
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szguiderecord" )
@Api(value = "szguiderecord", tags = "办事指南申请记录管理")
public class SzGuideRecordController {

    private final  SzGuideRecordService szGuideRecordService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szGuideRecord 办事指南申请记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szguiderecord_get')" )
    public R getSzGuideRecordPage(Page page, SzGuideRecord szGuideRecord) {
        return R.ok(szGuideRecordService.getPage(page,szGuideRecord));
    }


    /**
     * 通过id查询办事指南申请记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szguiderecord_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szGuideRecordService.getById(id));
    }

    /**
     * 新增办事指南申请记录
     * @param szGuideRecord 办事指南申请记录
     * @return R
     */
    @ApiOperation(value = "新增办事指南申请记录", notes = "新增办事指南申请记录")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szguiderecord_add')" )
    public R save(@RequestBody SzGuideRecord szGuideRecord) {
        return R.ok(szGuideRecordService.save(szGuideRecord));
    }

    /**
     * 修改办事指南申请记录
     * @param szGuideRecord 办事指南申请记录
     * @return R
     */
    @ApiOperation(value = "修改办事指南申请记录", notes = "修改办事指南申请记录")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szguiderecord_edit')" )
    public R updateById(@RequestBody SzGuideRecord szGuideRecord) {
        return R.ok(szGuideRecordService.updateById(szGuideRecord));
    }

    /**
     * 通过id删除办事指南申请记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除办事指南申请记录", notes = "通过id删除办事指南申请记录")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szguiderecord_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szGuideRecordService.removeById(id));
    }

}
