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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.pa.constants.OrgTypeEnum;
import com.px.pa.constants.PointsTypeEnum;
import com.px.pa.modulars.core.entity.SzPointsLog;
import com.px.pa.modulars.core.entity.SzTask;
import com.px.pa.modulars.core.entity.SzTaskRecord;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.mapper.SzTaskRecordMapper;
import com.px.pa.modulars.core.service.SzPointsLogService;
import com.px.pa.modulars.core.service.SzTaskRecordService;
import com.px.pa.modulars.core.service.SzTaskService;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.modulars.vo.DeductTaskQueryParam;
import com.px.pa.modulars.vo.TaskRecordParam;
import com.px.pa.modulars.vo.TaskRecordQueryParam;
import com.px.pa.modulars.vo.result.SzRecordSimpleResult;
import com.px.pa.utils.bean.BaseQueryToPageUtil;
import com.px.pa.utils.bean.BeanToResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务完成记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Service
@Transactional
public class SzTaskRecordServiceImpl extends ServiceImpl<SzTaskRecordMapper, SzTaskRecord> implements SzTaskRecordService {

    @Autowired
    private SzTaskService taskService;
    @Autowired
    private SzPointsLogService pointsLogService;
    @Autowired
    private SzUserService userService;
    @Autowired
    private SysDeptService deptService;

    @Override
    public boolean saveCompanyTask(SzTaskRecord record) {
        if (record.getUid() != null && record.getTid() != null && record.getPoints() > 0) {
            SzTask task = taskService.getById(record.getTid());
            if (task.getPoints() >= record.getPoints()) {
                if (task.getType() == 0) {
                    record.setPoints(-record.getPoints());
                }
                Integer uid = SecurityUtils.getUser().getId();
                record.setArea(task.getArea());
                record.setHelp(uid);
                record.setPointsUid(uid);
                record.setPointsTime(LocalDateTime.now());
                if (task.getArea().equals(OrgTypeEnum.COMPANY.getValue())) {
                    record.setState(1);
                    record.setShUid(uid);
                    record.setShTime(LocalDateTime.now());
                }
                if (super.save(record)&&task.getArea().equals(OrgTypeEnum.COMPANY.getValue())) {
                    pointsLogService.saveLog(initLog(record, OrgTypeEnum.COMPANY.getValue(), task.getType()));
                }
            }
        }
        return false;
    }

    public SzPointsLog initLog(SzTaskRecord record, Integer type, Integer optype) {
        SzPointsLog log = new SzPointsLog();
        log.setIds(record.getUid() + "");
        log.setType(type);
        log.setPoints(Math.abs(record.getPoints()));
        log.setOperator(optype);
        log.setOpType(PointsTypeEnum.TASK.getValue());
        log.setCreateInt(Math.toIntExact(System.currentTimeMillis() / 1000));
        log.setMsg(record.getMark());
        return log;
    }

    @Override
    public boolean shenhe(SzTaskRecord record) {
        if (record != null) {
            if (record.getState() == 1 || record.getState() == 2) {
                SzTaskRecord old = super.getById(record.getId());
                if (old.getState() == 0) {
                    if(record.getShUid()!=null){
                        record.setShXcx(1);
                    }else{
                        record.setShUid(SecurityUtils.getUser().getId());
                    }
                    record.setShTime(LocalDateTime.now());
                    if (super.updateById(record)) {
                        if ((record.getState() == 1&&old.getPoints()>0)||(record.getState()==2&&old.getPoints()<0)) {
                            SzTask task=taskService.getById(old.getTid());
                            Integer type = task.getType();
                            old.setType(type);
                            if(record.getState() == 1&&old.getPoints()>0&&task.getAllNum()!=0){
                                Integer count=super.lambdaQuery()
                                        .eq(SzTaskRecord::getTid,old.getTid())
                                        .eq(SzTaskRecord::getState,1)
                                        .count();
                                if(count>=task.getAllNum()){
                                    taskService.lambdaUpdate()
                                            .eq(SzTask::getId,old.getTid())
                                            .set(SzTask::getState,2)
                                            .set(SzTask::getMsg,"任务完成次数已达上限，提前结束")
                                            .update();
                                }
                            }
                            if (old.getArea().equals(OrgTypeEnum.HOUSE.getValue())) {
                                return housePoints(old);
                            } else if (old.getArea().equals(OrgTypeEnum.VILLAGE.getValue())) {
                                return villagePoints(old);
                            } else if (old.getArea().equals(OrgTypeEnum.COMPANY.getValue())) {
                                return companyPoints(old);
                            }
                        }
                        return true;
                    }
                }
            } else {
                return super.updateById(record);
            }
        }
        return false;
    }

