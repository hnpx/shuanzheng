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
import com.px.pa.modulars.core.entity.SzGuideFlow;
import com.px.pa.modulars.core.entity.SzUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 办事指南审核记录
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:16
 */
@Mapper
public interface SzGuideFlowMapper extends BaseMapper<SzGuideFlow> {

    @Select("select a.*,b.name uname from sz_guide_flow a " +
            "left join sz_user b on a.uid=b.id " +
            "${ew.customSqlSegment}")
    List<SzGuideFlow> flowList(@Param("ew") QueryWrapper wrapper);
}
