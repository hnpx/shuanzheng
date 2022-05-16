package com.px.pa.modulars.shop.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.pa.modulars.core.entity.SzShopCate;
import com.px.pa.modulars.core.entity.SzShopGoods;
import com.px.pa.modulars.core.entity.SzTaskRecord;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.service.SzShopCateService;
import com.px.pa.modulars.core.service.SzShopGoodsService;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.utils.bean.BaseQueryToPageUtil;
import com.px.pa.utils.bean.BeanToResultUtil;
import com.px.pa.modulars.vo.result.SzShopGoodResult;
import com.px.pa.vo.param.GoodsQueryParam;
import com.px.pa.vo.result.SzShopCateResult;
import com.px.pa.vo.result.SzShopHomeInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhouz
 */
@Service
@Transactional
public class ShopApiService {
    @Value("${application.shop.goods-len}")
    private Integer goodsLen;
    @Autowired
    private SzUserService userService;
    @Autowired
    private SzShopCateService shopCateService;
    @Autowired
    private SzShopGoodsService shopGoodsService;

    /**
     * 读取首页信息
     *
     * @param userId 用户ID
     * @return
     */
    public SzShopHomeInfoResult readHomeInfo(Integer userId) {
        SzShopHomeInfoResult info = new SzShopHomeInfoResult();
        SzUser user = this.userService.getById(userId);
        info.setMyScore(user.getScore());
        List<SzShopCate> cates = this.shopCateService.lambdaQuery().eq(SzShopCate::getDelFlag, 0).orderByDesc(SzShopCate::getSort, SzShopCate::getUpdateTime, SzShopCate::getCreateTime).list();
        List<SzShopCateResult> cateResults = BeanToResultUtil.beanToResult(cates, SzShopCateResult.class);
        info.setCates(cateResults);
        return info;
    }

    /**
     * 查询某个分类的商品信息
     *
     * @return
     */
    public Page<SzShopGoodResult> queryByCate(GoodsQueryParam param) {
        Page<SzShopGoods> page =BaseQueryToPageUtil.createPage(param);
        page = this.shopGoodsService.lambdaQuery().eq(param.getCate() != null, SzShopGoods::getCid, param.getCate()).eq(SzShopGoods::getDelFlag, 0).like(StrUtil.isNotBlank(param.getKw()), SzShopGoods::getName, param.getKw()).orderByDesc(SzShopGoods::getSort, SzShopGoods::getUpdateTime, SzShopGoods::getCreateTime).page(page);
        Page<SzShopGoodResult> pageResult = BeanToResultUtil.beanToResult(page, SzShopGoodResult.class);
        return pageResult;
    }
}
