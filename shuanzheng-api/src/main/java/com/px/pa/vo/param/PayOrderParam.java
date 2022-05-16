package com.px.pa.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付订单创建参数
 *
 * @author zhouz
 */
@ApiModel(description = "支付订单创建参数")
@Data
public class PayOrderParam {

    /**
     * 参照PayOrderTypeEnum
     */
    @ApiModelProperty("订单类型")
    @NotNull(message = "请填写类型")
    private Integer type;

    @ApiModelProperty("费用")
    @NotNull(message = "请填写费用")
    private BigDecimal price;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("服务/插件ID")
    @NotNull(message = "请填写服务/插件ID")
    private Long serviceId;

    @ApiModelProperty("服务/插件名称")
    @NotNull(message = "请填写服务/插件名称")
    private String serviceName;

    @ApiModelProperty("用户OPENID")
    @NotNull(message = "请填OPENID")
    private String openId;

    @ApiModelProperty("用户IP")
    @NotNull(message = "请填用户IP")
    private String ip;

}
