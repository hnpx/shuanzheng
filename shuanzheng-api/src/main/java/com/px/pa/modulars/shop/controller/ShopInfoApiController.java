package com.px.pa.modulars.shop.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.pa.modulars.core.entity.SzShopGoods;
import com.px.pa.modulars.core.service.SzShopGoodsService;
import com.px.pa.modulars.shop.service.ShopApiService;
import com.px.pa.modulars.vo.result.SzShopGoodResult;
import com.px.pa.vo.param.GoodsQueryParam;
import com.px.pa.vo.result.SzShopHomeInfoResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商城信息服务
 *
 * @author zhouz
 */
@RequestMapping("/api/shop/info")
@RestController
@RequiredArgsConstructor
@Api(value = "/api/shop/info", tags = "积分商城信息服务")
public class ShopInfoApiController extends BaseApiController {
    @Autowired
    private ShopApiService shopApiService;
    @Autowired
    private SzShopGoodsService goodsService;


    @GetMapping("/read")
    @ApiOperation("读取首页信息")
    public R readHomeInfo() {
//        SzShopHomeInfoResult info = this.shopApiService.readHomeInfo(1);
        SzShopHomeInfoResult info = this.shopApiService.readHomeInfo(super.getCurrentId());
        return R.ok(info);
    }

    @PutMapping("/read/goods")
    @ApiOperation("读取商品信息")
    public R queryGoodsByCate(@RequestBody  GoodsQueryParam param) {
        Page<SzShopGoodResult> page = this.shopApiService.queryByCate(param);
        return R.ok(page);
    }


    @GetMapping("/read/goods/detail/{id}")
    @ApiOperation("读取商品信息")
    public R queryGoodsByCate(@PathVariable("id") Integer id) {
        SzShopGoods goods = this.goodsService.readDetail(id);
        return R.ok(goods);
    }

}
