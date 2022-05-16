package com.px.pa.modulars.core.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class SzVoteItemVo {
    @Excel(name = "对象名", width = 12)
    private String dname;
    @Excel(name = "所在村", width = 12)
    private String vname;
    @Excel(name = "所在组", width = 12)
    private String gname;
    @Excel(name = "排名", width = 12)
    private Integer rank;
    @Excel(name = "票数", width = 12)
    private Integer num;
    @Excel(name = "编号", width = 12)
    private String number;


}
