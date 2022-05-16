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
import com.px.pa.modulars.core.entity.SzGuideFlow;
import com.px.pa.modulars.core.service.SzGuideFlowService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 办事指南审核记录
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szguideflow" )
@Api(value = "szguideflow", tags = "办事指南审核记录管理")
public class SzGuideFlowController {

    private final  SzGuideFlowService szGuideFlowService;

    /**
     * 分页查询
     * @param szGuideFlow 办事指南审核记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szguideflow_get')" )
    public R getSzGuideFlowPage(SzGuideFlow szGuideFlow) {
        return R.ok(szGuideFlowService.flowList(szGuideFlow));
    }


    /**
     * 通过id查询办事指南审核记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szguideflow_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szGuideFlowService.getById(id));
    }

    /**
     * 新增办事指南审核记录
     * @param szGuideFlow 办事指南审核记录
     * @return R
     */
    @ApiOperation(value = "新增办事指南审核记录", notes = "新增办事指南审核记录")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szguideflow_add')" )
    public R save(@RequestBody SzGuideFlow szGuideFlow) {
        return R.ok(szGuideFlowService.save(szGuideFlow));
    }

    /**
     * 修改办事指南审核记录
     * @param szGuideFlow 办事指南审核记录
     * @return R
     */
    @ApiOperation(value = "修改办事指南审核记录", notes = "修改办事指南审核记录")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szguideflow_edit')" )
    public R updateById(@RequestBody SzGuideFlow szGuideFlow) {
        return R.ok(szGuideFlowService.updateById(szGuideFlow));
    }

    /**
     * 通过id删除办事指南审核记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除办事指南审核记录", notes = "通过id删除办事指南审核记录")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szguideflow_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szGuideFlowService.removeById(id));
    }

}
