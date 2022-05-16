package com.px.pa.modulars.vo;

import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务信息查询参数
 *
 * @author zhouz
 */
@ApiModel("任务信息查询参数")
@Data
public class TaskQueryParam extends BasePageParamVo {
    @ApiModelProperty("任务分类")
    private Integer cate;
    @ApiModelProperty("任务子分类")
    private Integer ccate;
    @ApiModelProperty("任务完成状态")
    private Integer status;
    @ApiModelProperty("任务查询关键字")
    private String kw;

    @ApiModelProperty("任务范围：1-户，2-村，3-企业")
    private Integer area;
    @ApiModelProperty("任务的信息类型，1：正常，2：超期")
    private Integer type;
    @ApiModelProperty("干部id")
    private Integer uid;
    @ApiModelProperty("扣分任务")
    private Integer deduct;

}
