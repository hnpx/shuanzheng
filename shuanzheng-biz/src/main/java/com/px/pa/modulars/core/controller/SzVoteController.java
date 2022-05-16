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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.security.annotation.Inner;
import com.px.pa.constants.DelFlagEnum;
import com.px.pa.modulars.core.entity.SzShopGoods;
import com.px.pa.modulars.core.entity.SzVote;
import com.px.pa.modulars.core.entity.SzVoteItem;
import com.px.pa.modulars.core.service.SzVoteItemService;
import com.px.pa.modulars.core.service.SzVoteService;
import com.px.pa.modulars.core.vo.ObjectVo;
import com.px.pa.modulars.core.vo.SzVoteItemVo;
import com.px.pa.utils.ExcelUtil.ExcelBaseUtil;
import org.jpedal.parser.shape.S;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 投票信息
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szvote")
@Api(value = "szvote", tags = "投票信息管理")
public class SzVoteController {

    private final SzVoteService szVoteService;
    private final SzVoteItemService szVoteItemService;

    /**
     * 分页查询
     *
     * @param page   分页对象
     * @param szVote 投票信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('core_szvote_get')")
    public R getSzVotePage(Page page, SzVote szVote) {
        return R.ok(szVoteService.lambdaQuery()
                .eq(szVote.getState() != null, SzVote::getState, szVote.getState())
                .eq(szVote.getArea() != null, SzVote::getArea, szVote.getArea())
                .eq(szVote.getType() != null, SzVote::getType, szVote.getType())
                .lt(szVote.getTypeTime() != null && szVote.getTypeTime() == 0, SzVote::getStime, LocalDateTime.now())
                .gt(szVote.getTypeTime() != null && szVote.getTypeTime() == 0, SzVote::getEtime, LocalDateTime.now())
                .lt(szVote.getTypeTime() != null && szVote.getTypeTime() == 1, SzVote::getEtime, LocalDateTime.now())
                .like(StrUtil.isNotEmpty(szVote.getName()), SzVote::getName, szVote.getName())
                .eq(SzVote::getDelFlag, DelFlagEnum.DEL_FLAG_ENUM_TWO.getValue().toString())
                .orderByDesc(SzVote::getNum)
                .orderByDesc(SzVote::getCreateTime)
                .page(page));
    }


    /**
     * 通过id查询投票信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('core_szvote_get')")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(szVoteService.getById(id));
    }

    /**
     * 新增投票信息
     *
     * @param szVote 投票信息
     * @return R
     */
    @ApiOperation(value = "新增投票信息", notes = "新增投票信息")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_szvote_add')")
    public R save(@RequestBody SzVote szVote) {
        System.out.println(szVote);
        return R.ok(szVoteService.save(szVote));
    }

    /**
     * 修改投票信息
     *
     * @param szVote 投票信息
     * @return R
     */
    @ApiOperation(value = "修改投票信息", notes = "修改投票信息")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_szvote_edit')")
    public R updateById(@RequestBody SzVote szVote) {


        return R.ok(szVoteService.updateById(szVote));
    }

    /**
     * 通过id删除投票信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除投票信息", notes = "通过id删除投票信息")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('core_szvote_del')")
    public R removeById(@PathVariable Integer id) {
        SzVote szVote = szVoteService.getById(id);
        szVote.setDelFlag(DelFlagEnum.DEL_FLAG_ENUM_ONE.getValue().toString());
        return R.ok(szVoteService.updateById(szVote));
    }


    /**
     * 投票选项接口（村庄，村民）
     */

    @ApiOperation(value = "投票选项接口（村庄，村民）", notes = "投票选项接口（村庄，村民）")
    @GetMapping("/vote/object")
    @Inner(value = false)
    public R getVoteObject(@RequestParam("voteId") Integer voteId, @RequestParam(required = false) String name) {
        List<ObjectVo> objectVoList = szVoteService.getObjectVo(voteId, name);
        return R.ok(objectVoList);
    }

    /**
     * 投票排行导出
     */
    @ApiOperation(value = "投票排行导出", notes = "投票排行导出")
    @GetMapping("/voteItem/excel")
    @Inner(value = false)
    public void getExcel(@RequestParam("vid") Integer vid, HttpServletResponse response, HttpServletRequest request) {
        SzVote szVote = szVoteService.getById(vid);
       /*List<SzVoteItem> szVoteItemList = szVoteItemService.list(new QueryWrapper<SzVoteItem>().eq("vid",vid).orderByDesc("num"));
       List<SzVoteItemVo> szVoteItemVoList = new ArrayList<>();
        szVoteItemList.forEach(szVoteItem -> {
            SzVoteItemVo szVoteItemVo = new SzVoteItemVo();
            BeanUtil.copyProperties(szVoteItem,szVoteItemVo,true);
            szVoteItemVoList.add(szVoteItemVo);
        });*/
        ExcelBaseUtil.exportExcel(szVoteItemService.listRank(vid), "<<" + szVote.getName() + ">>" + "投票记录", "投票记录", SzVoteItemVo.class, "投票记录.xls", response);
    }

}
