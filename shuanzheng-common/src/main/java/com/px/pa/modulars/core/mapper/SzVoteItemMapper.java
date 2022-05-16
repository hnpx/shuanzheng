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
import com.px.pa.modulars.core.entity.SzVoteItem;
import com.px.pa.modulars.core.vo.SzVoteItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 投票选项
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Mapper
public interface SzVoteItemMapper extends BaseMapper<SzVoteItem> {


    public Page<SzVoteItem> getListPage(@Param("page") Page page,@Param("vid") Integer vid,@Param("key") String key);

    /**
     * 查询选项排名
     */
    public Map<String,Object> getranking(@Param("vid") Integer vid,@Param("viid") Integer viid);

    /**
     * 查询选项排名 根据编号
     */
    public Map<String,Object> getrankingByNumber(@Param("vid") Integer vid,@Param("viid") Integer viid);

    /**
     * 获取投票数
     */
    public Integer getVoteNum(@Param("vid") Integer vid);

    /**
     *
     * 排行范围
     */
    public List<SzVoteItem> getrankingList(@Param("vid") Integer vid, @Param("rank1") Integer rank1, @Param("rank2") Integer rank2);

    @Select("set @x=0;select a.dname,a.num,a.number,c.name vname,d.name gname,@x:=ifnull(@x,0)+1 as rank " +
            "from sz_vote_item a " +
            "left join sz_user b on a.did=b.id " +
            "left join sys_dept c on b.did=c.dept_id " +
            "left join sys_dept d on b.cdid=d.dept_id " +
            "${ew.customSqlSegment}")
    List<SzVoteItemVo> listRank(@Param("ew") QueryWrapper wrapper);
}
