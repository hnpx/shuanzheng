package com.px.pa.modulars.vo;

import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("兑换记录查询参数")
public class ExchangeRecordParam extends BasePageParamVo {
    @ApiModelProperty("兑换状态")
    private Integer status;
    @ApiModelProperty("商品分类")
    private Integer cate;
    @ApiModelProperty("关键字")
    private String kw;

}
