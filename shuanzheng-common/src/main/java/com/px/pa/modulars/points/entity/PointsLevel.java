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

package com.px.pa.modulars.points.entity;

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
 * 栓正积分星级配置
 *
 * @author 吕郭飞
 * @date 2021-05-07 14:27:29
 */
@Data
@TableName("sz_points_level")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "栓正积分星级配置")
public class PointsLevel extends Model<PointsLevel> {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private LocalDateTime createTime;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private LocalDateTime updateTime;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private Integer delFlag;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private Integer sort;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 开始分值
     */
    @ApiModelProperty(value = "开始分值")
    private Integer beginScore;
    /**
     * 结束分值
     */
    @ApiModelProperty(value = "结束分值")
    private Integer endScore;

    @ApiModelProperty("积分类型，1、居民；2、驻村干部；3、村庄榜；4、村干部榜；5、企业榜")
    private Integer type;
    /**
     * 星级
     */
    @ApiModelProperty(value = "星级")
    private Integer num;

}
