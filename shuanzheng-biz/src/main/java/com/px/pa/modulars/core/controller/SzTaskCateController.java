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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.pa.modulars.core.entity.SzTaskCate;
import com.px.pa.modulars.core.service.SzTaskCateService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 任务分类
 *
 * @author pig code generator
 * @date 2021-04-23 17:14:21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sztaskcate" )
@Api(value = "sztaskcate", tags = "任务分类管理")
public class SzTaskCateController {

    private final  SzTaskCateService szTaskCateService;

    /**
     * 分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_sztaskcate_get')" )
    public R getSzTaskCatePage(@RequestParam("type") Integer type,@RequestParam(required = false) Integer parentId ) {
        return R.ok(szTaskCateService.cateTree(type,parentId));
    }


    /**
     * 通过id查询任务分类
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_sztaskcate_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szTaskCateService.getById(id));
    }

    /**
     * 新增任务分类
     * @param szTaskCate 任务分类
     * @return R
     */
    @ApiOperation(value = "新增任务分类", notes = "新增任务分类")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_sztaskcate_add')" )
    public R save(@RequestBody SzTaskCate szTaskCate) {
        return R.ok(szTaskCateService.save(szTaskCate));
    }

    /**
     * 修改任务分类
     * @param szTaskCate 任务分类
     * @return R
     */
    @ApiOperation(value = "修改任务分类", notes = "修改任务分类")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_sztaskcate_edit')" )
    public R updateById(@RequestBody SzTaskCate szTaskCate) {
        return R.ok(szTaskCateService.updateById(szTaskCate));
    }

    /**
     * 通过id删除任务分类
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除任务分类", notes = "通过id删除任务分类")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_sztaskcate_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szTaskCateService.removeById(id));
    }



    /**
     *
     * 任务列表父级
     */
    @ApiOperation(value = "任务列表父级", notes = "任务列表父级")
    @GetMapping("/parent/list")
    public R getList(){
       List<SzTaskCate> szTaskCateList = szTaskCateService.list(new QueryWrapper<SzTaskCate>().eq("parent_id",0)
               .eq("type",1).eq("del_flag",0));
       return R.ok(szTaskCateList);

    }

    @ApiOperation(value = "获取分类树", notes = "获取分类树")
    @GetMapping("/catetree" )
    @PreAuthorize("@pms.hasPermission('core_sztaskcate_get')" )
    public R getVillageTree() {
        return R.ok(szTaskCateService.cateTree(null,null));
    }
}
