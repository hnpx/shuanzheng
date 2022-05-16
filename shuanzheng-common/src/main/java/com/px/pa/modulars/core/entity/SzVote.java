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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 投票信息
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Data
@TableName("sz_vote")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "投票信息")
public class SzVote extends Model<SzVote> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 投票活动标题
     */
    @ApiModelProperty(value="投票活动标题")
    private String name;

    @ApiModelProperty(value = "图片")
    private String pic;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="开始时间")
    private LocalDateTime stime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="结束时间")
    private LocalDateTime etime;
    /**
     * 投票限制天数
     */
    @ApiModelProperty(value="投票限制天数")
    private Integer day;
    /**
     * 一次可投几票
     */
    @ApiModelProperty(value="一次可投几票")
    private Integer num;
    /**
     * 投票对象：1-村民，2-村庄
     */
    @ApiModelProperty(value="投票对象：1-村民，2-村庄")
    private Integer type;
    /**
     * 限制人员：1-仅村民，0-不限
     */
    @ApiModelProperty(value="限制人员：1-仅村民，0-不限")
    private Integer area;
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

    @ApiModelProperty(value = "访问量")
    private Integer views;

    @ApiModelProperty(value = "介绍")
    private String content;
    @ApiModelProperty(value = "积分状态")
    private Integer integralStatus;

    /**
     * 投票数
     */
    @TableField(exist = false)
    private Integer voteNum;
    /**
     * 参数人数
     */
    @TableField(exist = false)
    private Integer participateNum;
    /**
     * 投票项
     */
    @TableField(exist = false)
    private Page<SzVoteItem> szVoteItemPage;
    /**
     * 投票是否可以头
     */
    @TableField(exist = false)
    private Integer status;

    /**
     * 时间状态
     */
    @TableField(exist = false)
    private Integer timeStatus;

    /**
     *
     * 时间状态筛选（null.全部 0.进行中 1.已结束）
     */
    @TableField(exist = false)
    private Integer typeTime;
    /**
     * 是否授权
     */
    @TableField(exist = false)
    private Integer isAuthor;



}
