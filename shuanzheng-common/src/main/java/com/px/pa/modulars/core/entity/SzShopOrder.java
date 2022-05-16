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
 * 积分商品兑换记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Data
@TableName("sz_shop_order")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "积分商品兑换记录")
public class SzShopOrder extends Model<SzShopOrder> {
    private static final long serialVersionUID = 1L;
    /**
     * 待核销
     */
    public static final int STATE_DHX = 0;

    /**
     * 已核销
     */
    public static final int STATE_YHX = 1;

    /**
     * 核销失败
     */
    public static final int STATE_HXSB = 2;
    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Integer gid;
    /**
     * 状态：0-待核销，1-已核销，2-核销失败
     */
    @ApiModelProperty(value = "状态：0-待核销，1-已核销，2-核销失败")
    private Integer state;
    /**
     * 花费积分
     */
    @ApiModelProperty(value = "花费积分")
    private Integer points;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer uid;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
    /**
     * 是否删除  -1：已删除  0：正常
     */
    @ApiModelProperty(value = "是否删除  -1：已删除  0：正常")
    private String delFlag;
    /**
     * 核销人id
     */
    @ApiModelProperty(value = "核销人id")
    private Integer hxUid;
}
