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
import com.px.pa.modulars.core.entity.SzShopOrder;
import com.px.pa.modulars.core.service.SzShopGoodsService;
import com.px.pa.modulars.core.service.SzShopOrderService;
import com.px.pa.modulars.vo.ShopOrderParam;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 积分商品兑换记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szshoporder" )
@Api(value = "szshoporder", tags = "积分商品兑换记录管理")
public class SzShopOrderController {

    private final  SzShopOrderService szShopOrderService;
    private final SzShopGoodsService szShopGoodsService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szShopOrder 积分商品兑换记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szshoporder_get')" )
    public R getSzShopOrderPage(Page page, ShopOrderParam szShopOrder) {
        return R.ok(szShopOrderService.getPage(page,szShopOrder));
    }


    /**
     * 通过id查询积分商品兑换记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szshoporder_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szShopOrderService.getById(id));
    }

    /**
     * 通过id查询积分商品兑换记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "核销订单", notes = "核销订单")
    @GetMapping("/hx/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szshoporder_hx')" )
    public R hx(@PathVariable("id" ) Integer id) {
        if(szShopOrderService.hx(id)){
            return R.ok("核销成功");
        }else{
            return R.failed("核销失败");
        }
    }

    /**
     * 新增积分商品兑换记录
     * @param szShopOrder 积分商品兑换记录
     * @return R
     */
    @ApiOperation(value = "新增积分商品兑换记录", notes = "新增积分商品兑换记录")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szshoporder_add')" )
    public R save(@RequestBody SzShopOrder szShopOrder) {
        return R.ok(szShopOrderService.save(szShopOrder));
    }

    /**
     * 修改积分商品兑换记录
     * @param szShopOrder 积分商品兑换记录
     * @return R
     */
    @ApiOperation(value = "修改积分商品兑换记录", notes = "修改积分商品兑换记录")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szshoporder_edit')" )
    public R updateById(@RequestBody SzShopOrder szShopOrder) {
        return R.ok(szShopOrderService.updateById(szShopOrder));
    }

    /**
     * 通过id删除积分商品兑换记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除积分商品兑换记录", notes = "通过id删除积分商品兑换记录")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szshoporder_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szShopOrderService.removeById(id));
    }


    /**
     * 取消核销
     * @param
     * @return R
     */
    @ApiOperation(value = "核销订单", notes = "核销订单")
    @GetMapping("/qxhx/{id}" )
    public R getqxhx(@PathVariable Integer id){
       SzShopOrder szShopOrder = szShopOrderService.getById(id);
       if(szShopOrder.getState() == 1 || szShopOrder.getState() ==2){  //状态：0-待核销，1-已核销，2-核销失败
           szShopOrder.setState(0);
         SzShopGoods szShopGoods = szShopGoodsService.getById(szShopOrder.getGid());
         if(szShopGoods.getFinish() == null && szShopGoods.getFinish()==0){
             szShopGoods.setFinish(0);
             szShopGoodsService.updateById(szShopGoods);
         }else {
             szShopGoods.setFinish(szShopGoods.getFinish() -1);
             szShopGoodsService.updateById(szShopGoods);
         }


       }
        szShopOrderService.updateById(szShopOrder);
       return R.ok();

    }


}
