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
 * 投票选项
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Data
@TableName("sz_vote_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "投票选项")
public class SzVoteItem extends Model<SzVoteItem> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 名称
     */
    @ApiModelProperty(value="名称")
    private String name;
    /**
     * 图片
     */
    @ApiModelProperty(value="图片")
    private String image;
    /**
     * 描述
     */
    @ApiModelProperty(value="描述")
    private String content;
    /**
     * 得票数
     */
    @ApiModelProperty(value="得票数")
    private Integer num;
    /**
     * 村庄id、村民id
     */
    @ApiModelProperty(value="村庄id、村民id")
    private Integer did;
    /**
     * 投票活动id
     */
    @ApiModelProperty(value="投票活动id")
    private Integer vid;
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
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String number;

    /**
     *
     *
     */
    @ApiModelProperty(value = "村庄名、村民名")
    private String dname;

  /*  @TableField(exist = false)
    private String dname;*/

    /**
     * 投票数
     */
    @TableField(exist = false)
    private Integer voteNum;

    /**
     * 投票状态
     */
    @TableField(exist = false)
    private Integer voteStatus;


    /**
     * 是否可投票状态(1.可以2.不可以)
     */
    @TableField(exist = false)
    private Integer isVote;

    /**
     * 排名
     */
    @TableField(exist = false)
    private Integer rank;

    /**
     * 头像
     */
    @TableField(exist = false)
    private String avatar;

    /**
     * 时间状态
     */
    @TableField(exist = false)
    private Integer timeStatus;

    /**
     * 是否限制投票
     */
    @TableField(exist = false)
    private Integer islimit;

    /**
     *
     */
    @TableField(exist = false)
    private SzVote szVote;

    @TableField(exist = false)
    private Integer isAuthor;
    @TableField(exist = false)
    private Integer integralstatus;
}
