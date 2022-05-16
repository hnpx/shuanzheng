package com.px.pa.modulars.vo.result;

import com.px.pa.modulars.core.entity.SzTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhouz
 */
@Data
@ApiModel("栓正任务详情")
public class SzTaskDetailResult {
    @ApiModelProperty("任务")
    private SzTask task;
    @ApiModelProperty("完成记录")
    private List<SzRecordSimpleResult> records;
    private Long recordsNum;
    @ApiModelProperty("我的完成记录")
    private SzRecordSimpleResult myRecord;
    @ApiModelProperty("周期内完成次数")
    private Integer currNum;
}
