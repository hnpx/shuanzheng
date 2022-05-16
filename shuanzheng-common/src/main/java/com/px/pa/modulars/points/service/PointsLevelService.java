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

package com.px.pa.modulars.points.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.pa.modulars.points.entity.PointsLevel;

import java.util.List;

/**
 * 栓正积分星级配置
 *
 * @author 吕郭飞
 * @date 2021-05-07 14:27:29
 */
public interface PointsLevelService extends IService<PointsLevel> {


    public int getLevel(Integer score, List<PointsLevel> levels);
}
