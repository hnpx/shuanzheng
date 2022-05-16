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

package com.px.pa.modulars.core.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.pa.modulars.core.entity.SzTask;
import com.px.pa.modulars.vo.TaskQueryParam;
import com.px.pa.modulars.vo.result.SzTaskDetailResult;
import com.px.pa.modulars.vo.result.SzTaskResult;

import java.util.List;

/**
 * 积分任务
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
public interface SzTaskService extends IService<SzTask> {
    /**
     * 查看某个村庄的某个完成状态的任务信息
     *
     * @param dept
     * @param status
     * @return
     */
    public List<SzTask> queryByCompletionStatusAndDept(Integer dept, Integer status);

    /**
     * 查询某个人的所有栓正任务信息
     *
     * @param user
     * @param param
     * @return
     */
    public Page<SzTaskResult> queryByUser(Integer user, TaskQueryParam param);

    /**
     * 查询某个任务的详细信息
     * @param userId
     * @param taskId
     * @return
     */
    public SzTaskDetailResult readDetail(Integer userId,Integer taskId,Integer uid);

    List<DateTime> getTaskDate(Integer period);

    Integer checkNum(Integer uid,SzTask task,Integer area);
}
