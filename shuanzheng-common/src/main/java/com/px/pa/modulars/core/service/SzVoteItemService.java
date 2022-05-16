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
import com.px.pa.modulars.core.entity.SzVoteItem;
import com.px.pa.modulars.core.vo.ImagesVo;
import com.px.pa.modulars.core.vo.SzVoteItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 投票选项
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
public interface SzVoteItemService extends IService<SzVoteItem> {

    public Page<SzVoteItem> getListPage(Page page,Integer vid,String key);

    /**
     * 查询选项排名
     */
    public Map<String,Object> getranking(Integer vid, Integer viid);

    /**
     * 查询选项排名 根据编号
     */
    public Map<String,Object> getrankingByNumber(Integer vid, Integer viid);


    /**
     * 解析[{},{}]json字符串
     */

    public List<ImagesVo> getJson(String json);

    /**
     * 获取投票数
     */
    public Integer getVoteNum(Integer vid);

    /**
     *
     * 排行范围
     */
    public List<SzVoteItem> getrankingList(Integer vid, Integer rank1,Integer rank2);

    List<SzVoteItemVo> listRank(Integer vid);
}
