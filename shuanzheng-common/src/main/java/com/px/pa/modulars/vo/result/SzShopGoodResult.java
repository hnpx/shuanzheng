package com.px.pa.modulars.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 */
@ApiModel("积分商城中的商品信息")
@Data
public class SzShopGoodResult {

    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;
    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;
    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Integer stock;
    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Integer cid;
    /**
     * 所需积分
     */
    @ApiModelProperty(value = "所需积分")
    private Integer points;
    /**
     * 浏览量
     */
    @ApiModelProperty(value = "浏览量")
    private Integer views;
    /**
     * 兑换量
     */
    @ApiModelProperty(value = "兑换量")
    private Integer buys;
    /**
     * 核销量
     */
    @ApiModelProperty(value = "核销量")
    private Integer finish;
}
