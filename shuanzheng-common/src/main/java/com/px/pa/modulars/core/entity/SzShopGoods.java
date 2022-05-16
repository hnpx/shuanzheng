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

package com.px.pa.modulars.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积分商品
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Data
@TableName("sz_shop_goods")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "积分商品")
public class SzShopGoods extends Model<SzShopGoods> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 商品名称
     */
    @ApiModelProperty(value="商品名称")
    private String name;
    /**
     * 商品图片
     */
    @ApiModelProperty(value="商品图片")
    private String image;
    /**
     * 商品内容
     */
    @ApiModelProperty(value="商品内容")
    private String content;
    /**
     * 库存
     */
    @ApiModelProperty(value="库存")
    private Integer stock;
    /**
     * 分类id
     */
    @ApiModelProperty(value="分类id")
    private Integer cid;
    /**
     * 所需积分
     */
    @ApiModelProperty(value="所需积分")
    private Integer points;
    /**
     * 浏览量
     */
    @ApiModelProperty(value="浏览量")
    private Integer views;
    /**
     * 兑换量
     */
    @ApiModelProperty(value="兑换量")
    private Integer buys;
    /**
     * 核销量
     */
    @ApiModelProperty(value="核销量")
    private Integer finish;
    /**
     * 是否显示：1-显示，0-不显示
     */
    @ApiModelProperty(value="是否显示：1-显示，0-不显示")
    private Integer state;
    /**
     * 排序
     */
    @ApiModelProperty(value="排序")
    private Integer sort;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;
    /**
     * 是否删除  -1：已删除  0：正常
     */
    @ApiModelProperty(value="是否删除  -1：已删除  0：正常")
    private String delFlag;




}
