package com.px.pa.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 * 支付订单类型
 *
 * @author zhouz
 */
public enum PeriodEnum implements BaseEnum {

    DAY(1, "日"),
    WEEK(2, "周"),
    MONTH(3, "月"),
    SEASON(4, "季"),
    YEAR(5, "年");

    private Integer value;
    private String desc;

    private PeriodEnum(Integer value, String desc) {
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
