package com.px.pa.vo.param;

import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author zhouz
 */
@ApiModel("商品查询信息")
@Data
public class GoodsQueryParam extends BasePageParamVo {
    @ApiModelProperty("关键字")
    private String kw;
    @ApiModelProperty("分类ID")
    private Integer cate;
}
