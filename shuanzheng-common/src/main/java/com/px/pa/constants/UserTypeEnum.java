package com.px.pa.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 * 支付订单类型
 *
 * @author zhouz
 */
public enum UserTypeEnum implements BaseEnum {

    NORMAL(1, "村民"),
    LEADER(2, "村干部"),
    TOPLEADER(3, "包村干部"),
    AUDIT(4, "巡查员");

    private Integer value;
    private String desc;

    private UserTypeEnum(Integer value, String desc) {
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
