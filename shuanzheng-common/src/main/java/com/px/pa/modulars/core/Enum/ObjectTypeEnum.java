package com.px.pa.modulars.core.Enum;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 * 投票对象（2.村庄1.村民）
 *
 * @author zhouz
 */
public enum ObjectTypeEnum implements BaseEnum {

    ObjectTypeEnumONE(1, "村民"),
    ObjectTypeEnumTWO(2, "村庄");

    private Integer value;
    private String desc;

    private ObjectTypeEnum(Integer value, String desc) {
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
