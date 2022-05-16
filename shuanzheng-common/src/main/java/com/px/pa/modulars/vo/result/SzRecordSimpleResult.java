package com.px.pa.modulars.vo.result;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhouz
 */
@Data
@ApiModel("栓正完成记录")
public class SzRecordSimpleResult {
    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private Integer tid;
    @ApiModelProperty(value = "任务名称")
    private String tname;
    @ApiModelProperty(value = "任务图片")
    private String image;
    /**
     * 提交人id
     */
    @ApiModelProperty(value = "提交人id")
    private Integer uid;
    @ApiModelProperty(value = "提交人id")
    private String uname;
    @ApiModelProperty(value = "帮助提交人id")
    private String hname;
    @ApiModelProperty(value = "提交人id")
    private String uphoto;
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "任务备注")
    private String mark;

    @ApiModelProperty(value = "积分")
    private Integer points;

    @ApiModelProperty(value = "完成要求")
    private Integer finish;

    @ApiModelProperty(value = "村名称")
    private String deptName;

    @ApiModelProperty(value = "完成说明")
    private String msg;
    @ApiModelProperty(value = "图片")
    private String result;
    @ApiModelProperty(value = "提交人类型")
    private Integer xcx;

}
