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
import com.px.pa.modulars.core.entity.SzVote;
import com.px.pa.modulars.core.vo.ObjectVo;
import com.px.pa.modulars.core.vo.VoteVo;

import java.util.Date;
import java.util.List;

/**
 * 投票信息
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
public interface SzVoteService extends IService<SzVote> {


    /**
     * 投票选项接口（2.村庄，1.村民）
     */
    public List<ObjectVo> getObjectVo(Integer voteId,String name);

    /**
     * 时间差
     * @return
     */
    public int getTime(Date startTime, Date endTime);

    /**
     * 查询列表并按时间状态排序
     * @param page
     * @param name
     * @param type
     * @return
     */
    public Page<SzVote> getPageList(Page page,String name,Integer type);

    /**
     *  投票数 线程锁
     * @param voteVo
     */
    public void run(VoteVo voteVo);

    /**
     * 访问量线程锁
     * @param
     */
    public void run1(Integer id,Integer state);


    /**
     * 积分发放
     */
    public void issue(Integer vid);
}
