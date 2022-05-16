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
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.vo.result.GuideUserResult;

import java.math.BigDecimal;

import java.util.List;

/**
 * 居民信息
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
public interface SzUserService extends IService<SzUser> {

    /**
     * 村干部管理成员
     */

    Page<SzUser>  getSzUserPage(Integer uid,Page page,String key,Integer role);

    List<SzUser> villageUser(Integer vid);

    Page<GuideUserResult> userList(Page page, String key);
}
