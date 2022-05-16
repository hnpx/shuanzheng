package com.px.pa.modulars.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhouz
 */
@ApiModel("积分兑换记录信息")
@Data
public class SzOrderResult {

    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;
    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Integer cid;
    /**
     * 所需积分
     */
    @ApiModelProperty(value = "所需积分")
    private Integer points;


    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Integer gid;
    /**
     * 状态：0-待核销，1-已核销，2-核销失败
     */
    @ApiModelProperty(value = "状态：0-待核销，1-已核销，2-核销失败")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "核销时间")
    private LocalDateTime updateTime;
}
