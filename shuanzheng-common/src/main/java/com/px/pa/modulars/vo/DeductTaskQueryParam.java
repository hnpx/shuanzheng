package com.px.pa.modulars.vo;

import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import lombok.Data;

/**
 * 信息查询
 *
 * @author zhouz
 */
@Data
public class DeductTaskQueryParam extends BasePageParamVo {

    //1-已回复，0-未回复
    //1-清除，0-扣分
    private Integer type;
    private String msg;
    private Integer id;

}
