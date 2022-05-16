package com.px.pa.modulars.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 */
@ApiModel("榜单项信息")
@Data
public class RankingItemResult {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("头像")
    private String photo;
    @ApiModelProperty("积分")
    private Integer score;
    @ApiModelProperty("星星数量")
    private Integer num;
    @ApiModelProperty("任务范围：1-户，2-村，3-企业")
    private Integer area;
    @ApiModelProperty("所在村")
    private String deptName;
    @ApiModelProperty("所在村")
    private Integer did;
}
