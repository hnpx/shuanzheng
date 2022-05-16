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

/**
 * 居民信息
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Data
@TableName("sz_user")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "居民信息")
public class SzUser extends Model<SzUser> implements Serializable {
private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 真实姓名
     */
    @ApiModelProperty(value="真实姓名")
    private String name;
    /**
     * 身份证号
     */
    @ApiModelProperty(value="身份证号")
    private String idcard;
    /**
     * 乡村id
     */
    @ApiModelProperty(value="乡村id")
    private Integer did;
    /**
     * 大队id
     */
    @ApiModelProperty(value="大队id")
    private Integer cdid;
    /**
     * 户号
     */
    @ApiModelProperty(value="户号")
    private String code;
    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号")
    private String phone;
    /**
     * 与户主关系
     */
    @ApiModelProperty(value="与户主关系")
    private String relation;
    /**
     * 微信OPENID
     */
    @ApiModelProperty(value="微信OPENID")
    private String openid;
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
     * 性别：1-男，2-女，0-保密
     */
    @ApiModelProperty(value="性别：1-男，2-女，0-保密")
    private Integer sex;
    /**
     * 是否可登录：1-可登录，0-不可登录
     */
    @ApiModelProperty(value="是否可登录：1-可登录，0-不可登录")
    private Integer login;
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
    @TableLogic
    private String delFlag;
    /**
     * 帮助上传信息用户id
     */
    @ApiModelProperty(value="帮助上传信息用户id")
    private Integer help;
    /**
     * 身份：1-村民，2-村干部，2-包村干部
     */
    @ApiModelProperty(value="身份：1-村民，2-村干部，3-包村干部")
    private Integer role;

    @ApiModelProperty(value="token")
    private String token;
    @ApiModelProperty(value="更新标示")
    private Integer editFlag;
    }
