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
import java.util.List;

/**
 * 办事指南所属村列表
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:17
 */
@Data
@TableName("sz_guide_village")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "办事指南所属村列表")
public class SzGuideVillage extends Model<SzGuideVillage> {
private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 村id
     */
    @ApiModelProperty(value="村id")
    private Integer did;
    /**
     * 指南id
     */
    @ApiModelProperty(value="指南id")
    private Integer gid;
    /**
     * 用户id
     */
    @TableField(exist = false)
    @ApiModelProperty(value="用户id")
    private List<Integer> uid;

    @TableField(exist = false)
    @ApiModelProperty(value="村名字")
    private String dname;

    /**
     * 审核人列表
     */
    @TableField(exist = false)
    @ApiModelProperty(value="审核人列表")
    private List<SzUser> audit;
    }
