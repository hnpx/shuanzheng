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
package com.px.pa.modulars.points.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.modulars.points.entity.PointsLevel;
import com.px.pa.modulars.points.mapper.PointsLevelMapper;
import com.px.pa.modulars.points.service.PointsLevelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 栓正积分星级配置
 *
 * @author 吕郭飞
 * @date 2021-05-07 14:27:29
 */
@Service
@Transactional
public class PointsLevelServiceImpl extends ServiceImpl<PointsLevelMapper, PointsLevel> implements PointsLevelService {

    /**
     * 查询符合哪个等级，返回星级积分
     *
     * @param score
     * @param levels
     * @return
     */
    @Override
    public int getLevel(Integer score, List<PointsLevel> levels) {
        if (levels == null || score == null) {
            return 0;
        }
        for (PointsLevel level : levels) {
            if (score >= level.getBeginScore() && score <= level.getEndScore()) {
                return level.getNum();
            }
        }
        return 0;
    }

}
