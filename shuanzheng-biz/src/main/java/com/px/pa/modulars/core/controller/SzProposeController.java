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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.pa.constants.SuggestTypeEnum;
import com.px.pa.modulars.core.entity.SzSuggest;
import com.px.pa.modulars.core.service.SzSuggestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 村民需求
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szpropose" )
@Api(value = "szpropose", tags = "村民意见建议管理")
public class SzProposeController {

    private final  SzSuggestService szSuggestService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szSuggest 村民需求
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szpropose_get')" )
    public R getSzSuggestPage(Page page, SzSuggest szSuggest) {
        if(szSuggest==null){
            szSuggest=new SzSuggest();
        }
        szSuggest.setType(SuggestTypeEnum.SUGGEST.getValue());
        return R.ok(szSuggestService.getPage(page, szSuggest));
    }

    @ApiOperation(value = "处理结果", notes = "处理结果")
    @GetMapping("/deal" )
    @PreAuthorize("@pms.hasPermission('core_szpropose_deal')" )
    public R getSzSuggestDeal(SzSuggest szSuggest) {
        if(szSuggestService.deal(szSuggest)){
            return R.ok("处理成功");
        }else{
            return R.failed("处理失败");
        }
    }

    /**
     * 通过id查询村民需求
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szpropose_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szSuggestService.getById(id));
    }

    /**
     * 新增村民需求
     * @param szSuggest 村民需求
     * @return R
     */
    @ApiOperation(value = "新增村民需求", notes = "新增村民需求")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szpropose_add')" )
    public R save(@RequestBody SzSuggest szSuggest) {
        return R.ok(szSuggestService.save(szSuggest));
    }

    /**
     * 修改村民需求
     * @param szSuggest 村民需求
     * @return R
     */
    @ApiOperation(value = "修改村民需求", notes = "修改村民需求")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szpropose_edit')" )
    public R updateById(@RequestBody SzSuggest szSuggest) {
        return R.ok(szSuggestService.updateById(szSuggest));
    }

    /**
     * 通过id删除村民需求
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除村民需求", notes = "通过id删除村民需求")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szpropose_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szSuggestService.removeById(id));
    }

}
