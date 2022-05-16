package com.px.pa.modulars.shop.service;


import cn.hutool.core.util.NumberUtil;
import com.pig4cloud.pig.common.core.util.R;
import com.px.pa.constants.OrgTypeEnum;
import com.px.pa.constants.PointsTypeEnum;
import com.px.pa.modulars.core.entity.*;
import com.px.pa.modulars.core.service.SzPointsLogService;
import com.px.pa.modulars.core.service.SzShopGoodsService;
import com.px.pa.modulars.core.service.SzShopOrderService;
import com.px.pa.modulars.core.service.SzUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author zhouz
 */
@Service
@Transactional
public class ShopGoodsApiService {
    @Autowired
    private SzShopGoodsService goodsService;
    @Autowired
    private SzShopOrderService orderService;
    @Autowired
    private SzUserService userService;
    @Autowired
    private SzPointsLogService pointsLogService;

    /**
     * 兑换商品
     *
     * @param goodsId
     * @param userId
     * @return
     */
    @Transactional
    public synchronized R exchange(Integer goodsId, Integer userId) {
        SzUser user = this.userService.getById(userId);
        SzShopGoods goods = this.goodsService.getById(goodsId);
        if (user.getScore() == null) {
            user.setScore(0);
        }
        //如果当前的分值超过用户拥有的积分，报错
        if (goods.getPoints() > user.getScore()) {
            return R.failed("用户积分不足");
        }
        //增加销量
        goods.setBuys(goods.getBuys()==null?1:(goods.getBuys()+1));
        //减少库存
        goods.setStock(goods.getStock()==null?0:(goods.getStock()-1));
        this.goodsService.updateById(goods);
        //创建兑换订单
        SzShopOrder order = new SzShopOrder();
        order.setGid(goodsId);
        order.setState(SzShopOrder.STATE_DHX);
        order.setPoints(goods.getPoints());
        order.setUid(userId);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(order.getCreateTime());
        this.orderService.save(order);


        // 记录日志
        SzPointsLog log = this.createLogs(goods, user);
        this.pointsLogService.saveLog(log, null);
        //消耗积分
        user.setScore(user.getScore() - goods.getPoints());
        this.userService.updateById(user);

        return R.ok();
    }


    private SzPointsLog createLogs(SzShopGoods goods, SzUser user) {
        SzPointsLog log = new SzPointsLog();
        log.setIds(user.getId() + "");
        log.setType(OrgTypeEnum.HOUSE.getValue());
        log.setPoints(goods.getPoints());
        log.setOperator(SzPointsLog.OPERATOR_SUB);
        log.setOpType(PointsTypeEnum.ORDER.getValue());
        log.setCreateInt(Math.toIntExact(System.currentTimeMillis() / 1000));
        log.setMsg("兑换商品：" + goods.getName());
        return log;
    }


}
