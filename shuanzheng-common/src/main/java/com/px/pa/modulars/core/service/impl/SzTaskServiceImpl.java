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
package com.px.pa.modulars.core.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.constants.OrgTypeEnum;
import com.px.pa.modulars.core.entity.SzTask;
import com.px.pa.modulars.core.entity.SzTaskRecord;
import com.px.pa.modulars.core.mapper.SzTaskMapper;
import com.px.pa.modulars.core.service.SzTaskRecordService;
import com.px.pa.modulars.core.service.SzTaskService;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.entity.SysUserDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.modulars.upms.service.SysUserDeptService;
import com.px.pa.utils.bean.BaseQueryToPageUtil;
import com.px.pa.utils.bean.BeanToResultUtil;
import com.px.pa.modulars.vo.TaskQueryParam;
import com.px.pa.modulars.vo.result.SzRecordSimpleResult;
import com.px.pa.modulars.vo.result.SzTaskDetailResult;
import com.px.pa.modulars.vo.result.SzTaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 积分任务
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Service
@Transactional
public class SzTaskServiceImpl extends ServiceImpl<SzTaskMapper, SzTask> implements SzTaskService {
    @Autowired
    private SzTaskRecordService recordService;
    @Autowired
    private SysUserDeptService userDeptService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SzUserService userService;
    @Value("${application.task.record-len}")
    private Integer recordLen;


    @Override
    public List<SzTask> queryByCompletionStatusAndDept(Integer dept, Integer status) {
        return null;
    }

    @Override
    public Page<SzTaskResult> queryByUser(Integer user, TaskQueryParam param) {
        Page<SzTaskResult> page = BaseQueryToPageUtil.createPage(param);
        QueryWrapper query = new QueryWrapper();

        Integer trUid;//完成任务记录中的用户信息

        if(param.getArea().equals(OrgTypeEnum.VILLAGE.getValue())) {
            SysUserDept dept= userDeptService.lambdaQuery()
                    .eq(SysUserDept::getUserId, user)
                    .last("limit 1")
                    .one();
            trUid = dept.getChildId()==null?dept.getDeptId():dept.getChildId();
        }else{
            trUid=param.getUid();
        }
        query.eq(param.getStatus() != null && param.getStatus() == 1, "tr.uid", trUid);
        if(param.getUid()!=null){
            query.eq("tr.uid", trUid);
        }
        if(param.getDeduct()!=null && param.getDeduct()==1){
            query.eq("st.type",0);//扣分
            query.eq("st.state",1);//进行中
        }
        query.eq(param.getCate() != null && param.getCate() != 0, "st.cid", param.getCate());
        query.eq(param.getCcate() != null && param.getCcate() != 0, "st.ccid", param.getCcate());
        query.eq("st.area", param.getArea());


        if(param.getArea().equals(OrgTypeEnum.VILLAGE.getValue())){

//            query.eq("tr.uid", trUid);
        }
        query.like(StrUtil.isNotBlank(param.getKw()), "st.name", param.getKw());
        LocalDateTime now = LocalDateTime.now();
        query.lt("st.stime", now);
        query.gt(param.getType() != null && param.getType() == 1, "st.etime", now);
        query.lt(param.getType() != null && param.getType() == 2, "st.etime", now);
        query.eq("st.del_flag",0);
        query.orderByDesc("tr.uid", "st.sort");
        query.groupBy("st.id");
        page = super.baseMapper.queryByUser(page, user,trUid, query);
        page.getRecords().forEach(item -> {
            Map<String, Object> info = this.countNumAndScoreByTask(item);
            item.setSumNum(Integer.parseInt(info.get("num") == null ? "0" : info.get("num").toString()));
            item.setSumPoints(Integer.parseInt(info.get("score") == null ? "0" : info.get("score").toString()));
            if(user!=null){
                SzTask t=new SzTask();
                t.setPeriod(item.getPeriod());
                t.setId(item.getId());
                item.setCurrNum(checkNum(user,t,item.getArea()));
            }
        });
        return page;
    }

    /**
     * 查询某个任务的完成
     *
     * @param taskInfo
     * @return
     */
    private Map<String, Object> countNumAndScoreByTask(SzTaskResult taskInfo) {
        LocalDateTime now = LocalDateTime.now();
        String beginTime = null;
        String endTime = null;
        switch (taskInfo.getPeriod()) {
            case 1:
                beginTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                endTime = now.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                break;
            case 2:
                now = now.minusDays(now.getDayOfWeek().getValue());
                beginTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                endTime = now.plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                break;
            case 3:
                now = now.minusDays(now.getDayOfMonth());
                beginTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                endTime = now.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                break;
            case 4:
                now = now.minusDays(now.getDayOfMonth());
                now = now.minusMonths(now.getMonthValue() % 3 - 1);
                beginTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-01";
                endTime = now.plusMonths(3).format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-01";
                break;
            case 5:
                beginTime = now.getYear() + "-01-01 00:00:00";
                endTime = (now.getYear() + 1) + "-01-01 00:00:00";
                break;
        }

        Map<String, Object> info = this.baseMapper.countNumAndScoreByTask(taskInfo.getId(), taskInfo.getType()==1?2:-1);
        return info;
    }

