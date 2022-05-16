package com.px.pa.modulars.upms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskCateTree extends TreeNode {

    private String name;
    private Integer state;
}
