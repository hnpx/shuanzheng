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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.pa.modulars.core.entity.SzTaskRecord;
import com.px.pa.modulars.vo.TaskRecordParam;
import com.px.pa.modulars.vo.result.SzRecordSimpleResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 任务完成记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Mapper
public interface SzTaskRecordMapper extends BaseMapper<SzTaskRecord> {

    @Select("select * from " +
            "((SELECT a.*,b.`name` tname,b.finish,b.type,c.`name` uname,d.`username` hname,e.`name` pname,f.`name` sname,'' h1name,'' s1name FROM sz_task_record a left join sz_task b on a.tid=b.id left join sz_company c on a.uid=c.id left join sys_user d on a.`help`=d.user_id left join sys_user e on a.points_uid=e.user_id left join sys_user f on a.sh_uid=f.user_id ${ew.customSqlSegment} ) " +
            " union all " +
            "(SELECT a.*,b.`name` tname,b.finish,b.type,c.`name` uname,d.`name` hname,e.`name` pname,f.`name` sname,'' h1name,'' s1name FROM sz_task_record a left join sz_task b on a.tid=b.id left join sys_dept c on a.uid=c.dept_id left join sz_user d on a.`help`=d.id left join sz_user e on a.points_uid=e.id left join sys_user f on a.sh_uid=f.user_id ${ew1.customSqlSegment} ) " +
            " union all  " +
            "(SELECT a.*,b.`name` tname,b.finish,b.type,c.`name` uname,d.`name` hname,e.`name` pname,f.`name` sname,d1.name h1name,f1.name s1name FROM sz_task_record a left join sz_task b on a.tid=b.id left join sz_user c on a.uid=c.id left join sys_user d on a.`help`=d.user_id left join sz_user d1 on a.`help`=d1.id left join sz_user e on a.points_uid=e.id left join sys_user f on a.sh_uid=f.user_id left join sz_user f1 on a.sh_uid=f1.id ${ew2.customSqlSegment}  ) ) a order by a.create_time desc  "
    )
    Page<TaskRecordParam> getPage(Page page, @Param("ew") QueryWrapper wrapper, @Param("ew1") QueryWrapper wrapper1, @Param("ew2") QueryWrapper wrapper2);

    @Select("select a.id,a.xcx,a.mark,a.points,a.create_time,b.name tname,b.finish,c.name uname,d.name hname,a.image,a.msg,a.result " +
            "from sz_task_record a " +
            "left join sz_task b on a.tid=b.id " +
            "left join sys_user c on a.help=c.user_id " +
            "left join sz_user d on a.help=d.id " +
            "${ew.customSqlSegment}")
    Page<SzRecordSimpleResult> queryBadList(Page page, @Param("ew") QueryWrapper wrapper);

    @Select("SELECT a.*,b.`name` tname,b.finish,b.type,c.`name` uname," +
            "c.avatar,d.`name` hname,e.`name` pname,f.`name` sname," +
            "d1.name h1name,f1.name s1name,sd.name dept " +
            "FROM sz_task_record a left join " +
            "sz_task b on a.tid=b.id left join " +
            "sz_user c on a.uid=c.id left join " +
            "sys_dept sd on c.did=sd.dept_id left join " +
            "sys_user d on a.`help`=d.user_id " +
            "left join sz_user d1 on a.`help`=d1.id " +
            "left join sz_user e on a.points_uid=e.id " +
            "left join sys_user f on a.sh_uid=f.user_id " +
            "left join sz_user f1 on a.sh_uid=f1.id " +
            "${ew.customSqlSegment}"
    )
    Page<TaskRecordParam> deductList(Page page, @Param("ew") QueryWrapper wrapper);

}
