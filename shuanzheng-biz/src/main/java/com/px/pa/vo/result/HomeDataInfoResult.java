package com.px.pa.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@ApiModel("首页数据信息")
@Data
public class HomeDataInfoResult {
    @ApiModelProperty("系统用户数量")
    private Integer sysUserNum;
    @ApiModelProperty("村民数量")
    private Integer peopleNum;
    @ApiModelProperty("村数量")
    private Integer villagerNum;
    @ApiModelProperty("干部数量数量")
    private Integer cadresNum;


    @ApiModelProperty("昨天登录用户数")
    private Integer loginNum;
    @ApiModelProperty("昨天完成任务数")
    private Integer yesterdayFinishTaskNum;

    @ApiModelProperty("今天完成任务数")
    private Integer todayFinishTaskNum;


    @ApiModelProperty("今天审核任务数")
    private Integer todayExamineTaskNum;

    @ApiModelProperty("村民人数统计")
    private Map<String,Integer> peopleInfoMap;

    @ApiModelProperty("用户人数统计")
    private Map<String,Integer> sysUserMap;

    @ApiModelProperty("任务完成量统计")
    private Map<String,Integer> taskFinishMap;

}
