package com.px.pa.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 * 是否删除  -1：已删除  0：正常
 *
 * @author zhouz
 */
public enum DelFlagEnum implements BaseEnum {

    DEL_FLAG_ENUM_ONE(1, "已删除"),
    DEL_FLAG_ENUM_TWO(0, "正常");

    private Integer value;
    private String desc;

    private DelFlagEnum(Integer value, String desc) {
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
