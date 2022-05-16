package com.px.pa.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 * 支付订单类型
 *
 * @author zhouz
 */
public enum PayOrderTypeEnum implements BaseEnum {

    PLUGIN(1, "插件"), SMS(2, "短信服务");

    private Integer value;
    private String desc;

    private PayOrderTypeEnum(Integer value, String desc) {
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
