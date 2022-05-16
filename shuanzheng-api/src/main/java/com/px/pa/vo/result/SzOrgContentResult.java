package com.px.pa.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SzOrgContentResult {

    /**
     *
     */
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 政务信息标题
     */
    @ApiModelProperty(value = "政务信息标题")
    private String name;
    /**
     * 主图
     */
    @ApiModelProperty(value = "主图")
    private String image;
    /**
     * 政务分类id
     */
    @ApiModelProperty(value = "政务分类id")
    private Integer cid;
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
}
