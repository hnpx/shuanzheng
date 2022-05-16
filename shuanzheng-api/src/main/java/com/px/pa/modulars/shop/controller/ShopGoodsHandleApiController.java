package com.px.pa.modulars.shop.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.pa.modulars.core.service.SzShopOrderService;
import com.px.pa.modulars.shop.service.ShopGoodsApiService;
import com.px.pa.modulars.vo.ExchangeRecordParam;
import com.px.pa.modulars.vo.result.SzOrderResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/shop/goods")
@RestController
@RequiredArgsConstructor
@Api(value = "/api/shop/goods", tags = "积分商城商品相关服务")
public class ShopGoodsHandleApiController extends BaseApiController {
    @Autowired
    private ShopGoodsApiService goodsApiService;
    @Autowired
    private SzShopOrderService orderService;

    @GetMapping("/exchange/{goodsId}")
    @ApiOperation("兑换")
    public R exchange(@PathVariable("goodsId") Integer goodsId) {
        Integer userId = super.getCurrentId();
//        Integer userId = 1;
        return this.goodsApiService.exchange(goodsId, userId);
    }

    @PutMapping("/read/exchange")
    @ApiOperation("兑换记录信息查询")
    public R exchangeRecord(@RequestBody ExchangeRecordParam param) {
        Integer userId = super.getCurrentId();
//        Integer userId = 1;
        Page<SzOrderResult> page = this.orderService.queryExchangeRecord(userId, param);
        return R.ok(page);
    }
}
