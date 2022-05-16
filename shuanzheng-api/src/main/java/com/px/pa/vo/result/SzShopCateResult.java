package com.px.pa.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @author zhouz
 */
@ApiModel("栓正积分商城中包含的分类")
@Data
public class SzShopCateResult {
    @ApiModelProperty("分类名称")
    private String name;
    @ApiModelProperty("分类ID")
    private Integer id;
    @ApiModelProperty
    private Integer num;
}
