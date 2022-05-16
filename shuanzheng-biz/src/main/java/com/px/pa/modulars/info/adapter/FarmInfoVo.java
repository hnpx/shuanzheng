package com.px.pa.modulars.info.adapter;

import lombok.Data;
import lombok.ToString;

/**
 * 用户导入的VO类
 * @author zhouz
 */
@Data
@ToString
public class FarmInfoVo {
    private String village;
    private String group;
    private String number;
    private String name;
    private String phone;
    private String sex;
    private String relation;
    private String idcard;
}
