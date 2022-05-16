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
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * 办事指南申请记录
 *
 * @author pig code generator
 * @date 2021-05-06 17:12:19
 */
@Data
@TableName("sz_guide_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "办事指南申请记录")
public class SzGuideRecord extends Model<SzGuideRecord> {
private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 指南id
     */
    @ApiModelProperty(value="指南id")
    private Integer gid;
    /**
     * 村民id
     */
    @ApiModelProperty(value="村民id")
    private Integer uid;
    /**
     * 申请内容
     */
    @ApiModelProperty(value="申请内容")
    private String content;
    /**
     * 申请时间
     */
    @ApiModelProperty(value="申请时间")
    private LocalDateTime sqDate;
    /**
     * 申请图片
     */
    @ApiModelProperty(value="申请图片")
    private String images;
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
     * 0-待审核，1-审核中，2-审核通过，3-审核不通过
     */
    @ApiModelProperty(value="0-待审核，1-审核中，2-审核通过，3-审核不通过")
    private Integer state;
    /**
     * 办事指南
     */
    @TableField(exist = false)
    @ApiModelProperty(value="办事指南")
    private String gname;
    /**
     * 用户姓名
     */
    @TableField(exist = false)
    @ApiModelProperty(value="用户姓名")
    private String uname;
    /**
     * 用户信息
     */
    @TableField(exist = false)
    @ApiModelProperty(value="用户信息")
    private SzUser user;
    /**
     * 审核流程
     */
    @TableField(exist = false)
    @ApiModelProperty(value="审核流程")
    private List<SzGuideFlow> flow;
    /**
     * 编号
     */
    @ApiModelProperty(value="编号")
    private String code;
    /**
     * 是否删除  -1：已删除  0：正常
     */
    @ApiModelProperty(value = "是否删除  -1：已删除  0：正常")
    @TableLogic
    private String delFlag;
    }
