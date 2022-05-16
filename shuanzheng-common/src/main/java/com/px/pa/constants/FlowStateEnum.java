package com.px.pa.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 * 支付订单类型
 *
 * @author zhouz
 */
public enum FlowStateEnum implements BaseEnum {

    WAIT(0, "待审核"),
    PROCESS(1, "审核中"),
    CONFIRM(2, "审核通过"),
    REJECT(3, "审核不通过");

    private Integer value;
    private String desc;

    private FlowStateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
