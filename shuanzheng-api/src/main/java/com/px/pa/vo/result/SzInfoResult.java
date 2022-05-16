package com.px.pa.vo.result;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 栓正释义
 */
@Data
@ApiModel("栓正释义")
public class SzInfoResult {

    /**
     *
     */
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 视频介绍
     */
    @ApiModelProperty(value = "视频介绍")
    private String video;
    /**
     * 图文介绍
     */
    @ApiModelProperty(value = "图文介绍")
    private String content;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;
}
