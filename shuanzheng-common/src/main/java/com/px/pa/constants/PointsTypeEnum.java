package com.px.pa.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 * 支付订单类型
 *
 * @author zhouz
 */
public enum PointsTypeEnum implements BaseEnum {

    VOTE(1, "投票积分"),
    SYS(2, "系统操作"),
    ORDER(3, "订单积分"),
    TASK(4, "任务积分");

    private Integer value;
    private String desc;

    private PointsTypeEnum(Integer value, String desc) {
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
