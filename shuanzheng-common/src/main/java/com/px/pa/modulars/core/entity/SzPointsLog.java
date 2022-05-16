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
 * 积分记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Data
@TableName("sz_points_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "积分记录")
public class SzPointsLog extends Model<SzPointsLog> {
    private static final long serialVersionUID = 1L;

    /**
     * 增加
     */
    public static final int OPERATOR_ADD = 1;
    /**
     * 减少
     */
    public static final int OPERATOR_SUB = 0;
    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 对象id：部门id、用户id、企业id
     */
    @ApiModelProperty(value = "对象id：部门id、用户id、企业id")
    private Integer oid;
    /**
     * 积分操作类型：1-户积分，2-村积分，3-企业积分
     */
    @ApiModelProperty(value = "积分操作类型：1-户积分，2-村积分，3-企业积分")
    private Integer type;
    /**
     * 积分数量
     */
    @ApiModelProperty(value = "积分数量")
    private Integer points;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String msg;
    /**
     * 操作类型：1-增加，0-减少
     */
    @ApiModelProperty(value = "操作类型：1-增加，0-减少")
    private Integer operator;
    /**
     * 积分类型，1-投票积分，2-系统操作，3-订单积分，4-任务积分
     */
    @ApiModelProperty(value = "积分类型，1-投票积分，2-系统操作，3-订单积分，4-任务积分")
    private Integer opType;
    /**
     * int类型时间
     */
    @ApiModelProperty(value = "int类型时间")
    private Integer createInt;
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
     * 操作员id
     */
    @ApiModelProperty(value = "操作员id")
    private Integer uid;
    /**
     * 操作之前积分
     */
    @ApiModelProperty(value = "操作之前积分")
    private Integer pev;
    /**
     * 操作之后积分
     */
    @ApiModelProperty(value = "操作之后积分")
    private Integer next;
    /**
     * 图片信息
     */
    @ApiModelProperty(value = "图片信息")
    private String images;
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Integer orderId;
    /**
     * 批量操作
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "批量操作")
    private String ids;
}
