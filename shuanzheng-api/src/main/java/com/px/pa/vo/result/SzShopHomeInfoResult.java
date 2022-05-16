package com.px.pa.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhouz
 */
@ApiModel("积分商城首页")
@Data
public class SzShopHomeInfoResult {
    @ApiModelProperty("我的积分")
    private Integer myScore;
    @ApiModelProperty("积分商城中的分类信息")
    private List<SzShopCateResult> cates;

}
