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
import com.px.pa.modulars.core.entity.SzTask;
import com.px.pa.modulars.vo.result.SzTaskResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 积分任务
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Mapper
public interface SzTaskMapper extends BaseMapper<SzTask> {

    /**
     * 查看某个村庄的某个完成状态的任务信息
     *
     * @param dept
     * @param status
     * @return
     */
    public List<SzTask> queryByCompletionStatusAndDept(@Param("deptId") Integer dept, @Param("status") Integer status);



    Page<SzTaskResult> queryByUser(Page page, @Param("userId") Integer userId,@Param("trUid")Integer trUid,@Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select("SELECT count(id) as num,sum(points) as score FROM sz_task_record WHERE state!=#{state} and del_flag=0 and tid=#{taskId}")
    public Map<String, Object> countNumAndScoreByTask(@Param("taskId") Integer taskId, @Param("state") Integer state);
}
