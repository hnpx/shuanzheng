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
import com.px.pa.modulars.core.entity.SzGuideRecord;
import com.px.pa.modulars.core.entity.SzGuideUser;
import com.px.pa.modulars.core.entity.SzSuggest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 办事指南申请记录
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:19
 */
@Mapper
public interface SzGuideRecordMapper extends BaseMapper<SzGuideRecord> {

    @Select("select a.*,b.name gname,c.name uname from sz_guide_record a " +
            "left join sz_guide b on a.gid=b.id " +
            "left join sz_user c on a.uid=c.id " +
            "${ew.customSqlSegment}")
    Page<SzGuideRecord> getPage(Page page, @Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select("select a.*,b.name gname,c.name uname from sz_guide_flow f " +
            "left join sz_guide_record a on f.rid=a.id " +
            "left join sz_guide b on a.gid=b.id " +
            "left join sz_user c on a.uid=c.id " +
            "${ew.customSqlSegment}")
    Page<SzGuideRecord> getPageBySh(Page page, @Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select("select d.* from sz_guide_record a " +
            "left join sz_user b on a.uid=b.id " +
            "left join sz_guide_village c on a.gid=c.gid and b.did=c.did " +
            "left join sz_guide_user d on c.id=d.vid " +
            "${ew.customSqlSegment}")
    List<SzGuideUser> getNextUser(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
