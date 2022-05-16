package com.px.pa.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 * 是否删除  -1：已删除  0：正常
 *
 * @author zhouz
 */
public enum VoteStatusEnum implements BaseEnum {

    VOTE_STATUS_ENUM_ONE(1, "可以投"),
    VOTE_STATUS_ENUM_TWO(2, "不可以投");


    private Integer value;
    private String desc;

    private VoteStatusEnum(Integer value, String desc) {
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
