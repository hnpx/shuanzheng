package com.px.pa.modulars.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 兑换订单
 *
 * @author zhouz
 */
@ApiModel(description = "兑换订单")
@Data
public class ShopOrderParam {

    @ApiModelProperty(value="")
    private Integer id;

    @ApiModelProperty("兑换状态")
    private Integer state;

    @ApiModelProperty("商品分类id")
    private Integer cid;

    @ApiModelProperty("商品id")
    private Integer gid;

    @ApiModelProperty("商品名称")
    private String gname;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("核销员姓名")
    private String hname;

    @ApiModelProperty(value="花费积分")
    private Integer points;

    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @TableField(exist = false)
    private LocalDateTime endTime;

}
