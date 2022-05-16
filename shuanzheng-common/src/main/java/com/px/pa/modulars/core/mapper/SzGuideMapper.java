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
import com.px.pa.modulars.core.entity.SzGuide;
import com.px.pa.modulars.core.entity.SzGuideFlow;
import com.px.pa.modulars.vo.result.GuideUserResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 办事指南
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:19
 */
@Mapper
public interface SzGuideMapper extends BaseMapper<SzGuide> {

    @Select("select e.name,e.phone,d.name dept from sz_guide_village a " +
            "left join sz_guide_user b on a.id=b.vid " +
            "left join sys_user_dept c on b.uid=c.user_id " +
            "left join sys_dept d on c.dept_id=d.dept_id " +
            "left join sz_user e on b.uid=e.id " +
            "${ew.customSqlSegment}")
    List<GuideUserResult> getUsers(@Param("ew") QueryWrapper wrapper);

    @Select("select b.uid from sz_guide_village a " +
            "left join sz_guide_user b on a.id=b.vid " +
            "${ew.customSqlSegment}")
    Integer getFirstUser(@Param("ew") QueryWrapper wrapper);
}
