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
import com.px.pa.modulars.core.entity.SzPointsLog;
import com.px.pa.modulars.core.service.SzPointsLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 积分记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szpointslog" )
@Api(value = "szpointslog", tags = "积分记录管理")
public class SzPointsLogController {

    private final  SzPointsLogService szPointsLogService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szPointsLog 积分记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szpointslog_get')" )
    public R getSzPointsLogPage(Page page, SzPointsLog szPointsLog) {
        return R.ok(szPointsLogService.lambdaQuery()
                .eq(szPointsLog.getOid()!=null, SzPointsLog::getOid,szPointsLog.getOid())
                .eq(szPointsLog.getOperator()!=null, SzPointsLog::getOperator,szPointsLog.getOperator())
                .eq(szPointsLog.getType()!=null, SzPointsLog::getType,szPointsLog.getType())
                .eq(szPointsLog.getOpType()!=null, SzPointsLog::getOpType,szPointsLog.getOpType())
                .orderByDesc(SzPointsLog::getCreateTime)
                .page(page));
    }


    /**
     * 通过id查询积分记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szpointslog_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szPointsLogService.getById(id));
    }

    /**
     * 新增积分记录
     * @param szPointsLog 积分记录
     * @return R
     */
    @ApiOperation(value = "新增积分记录", notes = "新增积分记录")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szpointslog_add')" )
    public R save(@RequestBody SzPointsLog szPointsLog) {
        return R.ok(szPointsLogService.saveLog(szPointsLog));
    }

    /**
     * 修改积分记录
     * @param szPointsLog 积分记录
     * @return R
     */
    @ApiOperation(value = "修改积分记录", notes = "修改积分记录")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szpointslog_edit')" )
    public R updateById(@RequestBody SzPointsLog szPointsLog) {
        return R.ok(szPointsLogService.updateById(szPointsLog));
    }

    /**
     * 通过id删除积分记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除积分记录", notes = "通过id删除积分记录")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szpointslog_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szPointsLogService.removeById(id));
    }

}