    public boolean housePoints(SzTaskRecord record) {
        return pointsLogService.saveLog(initLog(record, OrgTypeEnum.HOUSE.getValue(), record.getType()));
    }

    public boolean villagePoints(SzTaskRecord record) {
        return pointsLogService.saveLog(initLog(record, OrgTypeEnum.VILLAGE.getValue(), record.getType()));
    }

    public boolean companyPoints(SzTaskRecord record) {
        return pointsLogService.saveLog(initLog(record, OrgTypeEnum.COMPANY.getValue(), record.getType()));
    }

    @Override
    public Page<TaskRecordParam> getPage(Page page, TaskRecordParam szTaskRecord) {
        return super.baseMapper.getPage(page,
                getCompanyWrapper(szTaskRecord),
                getVillageWrapper(szTaskRecord),
                getHouseWrapper(szTaskRecord));
    }

    @Override
    public Page<SzRecordSimpleResult> queryToSimple(TaskRecordQueryParam param) {
        return this.queryByUserToSimple(null, param);
    }

    @Override
    public Page<SzRecordSimpleResult> queryBadList(Integer uid,BasePageParamVo param) {
        Page<SzTaskRecord> page = BaseQueryToPageUtil.createPage(param);
        QueryWrapper query=new QueryWrapper();
        query.eq("a.uid", uid);
        query.lt("a.points",0);
        query.eq("a.state",0);
        query.eq("a.area",OrgTypeEnum.HOUSE.getValue());
        return super.baseMapper.queryBadList(page,query);
    }

    @Override
    public Page<SzRecordSimpleResult> queryByUserToSimple(Integer userId, TaskRecordQueryParam param) {
        Page<SzTaskRecord> page = BaseQueryToPageUtil.createPage(param);
        Integer type=taskService.getById(param.getTid()).getType();
        page = super.lambdaQuery().eq(param.getTid() != null, SzTaskRecord::getTid, param.getTid())
                .eq(userId != null, SzTaskRecord::getUid, userId)
                .lt(param.getType() != null && param.getType() == 0, SzTaskRecord::getPoints, 0)
                .gt(param.getType() != null && param.getType() == 1, SzTaskRecord::getPoints, 0)
                .ne(type==1,SzTaskRecord::getState, SzTaskRecord.STATUS_WTG)
                .eq(param.getUid() != null, SzTaskRecord::getUid, param.getUid())
                .eq(SzTaskRecord::getArea,OrgTypeEnum.HOUSE.getValue())
                .orderByDesc(SzTaskRecord::getCreateTime, SzTaskRecord::getUpdateTime)
                .page(page);
        Page<SzRecordSimpleResult> pageResult = BeanToResultUtil.beanToResult(page, SzRecordSimpleResult.class);
        this.packResult(pageResult.getRecords());
        return pageResult;
    }

    @Override
    public List<SzRecordSimpleResult> packResult(List<SzRecordSimpleResult> results) {
        if(results.size()>0){
            results.forEach(item -> {
                SzUser user = this.userService.getById(item.getUid());
                if (user != null) {
                    item.setUname(user.getName());
                    item.setUphoto(user.getAvatar());
                    SysDept dept = this.deptService.getById(user.getDid());
                    if (dept != null) {
                        item.setDeptName(dept.getName());
                    }
                }
                item.setImage(taskService.getById(item.getTid()).getImage());
            });
        }

        return results;
    }

    @Override
    public Integer queryIsRectified(Integer id, Integer type) {
        Integer num=super.lambdaQuery()
                .eq(SzTaskRecord::getArea, type)
                .eq(SzTaskRecord::getUid, id)
                .lt(SzTaskRecord::getPoints, 0)
                .eq(SzTaskRecord::getState, 0)
                .count();
        return num;
    }

