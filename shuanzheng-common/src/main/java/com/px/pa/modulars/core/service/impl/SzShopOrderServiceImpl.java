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
package com.px.pa.modulars.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.pa.modulars.core.entity.SzShopGoods;
import com.px.pa.modulars.core.entity.SzShopOrder;
import com.px.pa.modulars.core.mapper.SzShopOrderMapper;
import com.px.pa.modulars.core.service.SzShopGoodsService;
import com.px.pa.modulars.core.service.SzShopOrderService;
import com.px.pa.modulars.vo.ExchangeRecordParam;
import com.px.pa.modulars.vo.ShopOrderParam;
import com.px.pa.modulars.vo.result.SzOrderResult;
import com.px.pa.utils.bean.BaseQueryToPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 积分商品兑换记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Service
@Transactional
public class SzShopOrderServiceImpl extends ServiceImpl<SzShopOrderMapper, SzShopOrder> implements SzShopOrderService {

    @Autowired
    private SzShopGoodsService goodsService;

    @Override
    public Page<ShopOrderParam> getPage(Page page, ShopOrderParam param) {
        QueryWrapper query = new QueryWrapper();
        query.like(StrUtil.isNotEmpty(param.getName()), "b.name", param.getName());
        query.like(StrUtil.isNotEmpty(param.getGname()), "g.name", param.getGname());
        query.like(StrUtil.isNotEmpty(param.getPhone()), "b.phone", param.getPhone());
        query.eq(param.getState() != null, "a.state", param.getState());
        query.eq(param.getCid() != null, "g.cid", param.getCid());
        query.eq(param.getGid() != null, "a.gid", param.getGid());
        query.gt(param.getStartTime() != null, "a.create_time", param.getStartTime());
        query.lt(param.getEndTime() != null, "a.create_time", param.getEndTime());
        query.orderByDesc("a.id");
        return super.baseMapper.getPage(page, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean hx(Integer id) {
        SzShopOrder so = getById(id);
        if (so != null && so.getState() == 0) {
            Integer uid = SecurityUtils.getUser().getId();
            boolean b = super.lambdaUpdate()
                    .eq(SzShopOrder::getId, id)
                    .set(SzShopOrder::getState, 1)
                    .set(SzShopOrder::getHxUid, uid)
                    .update();
            if (b) {
                Integer gid = getById(id).getGid();
                boolean c = goodsService.lambdaUpdate().eq(SzShopGoods::getId, gid).setSql("finish=finish+1").update();
                if (c) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Page<SzOrderResult> queryExchangeRecord(Integer userId, ExchangeRecordParam param) {
        Page<SzOrderResult> page = BaseQueryToPageUtil.createPage(param);
        QueryWrapper query = new QueryWrapper();
        query.like(StrUtil.isNotBlank(param.getKw()), "sg.name", param.getKw());
        query.eq(param.getCate() != null, "sg.cid", param.getCate());
        query.eq("so.uid", userId);
        query.eq("so.state",param.getStatus());
        if (param.getStatus() == SzShopOrder.STATE_DHX) {
            query.orderByDesc("so.create_time");
        } else {
            query.orderByDesc("so.update_time");
        }
        return super.baseMapper.queryOrderPage(page, query);
    }
}
