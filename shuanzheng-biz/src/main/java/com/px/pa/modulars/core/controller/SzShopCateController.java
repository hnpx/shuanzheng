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
import com.px.pa.modulars.core.entity.SzShopCate;
import com.px.pa.modulars.core.service.SzShopCateService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 商品分类
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szshopcate" )
@Api(value = "szshopcate", tags = "商品分类管理")
public class SzShopCateController {

    private final  SzShopCateService szShopCateService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szShopCate 商品分类
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szshopcate_get')" )
    public R getSzShopCatePage(Page page, SzShopCate szShopCate) {
        return R.ok(szShopCateService.lambdaQuery()
                .eq(szShopCate.getState()!=null, SzShopCate::getState,szShopCate.getState())
                .like(StrUtil.isNotEmpty(szShopCate.getName()),SzShopCate::getName,szShopCate.getName())
                .orderByDesc(SzShopCate::getCreateTime)
                .page(page));
    }

    @ApiOperation(value = "获取列表", notes = "获取列表")
    @GetMapping("/list" )
    @PreAuthorize("@pms.hasPermission('core_szshopcate_get')" )
    public R getSzShopCateList() {
        return R.ok(szShopCateService.lambdaQuery()
                .eq(SzShopCate::getState,1)
                .orderByDesc(SzShopCate::getCreateTime)
                .list());
    }


    /**
     * 通过id查询商品分类
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szshopcate_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szShopCateService.getById(id));
    }

    /**
     * 新增商品分类
     * @param szShopCate 商品分类
     * @return R
     */
    @ApiOperation(value = "新增商品分类", notes = "新增商品分类")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szshopcate_add')" )
    public R save(@RequestBody SzShopCate szShopCate) {
        return R.ok(szShopCateService.save(szShopCate));
    }

    /**
     * 修改商品分类
     * @param szShopCate 商品分类
     * @return R
     */
    @ApiOperation(value = "修改商品分类", notes = "修改商品分类")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szshopcate_edit')" )
    public R updateById(@RequestBody SzShopCate szShopCate) {
        return R.ok(szShopCateService.updateById(szShopCate));
    }

    /**
     * 通过id删除商品分类
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除商品分类", notes = "通过id删除商品分类")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szshopcate_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szShopCateService.removeById(id));
    }

}
