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
 * 栓正企业
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Data
@TableName("sz_company")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "栓正企业")
public class SzCompany extends Model<SzCompany> {
private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 企业名称
     */
    @ApiModelProperty(value="企业名称")
    private String name;
    /**
     * 企业logo
     */
    @ApiModelProperty(value="企业logo")
    private String image;
    /**
     * 企业简介
     */
    @ApiModelProperty(value="企业简介")
    private String content;
    /**
     * 历史总积分
     */
    @ApiModelProperty(value="历史总积分")
    private Integer allScore;
    /**
     * 剩余积分
     */
    @ApiModelProperty(value="剩余积分")
    private Integer score;
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
     * 是否显示  1：显示  0：不显示
     */
    @ApiModelProperty(value="是否显示  1：显示  0：不显示")
    private Integer state;
    }
