package com.px.pa.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 栓正释义
 */
@Data
@ApiModel("用户积分信息")
public class SzUserPointsResult {

    @ApiModelProperty(value = "累计积分")
    private Integer allScore;
    @ApiModelProperty(value = "剩余积分")
    private Integer score;
    @ApiModelProperty(value = "累计消耗")
    private Integer used;
    @ApiModelProperty(value = "今日获得")
    private Integer today;
}
