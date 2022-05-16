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
import com.px.pa.modulars.core.entity.SzShopOrder;
import com.px.pa.modulars.vo.ExchangeRecordParam;
import com.px.pa.modulars.vo.ShopOrderParam;
import com.px.pa.modulars.vo.result.SzOrderResult;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 积分商品兑换记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Mapper
public interface SzShopOrderMapper extends BaseMapper<SzShopOrder> {

    @Select("select a.*,b.name,b.phone,g.cid,g.name gname,c.name hname from sz_shop_order a " +
            "left join sz_user b on a.uid=b.id " +
            "left join sz_shop_goods g on a.gid=g.id "+
            "left join sys_user c on a.hx_uid=c.user_id "+
            "${ew.customSqlSegment}")
    Page<ShopOrderParam> getPage(Page page, @Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select("SELECT so.id,so.points,sg.name,sg.image,sg.cid,sg.id as gid,so.state,so.create_time as createTime,so.update_time as updateTime" +
            " FROM sz_shop_order so LEFT JOIN sz_shop_goods sg on sg.id=so.gid" +
            " ${ew.customSqlSegment} ")
    public Page<SzOrderResult> queryOrderPage(Page page, @Param(Constants.WRAPPER)QueryWrapper wrapper);
}
