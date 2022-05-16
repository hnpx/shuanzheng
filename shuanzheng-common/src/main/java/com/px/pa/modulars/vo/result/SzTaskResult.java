package com.px.pa.modulars.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("任务的基础信息")
@Data
public class SzTaskResult {
    /**
     *
     */
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String name;
    /**
     * 任务类型：1-增分，0-减分
     */
    @ApiModelProperty(value = "任务类型：1-增分，0-减分")
    private Integer type;
    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Integer points;

    /**
     * 周期：1-日，2-周，3-月，4-季，5-年
     */
    @ApiModelProperty(value = "周期：1-日，2-周，3-月，4-季，5-年")
    private Integer period;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime stime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime etime;
    /**
     * 单个完成次数上限
     */
    @ApiModelProperty(value = "单个完成次数上限")
    private Integer singleNum;
    /**
     * 总完成次数上限
     */
    @ApiModelProperty(value = "总完成次数上限")
    private Integer allNum;
    /**
     * 积分上限
     */
    @ApiModelProperty(value = "积分上限")
    private Integer pointsLimit;

    @ApiModelProperty(value = "完成状态")
    private Integer status;

    @ApiModelProperty(value = "已完成的总积分上限")
    private Integer sumPoints;

    @ApiModelProperty(value = "已完成的总次数上限")
    private Integer sumNum;

    @ApiModelProperty(value = "完成次数")
    private Integer finishNum;

    @ApiModelProperty(value = "获得的积分")
    private Integer gainPoints;

    @ApiModelProperty(value = "当前周期完成次数")
    private Integer currNum;

    @ApiModelProperty(value = "范围")
    private Integer area;
}
