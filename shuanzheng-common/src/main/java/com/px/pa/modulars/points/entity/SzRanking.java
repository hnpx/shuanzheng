package com.px.pa.modulars.points.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@ApiModel("栓正排名信息")
@Data
@TableName("sz_farm")
@EqualsAndHashCode(callSuper = true)
public class SzRanking
        extends Model<SzRanking> {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Integer id;
    @TableField("del_flag")
    private Integer delFlag;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
    private Integer sort;

    @ApiModelProperty(value = "分数")
    private Integer score;

    @ApiModelProperty(value = "部门ID")
    private Integer did;

    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "照片")
    private String photo;
    @ApiModelProperty(value = "部门名称")
    private String deptName;
    @ApiModelProperty(value = "")
    private Integer uid;

    @ApiModelProperty(value = "星星数量")
    private Integer num;
}
