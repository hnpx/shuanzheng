package com.px.pa.modulars.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("任务分类信息")
@Data
public class SzTaskCateResult {
    @ApiModelProperty("任务分类ID")
    private Integer id;
    @ApiModelProperty("任务分类名称")
    private String name;
    @ApiModelProperty("任务子分类")
    private List<SzTaskCateResult> childs;

}
