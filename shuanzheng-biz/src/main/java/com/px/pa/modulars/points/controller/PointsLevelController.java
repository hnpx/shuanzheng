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

package com.px.pa.modulars.points.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.pa.modulars.points.entity.PointsLevel;
import com.px.pa.modulars.points.service.PointsLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 栓正积分星级配置
 *
 * @author 吕郭飞
 * @date 2021-05-07 14:27:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pointslevel" )
@Api(value = "pointslevel", tags = "栓正积分星级配置管理")
public class PointsLevelController {

    private final  PointsLevelService pointsLevelService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param pointsLevel 栓正积分星级配置
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getPointsLevelPage(Page page, PointsLevel pointsLevel) {
        return R.ok(pointsLevelService.page(page, Wrappers.query(pointsLevel)));
    }


    /**
     * 通过id查询栓正积分星级配置
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(pointsLevelService.getById(id));
    }

    /**
     * 新增栓正积分星级配置
     * @param pointsLevel 栓正积分星级配置
     * @return R
     */
    @ApiOperation(value = "新增栓正积分星级配置", notes = "新增栓正积分星级配置")
    @PostMapping
    public R save(@RequestBody PointsLevel pointsLevel) {
        return R.ok(pointsLevelService.save(pointsLevel));
    }

    /**
     * 修改栓正积分星级配置
     * @param pointsLevel 栓正积分星级配置
     * @return R
     */
    @ApiOperation(value = "修改栓正积分星级配置", notes = "修改栓正积分星级配置")
    @PutMapping
    public R updateById(@RequestBody PointsLevel pointsLevel) {
        return R.ok(pointsLevelService.updateById(pointsLevel));
    }

    /**
     * 通过id删除栓正积分星级配置
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除栓正积分星级配置", notes = "通过id删除栓正积分星级配置")
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(pointsLevelService.removeById(id));
    }

}
