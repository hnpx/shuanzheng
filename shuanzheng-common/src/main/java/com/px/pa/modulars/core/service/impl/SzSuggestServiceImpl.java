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

import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.modulars.core.entity.SzSuggest;
import com.px.pa.modulars.core.mapper.SzSuggestMapper;
import com.px.pa.modulars.core.service.SzSuggestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 村民需求
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Service
@Transactional
public class SzSuggestServiceImpl extends ServiceImpl<SzSuggestMapper, SzSuggest> implements SzSuggestService {

    @Override
    public Page<SzSuggest> getPage(Page page, SzSuggest param) {
        QueryWrapper query=new QueryWrapper();
        query.eq("a.type", param.getType());
        query.eq(param.getState()!=null,"a.state",param.getState());
        query.eq(param.getUid()!=null,"a.uid",param.getUid());
        query.eq(param.getId()!=null,"a.id",param.getId());
        query.like(StrUtil.isNotEmpty(param.getUname()), "b.name", param.getUname());
        query.like(StrUtil.isNotEmpty(param.getTitle()), "a.title", param.getTitle());
        query.orderByDesc("a.create_time");
        return super.baseMapper.getPage(page,query);
    }

    @Override
    public boolean deal(SzSuggest param) {
        if(param.getId() != null){
            return super.lambdaUpdate()
                    .eq(SzSuggest::getState,0)
                    .eq(SzSuggest::getId,param.getId())
                    .set(SzSuggest::getState,param.getState())
                    .set(SzSuggest::getMsg,param.getMsg())
                    .update();
        }
        return false;
    }

    @Override
    public SzSuggest getDetail(Integer id,Integer type) {
        SzSuggest sz=new SzSuggest();
        sz.setId(id);
        sz.setType(type);
        return getPage(new Page(), sz).getRecords().get(0);
    }
}
