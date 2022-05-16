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
 * 政务信息内容
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Data
@TableName("sz_org_content")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "政务信息内容")
public class SzOrgContent extends Model<SzOrgContent> {
private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 政务信息标题
     */
    @ApiModelProperty(value="政务信息标题")
    private String name;
    /**
     * 主图
     */
    @ApiModelProperty(value="主图")
    private String image;
    /**
     * 视频连接
     */
    @ApiModelProperty(value="视频连接")
    private String video;
    /**
     * 图文内容
     */
    @ApiModelProperty(value="图文内容")
    private String content;
    /**
     * 是否显示：1-显示，0-不显示
     */
    @ApiModelProperty(value="是否显示：1-显示，0-不显示")
    private Integer state;
    /**
     * 政务分类id
     */
    @ApiModelProperty(value="政务分类id")
    private Integer cid;
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
     * 浏览量
     */
    @ApiModelProperty(value = "浏览量")
    private Integer views;
    }
