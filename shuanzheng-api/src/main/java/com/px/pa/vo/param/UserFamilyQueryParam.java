package com.px.pa.vo.param;

import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import lombok.Data;

/**
 * 用户家庭成员查询
 */
@Data
public class UserFamilyQueryParam extends BasePageParamVo {

    private Integer uid;
    private String key;
}
