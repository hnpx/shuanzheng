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
import com.px.pa.modulars.core.Enum.ObjectTypeEnum;
import com.px.pa.modulars.core.entity.*;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.core.service.SzVoteItemService;
import com.px.pa.modulars.core.service.SzVoteLogService;
import com.px.pa.modulars.core.service.SzVoteService;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 投票记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szvotelog" )
@Api(value = "szvotelog", tags = "投票记录管理")
public class SzVoteLogController {

    private final  SzVoteLogService szVoteLogService;
    private final SzVoteService szVoteService;
    private final SzUserService szUserService;
    private final SysDeptService sysDeptService;
    private final SzVoteItemService szVoteItemService;


    /**
     * 分页查询
     * @param page 分页对象
     * @param szVoteLog 投票记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_szvotelog_get')" )
    public R getSzVoteLogPage(Page page, SzVoteLog szVoteLog) {
    /*Page<SzVoteLog> szVoteLogPage =  szVoteLogService.lambdaQuery()
                .eq(szVoteLog.getVid()!= null,SzVoteLog::getVid, szVoteLog.getVid())
                 .eq(szVoteLog.getViid()!= null,SzVoteLog::getViid,szVoteLog.getViid())
                .orderByDesc(SzVoteLog::getCreateTime)
                .page(page);*/
        Page<SzVoteLog> szVoteLogPage = szVoteLogService.getListPage(page,szVoteLog.getVoteName(),szVoteLog.getVoteItem());
         szVoteLogPage.getRecords().forEach(szVoteLog1 -> {
         SzVote szVote = szVoteService.getById(szVoteLog1.getVid());
         SzVoteItem szVoteItem = szVoteItemService.getById(szVoteLog1.getViid());
         if(szVoteItem != null){
             if(szVote.getType().equals(ObjectTypeEnum.ObjectTypeEnumONE.getValue())){
                 SzUser szUser = szUserService.getById(szVoteItem.getDid());
                 if(szUser != null){
                     szVoteLog1.setVoteItem(szUser.getName());
                 }
             }else {
                 SysDept sysDept = sysDeptService.getById(szVoteItem.getDid());
                 if(sysDept != null){
                     szVoteLog1.setVoteItem(sysDept.getName());
                 }
             }
         }
           if(szVote != null){
               szVoteLog1.setVoteName(szVote.getName());
           }
        });
        return R.ok(szVoteLogPage);
    }


    /**
     * 通过id查询投票记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szvotelog_get')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(szVoteLogService.getById(id));
    }

    /**
     * 新增投票记录
     * @param szVoteLog 投票记录
     * @return R
     */
    @ApiOperation(value = "新增投票记录", notes = "新增投票记录")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szvotelog_add')" )
    public R save(@RequestBody SzVoteLog szVoteLog) {
        return R.ok(szVoteLogService.save(szVoteLog));
    }

    /**
     * 修改投票记录
     * @param szVoteLog 投票记录
     * @return R
     */
    @ApiOperation(value = "修改投票记录", notes = "修改投票记录")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szvotelog_edit')" )
    public R updateById(@RequestBody SzVoteLog szVoteLog) {
        return R.ok(szVoteLogService.updateById(szVoteLog));
    }

    /**
     * 通过id删除投票记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除投票记录", notes = "通过id删除投票记录")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_szvotelog_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(szVoteLogService.removeById(id));
    }

    /**
     *
     * 投票记录
     *
     */
    @ApiOperation(value = "投票记录", notes = "投票记录")
    @GetMapping("/record/vote" )

    public R getRecord(@RequestParam("vid") Integer vid,@RequestParam("viid") Integer viid,Page page){

      Page<SzVoteLog> szVoteLogPage =  szVoteLogService.lambdaQuery().eq(SzVoteLog::getVid,vid).eq(SzVoteLog::getViid,viid).page(page);
      return R.ok(szVoteLogPage);
    }

}
