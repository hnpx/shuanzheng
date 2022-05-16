package com.px.pa.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 汇聚三农的服务接口
 */

@ApiModel("首页信息")
@Data
public class FarmInfoResult {
    @ApiModelProperty("栓正优秀村户")
    private List<SzFarmResult> farms;

    @ApiModelProperty("种植养殖政策")
    private List<SzFarmResult> zcs;
}
