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
 * 村民需求
 *
 * @author pig code generator
 * @date 2021-04-13 11:26:18
 */
@Data
@TableName("sz_suggest")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "村民需求")
public class SzSuggest extends Model<SzSuggest> {
private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Integer uid;
    @ApiModelProperty("需求标题")
    private String title;
    /**
     * 处理方法
     */
    @TableField(exist = false)
    @ApiModelProperty(value="用户姓名")
    private String uname;
    /**
     * 需求内容
     */
    @ApiModelProperty(value="需求内容")
    private String content;
    /**
     * 处理方法
     */
    @ApiModelProperty(value="处理方法")
    private String msg;
    /**
     * 状态：0-不公示，1-公示
     */
    @ApiModelProperty(value="状态：0-不公示，1-公示")
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
     * 状态：1-需求，2-建议
     */
    @ApiModelProperty(value="类型：1-需求，2-建议")
    private Integer type;
    /**
     * 联系人
     */
    @ApiModelProperty(value="联系人")
    private String contact;
    /**
     * 联系电话
     */
    @ApiModelProperty(value="联系电话")
    private String phone;
    }
