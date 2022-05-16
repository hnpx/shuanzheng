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
import com.px.pa.modulars.core.entity.SzVote;
import com.px.pa.modulars.core.entity.SzVotePoints;
import com.px.pa.modulars.core.service.SzVotePointsService;
import com.px.pa.modulars.core.service.SzVoteService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 投票奖励积分
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szvotepoints" )
@Api(value = "szvotepoints", tags = "投票奖励积分管理")
public class SzVotePointsController {

    private final  SzVotePointsService szVotePointsService;
    private final SzVoteService szVoteService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szVotePoints 投票奖励积分
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szvotepoints_get')" )
    public R getSzVotePointsPage(Page page, SzVotePoints szVotePoints) {
      Page<SzVotePoints> szVotePointsPage =  szVotePointsService.lambdaQuery()
                .eq(SzVotePoints::getVid,szVotePoints.getVid())
                .orderByDesc(SzVotePoints::getCreateTime)
                .page(page);
        szVotePointsPage.getRecords().forEach(szVotePoints1 -> {
          SzVote szVote = szVoteService.getById(szVotePoints1.getVid());
            szVotePoints1.setIntegralStatus(szVote.getIntegralStatus());
        });
        return R.ok(szVotePointsPage);
    }


    /**
     * 通过id查询投票奖励积分
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szvotepoints_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szVotePointsService.getById(id));
    }

    /**
     * 新增投票奖励积分
     * @param szVotePoints 投票奖励积分
     * @return R
     */
    @ApiOperation(value = "新增投票奖励积分", notes = "新增投票奖励积分")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szvotepoints_add')" )
    public R save(@RequestBody SzVotePoints szVotePoints) {


        return R.ok(szVotePointsService.save(szVotePoints));
    }

    /**
     * 修改投票奖励积分
     * @param szVotePoints 投票奖励积分
     * @return R
     */
    @ApiOperation(value = "修改投票奖励积分", notes = "修改投票奖励积分")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szvotepoints_edit')" )
    public R updateById(@RequestBody SzVotePoints szVotePoints) {
        return R.ok(szVotePointsService.updateById(szVotePoints));
    }

    /**
     * 通过id删除投票奖励积分
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除投票奖励积分", notes = "通过id删除投票奖励积分")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szvotepoints_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szVotePointsService.removeById(id));
    }

}
