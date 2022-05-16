package com.px.pa.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 首页信息
 * @author zhouz
 */
@ApiModel("首页信息")
@Data
public class HomeInfoResult {
    @ApiModelProperty("栓正释义")
    private SzInfoResult info;
    @ApiModelProperty("栓正公告")
    private List<SzNoticeResult> notices;
    @ApiModelProperty("栓正村庄介绍")
    private SzDeptResult rootDept;
    @ApiModelProperty("栓正优秀村户")
    private List<SzFarmResult> farms;
    @ApiModelProperty("栓正政务公开")
    private List<SzOrgCateResult> cates;
}
