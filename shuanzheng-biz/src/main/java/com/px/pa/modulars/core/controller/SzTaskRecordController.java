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
import com.px.pa.modulars.core.entity.SzTaskRecord;
import com.px.pa.modulars.core.service.SzTaskRecordService;
import com.px.pa.modulars.vo.TaskRecordParam;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 任务完成记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sztaskrecord" )
@Api(value = "sztaskrecord", tags = "任务完成记录管理")
public class SzTaskRecordController {

    private final  SzTaskRecordService szTaskRecordService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szTaskRecord 任务完成记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_sztaskrecord_get')" )
    public R getSzTaskRecordPage(Page page, TaskRecordParam szTaskRecord) {
        return R.ok(szTaskRecordService.getPage(page,szTaskRecord));
    }


    /**
     * 通过id查询任务完成记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_sztaskrecord_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szTaskRecordService.getById(id));
    }

    /**
     * 新增任务完成记录
     * @param szTaskRecord 任务完成记录
     * @return R
     */
    @ApiOperation(value = "新增任务完成记录", notes = "新增任务完成记录")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_sztaskrecord_add')" )
    public R save(@RequestBody SzTaskRecord szTaskRecord) {

        if(szTaskRecordService.saveCompanyTask(szTaskRecord)){
            return R.ok("提交成功");
        }else{
            return R.ok("提交失败");
        }
    }

    /**
     * 修改任务完成记录
     * @param szTaskRecord 任务完成记录
     * @return R
     */
    @ApiOperation(value = "修改任务完成记录", notes = "修改任务完成记录")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_sztaskrecord_edit')" )
    public R updateById(@RequestBody SzTaskRecord szTaskRecord) {
        if(szTaskRecordService.shenhe(szTaskRecord)){
            return R.ok("审核成功");
        }else{
            return R.ok("审核失败");
        }
    }

    /**
     * 通过id删除任务完成记录
     * @return R
     */
    @ApiOperation(value = "通过id删除任务完成记录", notes = "通过id删除任务完成记录")
    @PostMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_sztaskrecord_del')" )
    public R removeById(@RequestBody SzTaskRecord param) {
        SzTaskRecord szTaskRecord = szTaskRecordService.getById(param.getId());
        szTaskRecord.setMark(param.getMark());
        if(StrUtil.isNotEmpty(param.getResult())){
            szTaskRecord.setResult(param.getResult());
        }
        szTaskRecord.setDelFlag("1");
        szTaskRecordService.updateById(szTaskRecord);
        return R.ok();
    }

}
