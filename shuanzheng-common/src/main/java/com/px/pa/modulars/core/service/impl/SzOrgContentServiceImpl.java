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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.modulars.core.entity.SzOrgContent;
import com.px.pa.modulars.core.mapper.SzOrgContentMapper;
import com.px.pa.modulars.core.service.SzOrgContentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 政务信息内容
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Service
@Transactional
public class SzOrgContentServiceImpl extends ServiceImpl<SzOrgContentMapper, SzOrgContent> implements SzOrgContentService {

    @Override
    public List<SzOrgContent> queryByLimit(Integer limit,Integer id) {
        List<SzOrgContent> contents = super.lambdaQuery().eq(SzOrgContent::getDelFlag, 0)
                .eq(SzOrgContent::getCid,id)
                .orderByDesc(SzOrgContent::getSort, SzOrgContent::getUpdateTime, SzOrgContent::getCreateTime)
                .last(" limit 0," + limit).list();
        return contents;
    }

    @Override
    public  SzOrgContent detail(Integer id) {
        SzOrgContent content=super.getById(id);
        return content;
    }
}