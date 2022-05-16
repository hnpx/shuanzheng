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
 * 任务完成记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Data
@TableName("sz_task_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "任务完成记录")
public class SzTaskRecord extends Model<SzTaskRecord> {
    private static final long serialVersionUID = 1L;
    public static final int STATUS_DSH = 0;
    public static final int STATUS_TG = 1;
    public static final int STATUS_WTG = 2;
    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private Integer tid;
    /**
     * 结果
     */
    @ApiModelProperty(value = "结果")
    private String result;
    /**
     * 提交人id
     */
    @ApiModelProperty(value = "提交人id")
    private Integer uid;
    /**
     * 提交备注
     */
    @ApiModelProperty(value = "提交备注")
    private String mark;
    /**
     * 审核状态：0-待审核，1-审核通过，2-审核不通过
     */
    @ApiModelProperty(value = "审核状态：0-待审核，1-审核通过，2-审核不通过")
    private Integer state;
    /**
     * 打分
     */
    @ApiModelProperty(value = "打分")
    private Integer points;
    /**
     * 审核备注
     */
    @ApiModelProperty(value = "审核备注")
    private String msg;
    /**
     * 帮助提交人id
     */
    @ApiModelProperty(value = "帮助提交人id")
    private Integer help;
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
     * 打分人id
     */
    @ApiModelProperty(value = "打分人id")
    private Integer pointsUid;
    /**
     * 打分时间
     */
    @ApiModelProperty(value = "打分时间")
    private LocalDateTime pointsTime;
    /**
     * 打分备注
     */
    @ApiModelProperty(value = "打分备注")
    private String pointsMsg;
    /**
     * 审核人id
     */
    @ApiModelProperty(value = "审核人id")
    private Integer shUid;
    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private LocalDateTime shTime;
    /**
     * 任务范围：1-户，2-村，3-企业
     */
    @ApiModelProperty(value = "任务范围：1-户，2-村，3-企业")
    private Integer area;
    /**
     * 积分类型
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "积分类型")
    private Integer type;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @TableField(exist = false)
    private LocalDateTime endTime;
    /**
     * 后台提交图片
     */
    @ApiModelProperty(value = "后台提交图片")
    private String image;

    @ApiModelProperty(value = "巡查员提交")
    private Integer xcx;
    @ApiModelProperty(value = "巡查员审核")
    private Integer shXcx;
}
