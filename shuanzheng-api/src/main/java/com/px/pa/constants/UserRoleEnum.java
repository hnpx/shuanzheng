package com.px.pa.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 *
 *身份：1-村民，2-村干部，3-包村干部
 * @author zhouz
 */
public enum UserRoleEnum implements BaseEnum {

    USER_ROLE_ENUM_ONE(1, "村民"),
    USER_ROLE_ENUM_TWO(2, "村干部"),
    USER_ROLE_ENUM_THREE(3, "包村干部");

    private Integer value;
    private String desc;

    private UserRoleEnum(Integer value, String desc) {
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
