package com.px.pa.modulars.core.vo;

import lombok.Data;

import java.sql.PreparedStatement;

@Data
public class UserVo {
    private Integer id;
    private String name;
    private String relation;
    private String idcard;
    private String phone;
    private String code;
    private Integer sex;
    private Integer did;
    private Integer cdid;
    private Integer login;
    private String avatar;
}
