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
import com.px.pa.modulars.core.entity.SzFarm;
import com.px.pa.modulars.core.mapper.SzFarmMapper;
import com.px.pa.modulars.core.service.SzFarmService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 汇聚三农
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Service
@Transactional
public class SzFarmServiceImpl extends ServiceImpl<SzFarmMapper, SzFarm> implements SzFarmService {

    @Override
    public List<SzFarm> queryByLimit(Integer cid, Integer limit) {
        List<SzFarm> szFarms = super.lambdaQuery().eq(SzFarm::getDelFlag, 0).eq(SzFarm::getCid, cid).orderByDesc(SzFarm::getSort, SzFarm::getUpdateTime, SzFarm::getCreateTime).last(" limit 0," + limit).list();
        return szFarms;
    }

    @Override
    public synchronized SzFarm detail(Integer id) {
        SzFarm farm = super.getById(id);
        farm.setViews((int) (farm.getViews() == null ? 0 : farm.getViews()) + 1);
        super.updateById(farm);
        return farm;
    }
}
