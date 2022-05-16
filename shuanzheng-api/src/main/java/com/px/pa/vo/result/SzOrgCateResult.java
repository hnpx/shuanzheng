package com.px.pa.vo.result;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SzOrgCateResult {

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private Integer id;
    /**
     * 政务分类名称
     */
    @ApiModelProperty(value = "政务分类名称")
    private String name;
    @ApiModelProperty("信息列表")
    List<SzOrgContentResult> contents;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
