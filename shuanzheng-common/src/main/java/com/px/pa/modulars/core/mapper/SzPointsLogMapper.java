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

package com.px.pa.modulars.core.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.pa.modulars.core.entity.SzPointsLog;
import com.px.pa.modulars.core.entity.SzUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 积分记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Mapper
public interface SzPointsLogMapper extends BaseMapper<SzPointsLog> {

    @Select("select sum(points) points from sz_points_log a "+
            "${ew.customSqlSegment}")
    Integer getUserTodayPoints(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select("SELECT a.* FROM sz_points_log a " +
            "left join sz_user b on a.oid=b.id " +
            "${ew.customSqlSegment}")
    Page<SzPointsLog> getLeaderPointsLog(Page page,@Param("uid") Integer uid);
}
