package com.px.pa.modulars.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("办事指南审核人")
@Data
public class GuideUserResult {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "用户姓名")
    private String name;
    @ApiModelProperty(value = "电话")
    private String phone;
    @ApiModelProperty(value = "村")
    private String dept;
    @ApiModelProperty(value = "组")
    private String area;
}
