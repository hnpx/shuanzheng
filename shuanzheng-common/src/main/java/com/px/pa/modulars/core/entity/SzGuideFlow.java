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
 * 办事指南审核记录
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:16
 */
@Data
@TableName("sz_guide_flow")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "办事指南审核记录")
public class SzGuideFlow extends Model<SzGuideFlow> {
private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 申请记录id
     */
    @ApiModelProperty(value="申请记录id")
    private Integer rid;
    /**
     * 审核人id
     */
    @ApiModelProperty(value="审核人id")
    private Integer uid;
    /**
     * 审核时间
     */
    @ApiModelProperty(value="审核时间")
    private LocalDateTime utime;
    /**
     * 审核状态：0-待审核，1-审核通过，2-审核不通过
     */
    @ApiModelProperty(value="审核状态：0-待审核，2-审核通过，3-审核不通过")
    private Integer state;
    /**
     * 审核备注
     */
    @ApiModelProperty(value="审核备注")
    private String msg;
    /**
     * 手写签名
     */
    @ApiModelProperty(value="手写签名")
    private String image;
    /**
     * 审核人
     */
    @TableField(exist = false)
    @ApiModelProperty(value="审核人")
    private String uname;
    }
