package com.px.pa.modulars.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.px.pa.modulars.core.entity.SzTaskRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务记录
 *
 * @author zhouz
 */
@ApiModel(description = "任务记录")
@Data
public class TaskRecordParam extends SzTaskRecord {

    @ApiModelProperty("任务名称")
    private String tname;

    @ApiModelProperty("用户姓名")
    private String uname;
    @ApiModelProperty("用户头像")
    private String avatar;
    @ApiModelProperty("用户所属村")
    private String dept;

    @ApiModelProperty("帮助提交人")
    private String hname;
    @ApiModelProperty("帮助提交人")
    private String h1name;
    @ApiModelProperty(value = "提交时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "提交时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("打分人")
    private String pname;

    @ApiModelProperty("审核人姓名")
    private String sname;
    @ApiModelProperty("审核人姓名")
    private String s1name;
    @ApiModelProperty(value = "提交时间")
    private LocalDateTime shTime;

    @ApiModelProperty("结果类型")
    private Integer finish;

}
