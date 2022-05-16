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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.modulars.core.entity.SzGuideRecord;
import com.px.pa.modulars.core.entity.SzGuideUser;
import com.px.pa.modulars.core.mapper.SzGuideRecordMapper;
import com.px.pa.modulars.core.service.SzGuideRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 办事指南申请记录
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:19
 */
@Service
@Transactional
public class SzGuideRecordServiceImpl extends ServiceImpl<SzGuideRecordMapper, SzGuideRecord> implements SzGuideRecordService {

    @Override
    public Page<SzGuideRecord> getPage(Page page, SzGuideRecord szGuideRecord) {
        Integer i1 = szGuideRecord.getState(), i2 = szGuideRecord.getState();
        if (szGuideRecord.getState() != null) {
            if(szGuideRecord.getState() == 4){
                i1 = 0;
                i2 = 1;
            }else if(szGuideRecord.getState() == 5){
                i1 = 2;
                i2 = 3;
            }
        }
        List<Integer> status=new ArrayList<>();
        status.add(i1);
        status.add(i2);
        QueryWrapper query = new QueryWrapper();
        query.like(StrUtil.isNotEmpty(szGuideRecord.getGname()), "b.name", szGuideRecord.getGname());
        query.like(StrUtil.isNotEmpty(szGuideRecord.getUname()), "c.name", szGuideRecord.getUname());
        query.like(StrUtil.isNotEmpty(szGuideRecord.getCode()), "a.code", szGuideRecord.getCode());
        query.eq("a.del_flag","0");
        query.in(szGuideRecord.getState() != null, "a.state", status);
        query.eq(szGuideRecord.getUid() != null, "a.uid", szGuideRecord.getUid());
        query.orderByDesc("a.id");
        return super.baseMapper.getPage(page, query);
    }

    @Override
    public Page<SzGuideRecord> getPage(Page page, Integer state, Integer uid) {
        QueryWrapper query = new QueryWrapper();
        query.eq("f.uid", uid);
        query.eq(state!=null,"f.state", state);
        query.orderByDesc("a.id");
        return super.baseMapper.getPageBySh(page, query);
    }

    @Override
    public List<SzGuideUser> getNextUser(QueryWrapper query) {
        return super.baseMapper.getNextUser(query);
    }
}
