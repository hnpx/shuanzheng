package com.px.pa.modulars.upms.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class LoginVo {
    private String phone;
    private String code;
    private String openId;
    private String nickName;
    private String avatarUrl;
}
