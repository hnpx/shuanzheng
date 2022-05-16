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
import com.px.basic.alone.security.annotation.Inner;
import com.px.pa.modulars.core.Enum.ObjectTypeEnum;
import com.px.pa.modulars.core.entity.*;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.core.service.SzVoteItemService;
import com.px.pa.modulars.core.service.SzVotePointsService;
import com.px.pa.modulars.core.service.SzVoteService;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import org.apache.commons.lang3.StringUtils;
import org.jpedal.parser.gs.Q;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.util.Date;


/**
 * 投票选项
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szvoteitem")
@Api(value = "szvoteitem", tags = "投票选项管理")
public class SzVoteItemController {

    private final SzVoteItemService szVoteItemService;
    private final SzUserService szUserService;
    private final SysDeptService sysDeptService;
    private final SzVoteService szVoteService;
    private final SzVotePointsService szVotePointsService;


    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param szVoteItem 投票选项
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('core_szvoteitem_get')")
    public R getSzVoteItemPage(Page page, SzVoteItem szVoteItem) {

        Page<SzVoteItem> szVoteItemPage = szVoteItemService.lambdaQuery()
                .like(StringUtils.isNotEmpty(szVoteItem.getName()), SzVoteItem::getName, szVoteItem.getName())
                .like(StringUtils.isNotEmpty(szVoteItem.getDname()),SzVoteItem::getDname,szVoteItem.getDname())
                .eq(szVoteItem.getDid() != null, SzVoteItem::getDid, szVoteItem.getDid())
                .eq(SzVoteItem::getVid, szVoteItem.getVid())
                .orderByAsc(SzVoteItem::getNumber)
                .orderByAsc(SzVoteItem::getSort)
                .orderByDesc(SzVoteItem::getCreateTime)
                .page(page);
        szVoteItemPage.getRecords().forEach(szVoteItem1 -> {
            SzVote szVote = szVoteService.getById(szVoteItem1.getVid());
            if (szVote.getType().equals(ObjectTypeEnum.ObjectTypeEnumONE.getValue())) {
                SzUser szUser = szUserService.getById(szVoteItem1.getDid());
                szVoteItem1.setDname(szUser.getName());
            } else if (szVote.getType().equals(ObjectTypeEnum.ObjectTypeEnumTWO.getValue())) {
                SysDept sysDept = sysDeptService.getById(szVoteItem1.getDid());
                szVoteItem1.setDname(sysDept.getName());
            }

        });
        return R.ok(szVoteItemPage);
    }


    /**
     * 通过id查询投票选项
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('core_szvoteitem_get')")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(szVoteItemService.getById(id));
    }

    /**
     * 新增投票选项
     *
     * @param szVoteItem 投票选项
     * @return R
     */
    @ApiOperation(value = "新增投票选项", notes = "新增投票选项")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szvoteitem_add')")
    public R save(@RequestBody SzVoteItem szVoteItem) {

        //判断编号是否重复
        if(StringUtils.isNotEmpty(szVoteItem.getNumber())){
            if (szVoteItem.getNumber().length() == 1){
                szVoteItem.setNumber("0"+szVoteItem.getNumber());
            }
        }
        SzVoteItem szVoteItem2 = szVoteItemService.getOne(new QueryWrapper<SzVoteItem>().eq("number",szVoteItem.getNumber()).eq("vid",szVoteItem.getVid()));
        if(szVoteItem2 != null){
            return R.failed("此编号重复，不能被重复添加");
        }
        //判断选择对象是否已经被添加
        SzVote szVote = szVoteService.getById(szVoteItem.getVid());
        if(szVote.getType() == 1){  //村民
           SzUser szUser = szUserService.getById(szVoteItem.getDid());
           if(szUser != null){
               szVoteItem.setDname(szUser.getName());
           }
        }else if(szVote.getType() == 2) { //村庄
         SysDept sysDept = sysDeptService.getById(szVoteItem.getDid());
         if(sysDept != null){
             szVoteItem.setDname(sysDept.getName());
         }
        }
        if (szVote != null) {
            SzVoteItem szVoteItem1 = szVoteItemService.getOne(new QueryWrapper<SzVoteItem>().eq("vid", szVoteItem.getVid())
                    .eq("did", szVoteItem.getDid()));
            if (szVoteItem1 != null) {
                return R.failed("此对象已被添加为选项，不能被重复添加");
            }

        }
        return R.ok(szVoteItemService.save(szVoteItem));
    }

    /**
     * 修改投票选项
     *
     * @param szVoteItem 投票选项
     * @return R
     */
    @ApiOperation(value = "修改投票选项", notes = "修改投票选项")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szvoteitem_edit')")
    public R updateById(@RequestBody SzVoteItem szVoteItem) {

        //判断选择对象是否已经被添加
        SzVote szVote = szVoteService.getById(szVoteItem.getVid());
        if (szVote != null) {
            SzVoteItem szVoteItem1 = szVoteItemService.getById(szVoteItem.getId());
            if(szVote.getType() == 1){  //村民
                SzUser szUser = szUserService.getById(szVoteItem.getDid());
                if(szUser != null){
                    szVoteItem.setDname(szUser.getName());
                }
            }else if(szVote.getType() == 2) { //村庄
                SysDept sysDept = sysDeptService.getById(szVoteItem.getDid());
                if(sysDept != null){
                    szVoteItem.setDname(sysDept.getName());
                }
            }
            if (!szVoteItem1.getDid().equals(szVoteItem.getDid())) {
                SzVoteItem szVoteItem2 = szVoteItemService.getOne(new QueryWrapper<SzVoteItem>().eq("vid", szVoteItem.getVid())
                        .eq("did", szVoteItem.getDid()));
                if (szVoteItem2 != null) {
                    return R.failed("此对象已被添加为选项，不能被重复添加");
                }
            }


        }
        return R.ok(szVoteItemService.updateById(szVoteItem));
    }

    /**
     * 通过id删除投票选项
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除投票选项", notes = "通过id删除投票选项")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('core_szvoteitem_del')")
    public R removeById(@PathVariable Integer id) {
        return R.ok(szVoteItemService.removeById(id));
    }



    /**
     * 投票排行积分发放
     * @return R
     */
    @ApiOperation(value = "投票排行积分发放", notes = "投票排行积分发放")
    @GetMapping("/issue")
    @Inner(value = false)
    public R getissue(@RequestParam("vid") Integer vid){
        SzVote szVote = szVoteService.getById(vid);
       int count = szVotePointsService.lambdaQuery().eq(SzVotePoints::getVid,vid).count();
       if(count==0){
           return R.failed("投票活动没有积分规则不能进行积分发放");
       }
        Date endDate = Date.from(szVote.getEtime().toInstant(ZoneOffset.of("+8")));
        Date date = new Date();
        if(endDate.getTime() > date.getTime()){
            return R.failed("投票活动未结束不能进行积分发放");
        }else {
            if(szVote.getIntegralStatus()==1){
                return R.failed("投票活动积分已经发放不能再次发放");
            }else {
                szVoteService.issue(vid);
            }

        }
        return R.ok();

    }



}
