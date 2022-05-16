package com.px.pa.modulars.vo;

import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 */
@ApiModel("任务记录查询")
@Data
public class TaskRecordQueryParam extends BasePageParamVo {
    @ApiModelProperty("任务ID")
    private Integer tid;
    @ApiModelProperty("任务类型：1-增分，0-减分")
    private Integer type;


    /**
     * 审核状态：0-待审核，1-审核通过，2-审核不通过
     */
    @ApiModelProperty(value = "审核状态：0-待审核，1-审核通过，2-审核不通过")
    private Integer state;

    @ApiModelProperty("用户ID")
    private Integer uid;
}
