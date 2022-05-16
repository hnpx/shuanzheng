package com.px.pa.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 *
 *
 * @author zhouz
 */
public enum TimeStatusEnum implements BaseEnum {

    TIME_STATUS_ENUM_ONE(1, "未开始"),
    TIME_STATUS_ENUM_TWO(2, "进行中"),
    TIME_STATUS_ENUM_THREE(3, "已结束");


    private Integer value;
    private String desc;

    private TimeStatusEnum(Integer value, String desc) {
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