    @Override
    public  List<SzRecordSimpleResult> queryRectified(Integer id, Integer type) {
        List<SzTaskRecord> records=super.lambdaQuery()
                .eq(SzTaskRecord::getArea, type)
                .eq(SzTaskRecord::getUid, id)
                .lt(SzTaskRecord::getPoints, 0)
                .eq(SzTaskRecord::getState, 0)
                .orderByDesc(SzTaskRecord::getCreateTime)
                .list();
        List<SzRecordSimpleResult> pageResult = BeanToResultUtil.beanToResult(records, SzRecordSimpleResult.class);
        this.packResult(pageResult);
        return pageResult;
    }

    public QueryWrapper getCompanyWrapper(TaskRecordParam szTaskRecord) {
        QueryWrapper query = getBaseWrapper(szTaskRecord);
        query.last(" and a.area=" + OrgTypeEnum.COMPANY.getValue());
        return query;
    }

    public QueryWrapper getVillageWrapper(TaskRecordParam szTaskRecord) {
        QueryWrapper query = getBaseWrapper(szTaskRecord);
        query.last(" and a.area=" + OrgTypeEnum.VILLAGE.getValue());
        return query;
    }

    public QueryWrapper getHouseWrapper(TaskRecordParam szTaskRecord) {
        QueryWrapper query = getBaseWrapper(szTaskRecord);
        query.last(" and a.area=" + OrgTypeEnum.HOUSE.getValue());
        return query;
    }

    public QueryWrapper getBaseWrapper(TaskRecordParam szTaskRecord) {
        QueryWrapper query = new QueryWrapper();
        query.eq("1", 1);
        query.eq("a.del_flag", 0);
        query.like(StrUtil.isNotEmpty(szTaskRecord.getTname()), "b.name", szTaskRecord.getTname());
        query.like(StrUtil.isNotEmpty(szTaskRecord.getUname()), "c.name", szTaskRecord.getUname());
        query.like(StrUtil.isNotEmpty(szTaskRecord.getHname()), "d.name", szTaskRecord.getHname());
        query.like(StrUtil.isNotEmpty(szTaskRecord.getPname()), "e.name", szTaskRecord.getPname());
        query.like(StrUtil.isNotEmpty(szTaskRecord.getSname()), "f.name", szTaskRecord.getSname());
        query.gt(szTaskRecord.getStartTime() != null, "a.create_time", szTaskRecord.getStartTime());
        query.lt(szTaskRecord.getEndTime() != null, "a.create_time", szTaskRecord.getEndTime());
        query.eq(szTaskRecord.getState() != null, "a.state", szTaskRecord.getState());
        query.eq(szTaskRecord.getArea() != null, "a.area", szTaskRecord.getArea());
//        query.orderByDesc("a.create_time");
        return query;
    }

    @Override
    public Page<TaskRecordParam> deductList(DeductTaskQueryParam param) {
        Page<SzTaskRecord> page = BaseQueryToPageUtil.createPage(param);
        QueryWrapper query=new QueryWrapper();
        query.eq("a.area",OrgTypeEnum.HOUSE.getValue());
        query.lt("a.points",0);
        query.eq("b.type", 0);
        if(param.getId() != null){
            query.eq("a.id",param.getId());
        }
        //1-已回复，0-未回复
        if(param.getType()!=null){
            if(param.getType()==1){
                query.isNotNull("a.result");
            }else if(param.getType()==0){
                query.isNull("a.result");
            }
        }
        query.orderByDesc("a.create_time");
        return baseMapper.deductList(page,query);
    }

    @Override
    public Boolean deductResult(DeductTaskQueryParam param,Integer uid) {
        //1-清除，0-扣分
        SzTaskRecord r=new SzTaskRecord();
        r.setShUid(uid);
        r.setId(param.getId());
        r.setMsg(param.getMsg());
        if(param.getType()==1){
            r.setState(1);
        }else{
            r.setState(2);
        }
        shenhe(r);
        return true;
    }
}