    @Override
    public SzTaskDetailResult readDetail(Integer userId, Integer taskId,Integer uid) {
        SzTaskDetailResult detail = new SzTaskDetailResult();
        SzTask task = super.getById(taskId);
        detail.setTask(task);
        Page<SzTaskRecord> records = this.recordService.lambdaQuery()
                .eq(uid!=null,SzTaskRecord::getUid, uid)
                .eq(SzTaskRecord::getTid, taskId)
                .ne(task.getType()==1,SzTaskRecord::getState, SzTaskRecord.STATUS_WTG)
                .orderByDesc(SzTaskRecord::getUpdateTime, SzTaskRecord::getCreateTime)
                .page(new Page(1,this.recordLen));
        List<SzRecordSimpleResult> recordSimpleResults = BeanToResultUtil.beanToResult(records.getRecords(), SzRecordSimpleResult.class);
//        recordSimpleResults.forEach(item -> {
//            SzUser user = this.userService.getById(item.getUid());
//            if (user != null) {
//                item.setUname(user.getName());
//                item.setUphoto(user.getPhone());
//            }
//        });
        this.recordService.packResult(recordSimpleResults);
        detail.setRecords(recordSimpleResults);
        detail.setRecordsNum(records.getTotal());

        LocalDateTime stime = this.getStartTime(task);
        SzTaskRecord myRecord = this.recordService.lambdaQuery().eq(SzTaskRecord::getUid, userId)
                .eq(SzTaskRecord::getTid, taskId)
                .eq(SzTaskRecord::getState, SzTaskRecord.STATUS_TG)
//                .in(SzTaskRecord::getState, SzTaskRecord.STATUS_DSH, SzTaskRecord.STATUS_TG)
                .gt(SzTaskRecord::getCreateTime, stime)
                .orderByDesc(SzTaskRecord::getCreateTime).last(" limit 0,1")
                .one();
        SzRecordSimpleResult myRecordResult = BeanToResultUtil.beanToResult(myRecord, SzRecordSimpleResult.class);
        detail.setMyRecord(myRecordResult);
        detail.setCurrNum(checkNum(userId,task,task.getArea()));
        return detail;
    }

    private LocalDateTime getStartTime(SzTask task) {

        LocalDateTime stime = null;
//        周期：1-日，2-周，3-月，4-季，5-年
        LocalDateTime now = LocalDateTime.now();
        switch (task.getPeriod()) {
            case 1:
                stime = now.plusDays(-1);
                break;
            case 2:
                stime = now.plusWeeks(-1);
                break;
            case 3:
                stime = now.plusMonths(-1);
                break;
            case 4:
                stime = now.plusMonths(-3 + (now.getMonthValue() % 3 - 1));
                break;
            case 5:
                stime = now.plusYears(-1);
                break;
        }
        return stime;
    }

    public List<DateTime> getTaskDate(Integer period){
        List<DateTime> date=new ArrayList();
//        周期：1-日，2-周，3-月，4-季，5-年
        Date d=new Date();
        switch (period){
            case 1:
                date.add(DateUtil.beginOfDay(d));
                date.add(DateUtil.endOfDay(d));
                break;
            case 2:
                date.add(DateUtil.beginOfWeek(d));
                date.add(DateUtil.endOfWeek(d));
                break;
            case 3:
                date.add(DateUtil.beginOfMonth(d));
                date.add(DateUtil.endOfMonth(d));
                break;
            case 4:
                date.add(DateUtil.beginOfQuarter(d));
                date.add(DateUtil.endOfQuarter(d));
                break;
            case 5:
                date.add(DateUtil.beginOfYear(d));
                date.add(DateUtil.endOfYear(d));
                break;
        }
        return date;
    }

    public Integer checkNum(Integer uid,SzTask task,Integer area){
        List<DateTime> date=getTaskDate(task.getPeriod());
        return recordService.lambdaQuery()
                .eq(SzTaskRecord::getUid,uid)
                .eq(SzTaskRecord::getTid,task.getId())
                .eq(SzTaskRecord::getArea,area)
                .between(SzTaskRecord::getCreateTime,date.get(0),date.get(1))
                .ne(SzTaskRecord::getState,2)
                .count();
    }
}
