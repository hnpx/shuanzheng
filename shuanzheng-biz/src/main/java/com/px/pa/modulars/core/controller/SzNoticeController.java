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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.pa.modulars.core.entity.SzNotice;
import com.px.pa.modulars.core.service.SzNoticeService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 发布公告
 *
 * @author pig code generator
 * @date 2021-04-23 11:21:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sznotice" )
@Api(value = "sznotice", tags = "发布公告管理")
public class SzNoticeController {

    private final  SzNoticeService szNoticeService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szNotice 发布公告
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_sznotice_get')" )
    public R getSzNoticePage(Page page, SzNotice szNotice) {
        return R.ok(szNoticeService.lambdaQuery()
                .select(SzNotice::getId,SzNotice::getName,SzNotice::getState,SzNotice::getSort,SzNotice::getCreateTime,SzNotice::getImage)
                .eq(szNotice.getState()!=null, SzNotice::getState,szNotice.getState())
                .like(StrUtil.isNotEmpty(szNotice.getName()),SzNotice::getName,szNotice.getName())
                .orderByDesc(SzNotice::getCreateTime)
                .page(page));
    }


    /**
     * 通过id查询发布公告
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_sznotice_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szNoticeService.getById(id));
    }

    /**
     * 新增发布公告
     * @param szNotice 发布公告
     * @return R
     */
    @ApiOperation(value = "新增发布公告", notes = "新增发布公告")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_sznotice_add')" )
    public R save(@RequestBody SzNotice szNotice) {
        return R.ok(szNoticeService.save(szNotice));
    }

    /**
     * 修改发布公告
     * @param szNotice 发布公告
     * @return R
     */
    @ApiOperation(value = "修改发布公告", notes = "修改发布公告")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_sznotice_edit')" )
    public R updateById(@RequestBody SzNotice szNotice) {


        return R.ok(szNoticeService.updateById(szNotice));
    }

    /**
     * 通过id删除发布公告
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除发布公告", notes = "通过id删除发布公告")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_sznotice_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szNoticeService.removeById(id));
    }

}
