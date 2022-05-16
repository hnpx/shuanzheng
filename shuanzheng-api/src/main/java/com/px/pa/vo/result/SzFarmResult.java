package com.px.pa.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("优秀村户、栓正政策")
@Data
public class SzFarmResult {

    private Integer id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty("优秀类型")
    private String type;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容，概述")
    private String content;
    @ApiModelProperty(value = "主图")
    private String image;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;



}
