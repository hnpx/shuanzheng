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
import com.px.pa.modulars.core.entity.SzCompany;
import com.px.pa.modulars.core.service.SzCompanyService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 栓正企业
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szcompany" )
@Api(value = "szcompany", tags = "栓正企业管理")
public class SzCompanyController {

    private final  SzCompanyService szCompanyService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szCompany 栓正企业
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szcompany_get')" )
    public R getSzCompanyPage(Page page, SzCompany szCompany) {
        return R.ok(szCompanyService.lambdaQuery()
                .select(SzCompany::getId,SzCompany::getName,SzCompany::getImage,SzCompany::getState,SzCompany::getSort,SzCompany::getAllScore,SzCompany::getScore,SzCompany::getCreateTime)
                .eq(szCompany.getState()!=null, SzCompany::getState,szCompany.getState())
                .like(StrUtil.isNotEmpty(szCompany.getName()), SzCompany::getName,szCompany.getName())
                .orderByDesc(SzCompany::getCreateTime)
                .page(page));
    }


    /**
     * 通过id查询栓正企业
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szcompany_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szCompanyService.getById(id));
    }

    /**
     * 新增栓正企业
     * @param szCompany 栓正企业
     * @return R
     */
    @ApiOperation(value = "新增栓正企业", notes = "新增栓正企业")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szcompany_add')" )
    public R save(@RequestBody SzCompany szCompany) {
        return R.ok(szCompanyService.save(szCompany));
    }

    /**
     * 修改栓正企业
     * @param szCompany 栓正企业
     * @return R
     */
    @ApiOperation(value = "修改栓正企业", notes = "修改栓正企业")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szcompany_edit')" )
    public R updateById(@RequestBody SzCompany szCompany) {
        return R.ok(szCompanyService.updateById(szCompany));
    }

    /**
     * 通过id删除栓正企业
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除栓正企业", notes = "通过id删除栓正企业")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szcompany_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szCompanyService.removeById(id));
    }

}
