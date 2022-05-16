package com.px.pa.modulars.vo;


import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 */
@ApiModel("榜单公示信息查询")
@Data
public class RankingItemQueryParam extends BasePageParamVo {
    @ApiModelProperty("类型，1、推介村民；2、待整改村民；3、黑名单")
    private Integer type;
    @ApiModelProperty("关键字查询")
    private String kw;

}
