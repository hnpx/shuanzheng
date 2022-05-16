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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import com.px.pa.modulars.core.entity.SzTaskRecord;
import com.px.pa.modulars.vo.DeductTaskQueryParam;
import com.px.pa.modulars.vo.TaskRecordParam;
import com.px.pa.modulars.vo.TaskRecordQueryParam;
import com.px.pa.modulars.vo.result.SzRecordSimpleResult;

import java.util.List;


/**
 * 任务完成记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
public interface SzTaskRecordService extends IService<SzTaskRecord> {

    boolean saveCompanyTask(SzTaskRecord record);

    boolean shenhe(SzTaskRecord record);

    Page<TaskRecordParam> getPage(Page page, TaskRecordParam szTaskRecord);

    public Page<SzRecordSimpleResult> queryToSimple(TaskRecordQueryParam param);
    public Page<SzRecordSimpleResult> queryBadList(Integer uid,BasePageParamVo param);

    public Page<SzRecordSimpleResult> queryByUserToSimple(Integer userId, TaskRecordQueryParam param);

    public List<SzRecordSimpleResult> packResult(List<SzRecordSimpleResult> results);

    /**
     * 查询是否存在待整改的任务
     * @param id 用户或者
     * @param type
     * @return
     */
    public Integer queryIsRectified(Integer id,Integer type);


    /**
     * 查询是否存在待整改的任务
     * @param id 用户或者
     * @param type
     * @return
     */
    public  List<SzRecordSimpleResult> queryRectified(Integer id,Integer type);

    Page<TaskRecordParam> deductList(DeductTaskQueryParam param);
    Boolean deductResult(DeductTaskQueryParam param,Integer userid);
}
