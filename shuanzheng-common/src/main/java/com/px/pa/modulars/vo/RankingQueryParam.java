package com.px.pa.modulars.vo;


import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouz
 */
@ApiModel("榜单查询")
@Data
public class RankingQueryParam extends BasePageParamVo {
    @ApiModelProperty("榜单类型，1、居民；2、驻村干部；3、村庄榜；4、村干部榜；5、企业榜")
    private Integer type;
    @ApiModelProperty("统计年份")
    private Integer year;
    @ApiModelProperty("所属村庄")
    private Integer dept;

    private List<Integer> skipIds=new ArrayList<>();

    public void addSkip(Integer id){
        this.skipIds.add(id);
    }


}
