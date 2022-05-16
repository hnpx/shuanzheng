/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.px.pa.modulars.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.modulars.core.entity.SzTaskCate;
import com.px.pa.modulars.core.mapper.SzTaskCateMapper;
import com.px.pa.modulars.core.service.SzTaskCateService;
import com.px.pa.modulars.upms.dto.DeptTree;
import com.px.pa.modulars.upms.dto.TaskCateTree;
import com.px.pa.modulars.upms.dto.TreeNode;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.vo.TreeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务分类
 *
 * @author pig code generator
 * @date 2021-04-23 17:14:21
 */
@Service
@Transactional
public class SzTaskCateServiceImpl extends ServiceImpl<SzTaskCateMapper, SzTaskCate> implements SzTaskCateService {

    @Override
    public List<TaskCateTree> cateTree(Integer type,Integer parentId) {
        List<SzTaskCate> cates = super.lambdaQuery()
                .eq(SzTaskCate::getDelFlag, 0)
                .eq(type != null, SzTaskCate::getType, type)
                .and(szTaskCateLambdaQueryWrapper -> szTaskCateLambdaQueryWrapper
                        .eq(SzTaskCate::getDelFlag,0)
                        .eq(parentId != null,SzTaskCate::getId,parentId)
                        .or()
                        .eq(parentId != null,SzTaskCate::getParentId,parentId)
                )
                .orderByDesc(SzTaskCate::getSort, SzTaskCate::getUpdateTime, SzTaskCate::getCreateTime).list();
        List<TaskCateTree> treeList = cates.stream().filter(dept -> !dept.getId().equals(dept.getParentId()))
                .sorted(Comparator.comparingInt(SzTaskCate::getSort)).map(dept -> {
                    TaskCateTree node = new TaskCateTree();
                    node.setId(dept.getId());
                    node.setParentId(dept.getParentId());
                    node.setName(dept.getName());
                    node.setState(dept.getState());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(treeList, 0);
    }
}
