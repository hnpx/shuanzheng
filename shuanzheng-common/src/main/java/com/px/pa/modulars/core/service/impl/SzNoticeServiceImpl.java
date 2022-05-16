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
import com.px.pa.modulars.core.entity.SzNotice;
import com.px.pa.modulars.core.mapper.SzNoticeMapper;
import com.px.pa.modulars.core.service.SzNoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 发布公告
 *
 * @author pig code generator
 * @date 2021-04-23 11:21:01
 */
@Service
@Transactional
public class SzNoticeServiceImpl extends ServiceImpl<SzNoticeMapper, SzNotice> implements SzNoticeService {

    @Override
    public List<SzNotice> queryLimit(Integer limit) {
        List<SzNotice> notices = this.lambdaQuery().orderByDesc(SzNotice::getUpdateTime, SzNotice::getCreateTime).last(" limit 0" + limit).list();
        return notices;
    }

    @Override
    public  SzNotice detail(Integer id) {
        SzNotice notice=super.getById(id);
        return notice;
    }
}
