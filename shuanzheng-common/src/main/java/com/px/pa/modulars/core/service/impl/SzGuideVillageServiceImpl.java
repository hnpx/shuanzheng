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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.modulars.core.entity.SzGuideUser;
import com.px.pa.modulars.core.entity.SzGuideVillage;
import com.px.pa.modulars.core.mapper.SzGuideVillageMapper;
import com.px.pa.modulars.core.service.SzGuideUserService;
import com.px.pa.modulars.core.service.SzGuideVillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 办事指南所属村列表
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:17
 */
@Service
@Transactional
public class SzGuideVillageServiceImpl extends ServiceImpl<SzGuideVillageMapper, SzGuideVillage> implements SzGuideVillageService {

    @Autowired
    private SzGuideUserService guideUserService;

    @Override
    public boolean saveVillage(SzGuideVillage v) {
        if(v.getId()!=null){
            deleteVillage(v.getId());
        }else{
            SzGuideVillage obj=super.lambdaQuery().eq(SzGuideVillage::getDid,v.getDid()).one();
            if(obj!=null){
                deleteVillage(obj.getId());
            }
        }
        super.saveOrUpdate(v);
        v.getUid().forEach(i->{
            SzGuideUser u=new SzGuideUser();
            u.setVid(v.getId());
            u.setUid(i);
            guideUserService.save(u);
        });
        return true;
    }

    @Override
    public boolean deleteVillage(Integer id) {
        super.removeById(id);
        Map<String,Object> map=new HashMap();
        map.put("vid", id);
        guideUserService.getBaseMapper().deleteByMap(map);
        return true;
    }

    @Override
    public Page<SzGuideVillage> getPage(Page page, SzGuideVillage szGuideVillage) {
        QueryWrapper query=new QueryWrapper();
        query.eq(szGuideVillage.getGid()!=null, "a.gid", szGuideVillage.getGid());
        query.orderByDesc("a.id");
        Page<SzGuideVillage> p=super.baseMapper.getPage(page,query);
        for(SzGuideVillage s:p.getRecords()){
            QueryWrapper q=new QueryWrapper();
            q.eq("a.vid",s.getId());
            q.orderByAsc("a.id");
            s.setAudit(guideUserService.getUser(q));
        }
        return p;
    }

}
