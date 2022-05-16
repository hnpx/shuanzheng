package com.px.pa.modulars.vo.result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.pa.modulars.upms.entity.SysDept;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("榜单首页信息")
@Data
public class RankingHomeResult {
    @ApiModelProperty("前几名")
    private List<RankingItemResult> topItems;
    @ApiModelProperty("积分任务播报")
    private List<SzRecordSimpleResult> records;
    @ApiModelProperty("榜单记录信息")
    private Page<RankingItemResult> rankings;
    @ApiModelProperty("可以查看的所有村信息")
    private List<SysDept> depts;
}
