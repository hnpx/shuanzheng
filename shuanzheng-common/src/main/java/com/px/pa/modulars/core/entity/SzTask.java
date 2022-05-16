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
 * 积分任务
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Data
@TableName("sz_task")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "积分任务")
public class SzTask extends Model<SzTask> {
private static final long serialVersionUID = 1L;
    /**
     * 户
     */
    public static final int AREA_H=1;
    /**
     * 村
     */
    public static final int AREA_C=2;
    /**
     * 企业
     */
    public static final int AREA_Q=3;
    /**
     *
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 任务名称
     */
    @ApiModelProperty(value="任务名称")
    private String name;
    /**
     * 任务类型：1-增分，0-减分
     */
    @ApiModelProperty(value="任务类型：1-增分，0-减分")
    private Integer type;
    /**
     * 任务范围：1-户，2-村，3-企业
     */
    @ApiModelProperty(value="任务范围：1-户，2-村，3-企业")
    private Integer area;
    /**
     * 积分
     */
    @ApiModelProperty(value="积分")
    private Integer points;
    /**
     * 周期：1-日，2-周，3-月，4-季，5-年
     */
    @ApiModelProperty(value="周期：1-日，2-周，3-月，4-季，5-年")
    private Integer period;
    /**
     * 开始时间
     */
    @ApiModelProperty(value="开始时间")
    private LocalDateTime stime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value="结束时间")
    private LocalDateTime etime;
    /**
     * 单个完成次数上限
     */
    @ApiModelProperty(value="单个完成次数上限")
    private Integer singleNum;
    /**
     * 总完成次数上限
     */
    @ApiModelProperty(value="总完成次数上限")
    private Integer allNum;
    /**
     * 积分上限
     */
    @ApiModelProperty(value="积分上限")
    private Integer pointsLimit;
    /**
     * 完成要求：1-文字，2-照片，3-视频
     */
    @ApiModelProperty(value="完成要求：1-文字，2-照片，3-视频")
    private Integer finish;
    /**
     * 活动状态：0-未开始，1-进行中，2-已结束
     */
    @ApiModelProperty(value="活动状态：0-未开始，1-进行中，2-已结束")
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
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String msg;
    /**
     * 图片
     */
    @ApiModelProperty(value="图片")
    private String image;
    /**
     * 内容
     */
    @ApiModelProperty(value="内容")
    private String content;
    @ApiModelProperty(value="一级分类")
    private Integer cid;
    @ApiModelProperty(value="二级分类")
    private Integer ccid;
    }
