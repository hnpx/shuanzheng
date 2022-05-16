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
 * 投票记录
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Data
@TableName("sz_vote_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "投票记录")
public class SzVoteLog extends Model<SzVoteLog> {
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
    /**
     * 投票活动id
     */
    @ApiModelProperty(value="投票活动id")
    private Integer vid;
    /**
     * 投票选项id
     */
    @ApiModelProperty(value="投票选项id")
    private Integer viid;
    /**
     * 投票人IP
     */
    @ApiModelProperty(value="投票人IP")
    private String ip;
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
     * 昵称
     */
    @ApiModelProperty(value="昵称")
    private String nickname;
    /**
     * 头像
     */
    @ApiModelProperty(value="头像")
    private String avatar;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String openid;

    /**
     * 投票活动名称
     */
    @TableField(exist = false)
    private String voteName;
    /**
     * 投票选项
     */
    @TableField(exist = false)
    private String voteItem;
    /**
     * 图片
     */
    @TableField(exist = false)
    private String image;
    /**
     * 编号
     */
    @TableField(exist = false)
    private String number;
    /**
     * 投票数
     */
    @TableField(exist = false)
    private Integer voteNum;

    }
