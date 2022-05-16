package com.px.pa.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 栓正村庄
 * @author zhouz
 */
@ApiModel("栓正村庄")
@Data
public class SzDeptResult {
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
}
