package com.px.pa.vo.param;

import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import lombok.Data;

/**
 * 信息查询
 *
 * @author zhouz
 */
@Data
public class InfoQueryParam extends BasePageParamVo {
    /**
     * 关键字查询
     */
    private String kw;

}
