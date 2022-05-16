package com.px.pa.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("公告")
public class SzNoticeResult {

    /**
     *
     */
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String name;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "主图")
    private String image;
}
