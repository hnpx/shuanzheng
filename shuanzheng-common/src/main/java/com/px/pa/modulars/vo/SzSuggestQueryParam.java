package com.px.pa.modulars.vo;

import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 */
@Data
public class SzSuggestQueryParam extends BasePageParamVo {

    @ApiModelProperty(value = "状态：0-待处理，1-已处理，2-不处理")
    private Integer state;

    @ApiModelProperty(value = "类型：1-需求，2-建议")
    private Integer type;
}
