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
import com.px.pa.modulars.core.entity.SzSuggest;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.vo.TaskRecordParam;
import com.px.pa.modulars.vo.result.GuideUserResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 居民信息
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Mapper
public interface SzUserMapper extends BaseMapper<SzUser> {

    @Select("select b.id,b.name from sys_user_dept a left join sz_user b on a.user_id=b.id ${ew.customSqlSegment}")
    List<SzUser> villageUser(@Param("ew") QueryWrapper wrapper);

    @Select("select a.id,a.name,a.phone,b.name dept,c.name area from sz_user a " +
            "left join sys_dept b on a.did=b.dept_id " +
            "left join sys_dept c on a.cdid=c.dept_id " +
            "${ew.customSqlSegment}")
    Page<GuideUserResult> userList(Page page, @Param(Constants.WRAPPER) QueryWrapper wrapper);
}
