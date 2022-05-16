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

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.pa.modulars.core.entity.SzGuide;
import com.px.pa.modulars.vo.result.GuideUserResult;

import java.util.List;

/**
 * 办事指南
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:19
 */
public interface SzGuideService extends IService<SzGuide> {

    List<GuideUserResult> getUsers(Integer id, Integer did);
    Integer getFirstUser(Integer id,Integer did);
}
