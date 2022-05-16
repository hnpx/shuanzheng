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

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.security.job.DelayQueueManager;
import com.px.basic.alone.security.job.TaskBase;
import com.px.pa.constants.OrgTypeEnum;
import com.px.pa.modulars.core.entity.SzTask;
import com.px.pa.modulars.core.service.SzTaskService;
import com.px.pa.modulars.core.task.PointsTask;
import com.px.pa.modulars.core.task.PointsTaskSign;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * 积分任务
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sztask" )
@Api(value = "sztask", tags = "积分任务管理")
@Service("szTaskController")
public class SzTaskController {

    private final SzTaskService szTaskService;
    private final DelayQueueManager job;

    /**
     * 分页查询
     * @param page 分页对象
     * @param szTask 积分任务
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('core_sztask_get')" )
    public R getSzTaskPage(Page page, SzTask szTask) {
        return R.ok(szTaskService.lambdaQuery()
                .eq(szTask.getState()!=null, SzTask::getState,szTask.getState())
                .eq(szTask.getType()!=null, SzTask::getType,szTask.getType())
                .eq(szTask.getArea()!=null, SzTask::getArea,szTask.getArea())
                .eq(szTask.getFinish()!=null, SzTask::getFinish,szTask.getFinish())
                .eq(szTask.getPeriod()!=null, SzTask::getPeriod,szTask.getPeriod())
                .eq(szTask.getCid()!=null, SzTask::getCid,szTask.getCid())
                .eq(szTask.getCcid()!=null, SzTask::getCcid,szTask.getCcid())
                .eq(SzTask::getDelFlag,0)
                .like(StrUtil.isNotEmpty(szTask.getName()),SzTask::getName,szTask.getName())
                .orderByDesc(SzTask::getCreateTime)
                .page(page));
    }

    @ApiOperation(value = "企业任务列表", notes = "企业任务列表")
    @GetMapping("/companylist" )
    @PreAuthorize("@pms.hasPermission('core_sztask_get')" )
    public R companyTaskList() {
        return R.ok(szTaskService.lambdaQuery()
                .select(SzTask::getId,SzTask::getName,SzTask::getPoints,SzTask::getFinish)
                .eq(SzTask::getArea, OrgTypeEnum.COMPANY.getValue())
                .eq(SzTask::getState,1)
                .orderByDesc(SzTask::getCreateTime)
                .list());
    }

    @ApiOperation(value = "村民任务列表", notes = "村民任务列表")
    @GetMapping("/userlist" )
    @PreAuthorize("@pms.hasPermission('core_sztask_get')" )
    public R userTaskList() {
        return R.ok(szTaskService.lambdaQuery()
                .select(SzTask::getId,SzTask::getName,SzTask::getPoints,SzTask::getFinish)
                .eq(SzTask::getArea, OrgTypeEnum.HOUSE.getValue())
                .eq(SzTask::getState,1)
                .orderByDesc(SzTask::getCreateTime)
                .list());
    }

    /**
     * 通过id查询积分任务
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_sztask_get')" )
    public R getById(@PathVariable("id" ) Integer id) {


        return R.ok(szTaskService.getById(id));
    }

    /**
     * 新增积分任务
     * @param szTask 积分任务
     * @return R
     */
    @ApiOperation(value = "新增积分任务", notes = "新增积分任务")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('core_sztask_add')" )
    public R save(@RequestBody SzTask szTask) {
        setState(szTask);
        szTaskService.save(szTask);
        addJob(szTask);
        return R.ok();
    }
    public void setState(SzTask szTask){
        szTask.setState(0);
        if(DateUtil.compare(Convert.toDate(szTask.getStime()),new Date())<=0){
            szTask.setState(1);
        }
        if(DateUtil.compare(Convert.toDate(szTask.getEtime()),new Date())<=0){
            szTask.setState(2);
        }
    }
    public void addJob(SzTask szTask){
        long start=DateUtil.betweenMs(Convert.toDate(szTask.getStime()),new Date());
        long end=DateUtil.betweenMs(new Date(),Convert.toDate(szTask.getEtime()));
        String s= PointsTaskSign.start_points_task +szTask.getId();
        String e=PointsTaskSign.end_points_task+szTask.getId();
        job.remove(s);
        job.remove(e);
        if(szTask.getState()==0){
            job.put(new PointsTask(new TaskBase(s),start));
            job.put(new PointsTask(new TaskBase(e),end));
        }else if(szTask.getState()==1){
            job.put(new PointsTask(new TaskBase(e),end));
        }
    }
    public void updateState(Integer id,Integer state,String msg){
        szTaskService.lambdaUpdate()
                .eq(SzTask::getId,id)
                .set(SzTask::getState,state)
                .set(StrUtil.isNotEmpty(msg),SzTask::getMsg,msg)
                .update();
    }
    /**
     * 修改积分任务
     * @param szTask 积分任务
     * @return R
     */
    @ApiOperation(value = "修改积分任务", notes = "修改积分任务")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('core_sztask_edit')" )
    public R updateById(@RequestBody SzTask szTask) {
        setState(szTask);
        addJob(szTask);
        return R.ok(szTaskService.updateById(szTask));
    }

    /**
     * 通过id删除积分任务
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除积分任务", notes = "通过id删除积分任务")
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('core_sztask_del')" )
    public R removeById(@PathVariable Integer id) {
        SzTask szTask =  szTaskService.getById(id);
        szTask.setDelFlag("1");
        szTaskService.saveOrUpdate(szTask);
        return R.ok();
    }

}
