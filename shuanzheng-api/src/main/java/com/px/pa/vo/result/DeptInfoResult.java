package com.px.pa.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel("首页信息")
@Data
public class DeptInfoResult {
    private Integer deptId;
    @ApiModelProperty(value = "文字简介，概要")
    private String name;
    /**
     * 文字简介
     */
    @ApiModelProperty(value = "文字简介，概要")
    private String content;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String images;
    /**
     * 下属
     */
    @ApiModelProperty(value = "下属")
    private List<SzDeptSimpleResult> childs;

    @ApiModelProperty(value = "优秀村户")
    private List<SzFarmResult> farms;
}
