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
import com.px.pa.modulars.core.entity.SzShopGoods;
import com.px.pa.modulars.core.service.SzShopGoodsService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 积分商品
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szshopgoods" )
@Api(value = "szshopgoods", tags = "积分商品管理")
public class SzShopGoodsController {

    private final  SzShopGoodsService szShopGoodsService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szShopGoods 积分商品
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szshopgoods_get')" )
    public R getSzShopGoodsPage(Page page, SzShopGoods szShopGoods) {
        return R.ok(szShopGoodsService.lambdaQuery()
                .select(SzShopGoods::getId,SzShopGoods::getName,SzShopGoods::getImage,SzShopGoods::getState,
                        SzShopGoods::getSort,SzShopGoods::getCid,SzShopGoods::getStock,SzShopGoods::getCreateTime,
                        SzShopGoods::getPoints,SzShopGoods::getViews,SzShopGoods::getBuys,SzShopGoods::getFinish)
                .eq(szShopGoods.getState()!=null, SzShopGoods::getState,szShopGoods.getState())
                .eq(szShopGoods.getCid()!=null, SzShopGoods::getCid,szShopGoods.getCid())
                .like(StrUtil.isNotEmpty(szShopGoods.getName()),SzShopGoods::getName,szShopGoods.getName())
                .orderByDesc(SzShopGoods::getCreateTime)
                .page(page));
    }


    /**
     * 通过id查询积分商品
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szshopgoods_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szShopGoodsService.getById(id));
    }

    /**
     * 新增积分商品
     * @param szShopGoods 积分商品
     * @return R
     */
    @ApiOperation(value = "新增积分商品", notes = "新增积分商品")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szshopgoods_add')" )
    public R save(@RequestBody SzShopGoods szShopGoods) {
        return R.ok(szShopGoodsService.save(szShopGoods));
    }

    /**
     * 修改积分商品
     * @param szShopGoods 积分商品
     * @return R
     */
    @ApiOperation(value = "修改积分商品", notes = "修改积分商品")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szshopgoods_edit')" )
    public R updateById(@RequestBody SzShopGoods szShopGoods) {
        return R.ok(szShopGoodsService.updateById(szShopGoods));
    }

    /**
     * 通过id删除积分商品
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除积分商品", notes = "通过id删除积分商品")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szshopgoods_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szShopGoodsService.removeById(id));
    }

}
