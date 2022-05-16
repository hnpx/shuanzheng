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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.pa.modulars.core.entity.SzFarm;
import com.px.pa.modulars.core.entity.SzFarmCate;
import com.px.pa.modulars.core.service.SzFarmService;
import com.px.pa.modulars.core.vo.ObjectVo;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 汇聚三农
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szfarm" )
@Api(value = "szfarm", tags = "汇聚三农管理")
public class SzFarmController {

    private final  SzFarmService szFarmService;
    private final SysDeptService sysDeptService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szFarm 汇聚三农
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szfarm_get')" )
    public R getSzFarmPage(Page page, SzFarm szFarm) {

        return R.ok(szFarmService.lambdaQuery()
                .select(SzFarm::getId,SzFarm::getName,SzFarm::getImage,SzFarm::getState,SzFarm::getSort,SzFarm::getCid,SzFarm::getViews,SzFarm::getCreateTime,SzFarm::getDept,SzFarm::getType,SzFarm::getUpdateTime)
                .eq(szFarm.getState()!=null,SzFarm::getState,szFarm.getState())
                .eq(szFarm.getCid()!=null,SzFarm::getCid,szFarm.getCid())
                .eq(szFarm.getDept() != null,SzFarm::getDept,szFarm.getDept())
                .gt(szFarm.getStartTime()!= null,SzFarm::getCreateTime,szFarm.getStartTime())
                .lt(szFarm.getEndTime()!= null,SzFarm::getCreateTime,szFarm.getEndTime())
                .like(StrUtil.isNotEmpty(szFarm.getName()), SzFarm::getName,szFarm.getName())
                .orderByDesc(SzFarm::getCreateTime)
                .page(page));
    }


    /**
     * 通过id查询汇聚三农
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szfarm_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szFarmService.getById(id));
    }

    /**
     * 新增汇聚三农
     * @param szFarm 汇聚三农
     * @return R
     */
    @ApiOperation(value = "新增汇聚三农", notes = "新增汇聚三农")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szfarm_add')" )
    public R save(@RequestBody SzFarm szFarm) {

        szFarm.setCreateTime(LocalDateTime.now());
        szFarm.setUpdateTime(LocalDateTime.now());
        return R.ok(szFarmService.save(szFarm));
    }

    /**
     * 修改汇聚三农
     * @param szFarm 汇聚三农
     * @return R
     */
    @ApiOperation(value = "修改汇聚三农", notes = "修改汇聚三农")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szfarm_edit')" )
    public R updateById(@RequestBody SzFarm szFarm) {

        szFarm.setUpdateTime(LocalDateTime.now());
        return R.ok(szFarmService.updateById(szFarm));
    }

    /**
     * 通过id删除汇聚三农
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除汇聚三农", notes = "通过id删除汇聚三农")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szfarm_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szFarmService.removeById(id));
    }


    /**
     * 所属村庄
     *
     *
     */
    @ApiOperation(value = "所属村庄", notes = "所属村庄")
    @GetMapping("/sysDept" )
    public R getSysDept(){

     List<SysDept> sysDeptList =   sysDeptService.list(new QueryWrapper<SysDept>().eq("del_flag",0).orderByAsc("sort").orderByDesc("create_time"));
     List<ObjectVo> objectVoList = new ArrayList<>();
        sysDeptList.forEach(sysDept -> {
            ObjectVo objectVo = new ObjectVo();
            objectVo.setDid(sysDept.getDeptId());
            objectVo.setName(sysDept.getName());
            objectVoList.add(objectVo);
        });

        return R.ok(objectVoList);
    }

}
