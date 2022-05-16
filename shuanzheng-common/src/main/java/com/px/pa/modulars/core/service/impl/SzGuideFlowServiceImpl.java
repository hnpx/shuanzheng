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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.constants.FlowStateEnum;
import com.px.pa.modulars.core.entity.SzGuideFlow;
import com.px.pa.modulars.core.entity.SzGuideRecord;
import com.px.pa.modulars.core.entity.SzGuideUser;
import com.px.pa.modulars.core.mapper.SzGuideFlowMapper;
import com.px.pa.modulars.core.service.SzGuideFlowService;
import com.px.pa.modulars.core.service.SzGuideRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 办事指南审核记录
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:16
 */
@Service
@Transactional
public class SzGuideFlowServiceImpl extends ServiceImpl<SzGuideFlowMapper, SzGuideFlow> implements SzGuideFlowService {

    @Autowired
    public SzGuideRecordService recordService;

    @Override
    public List<SzGuideFlow> flowList(SzGuideFlow szGuideFlow) {
        QueryWrapper query=new QueryWrapper();
        query.eq("a.rid", szGuideFlow.getRid());
        query.orderByDesc("a.id");
        return super.baseMapper.flowList(query);
    }

    @Override
    public boolean sh(SzGuideFlow szGuideFlow) {
        SzGuideFlow old=super.lambdaQuery()
                .eq(SzGuideFlow::getRid, szGuideFlow.getRid())
                .eq(SzGuideFlow::getUid,szGuideFlow.getUid())
                .orderByDesc(SzGuideFlow::getId)
                .last("limit 1")
                .one();
        if(old!=null){
            szGuideFlow.setId(old.getId());
        }
        szGuideFlow.setUtime(LocalDateTime.now());
        super.saveOrUpdate(szGuideFlow);
        if(szGuideFlow.getState().equals(FlowStateEnum.REJECT.getValue())){
            recordService.lambdaUpdate()
                    .eq(SzGuideRecord::getId,szGuideFlow.getRid())
                    .set(SzGuideRecord::getState,FlowStateEnum.REJECT.getValue())
                    .update();
        }else{
            QueryWrapper query=new QueryWrapper();
            query.eq("a.id", szGuideFlow.getRid());
            List<SzGuideUser> users=recordService.getNextUser(query);
            int c=super.lambdaQuery().eq(SzGuideFlow::getRid,szGuideFlow.getRid()).count()-1;
            int t=0;
            for (int i = c; i < users.size(); i++) {
                if(users.get(i).getUid().equals(szGuideFlow.getUid())){
                    t=i+1;
                    break;
                }
            }
            if(t>=users.size()){
                recordService.lambdaUpdate()
                        .eq(SzGuideRecord::getId,szGuideFlow.getRid())
                        .set(SzGuideRecord::getState,FlowStateEnum.CONFIRM.getValue())
                        .update();
            }else{
                recordService.lambdaUpdate()
                        .eq(SzGuideRecord::getId,szGuideFlow.getRid())
                        .set(SzGuideRecord::getState,FlowStateEnum.PROCESS.getValue())
                        .update();
                Integer uid=users.get(t).getUid();
                SzGuideFlow flow=new SzGuideFlow();
                flow.setUid(uid);
                flow.setRid(szGuideFlow.getRid());
                super.save(flow);
            }
        }
        return true;
    }
}
