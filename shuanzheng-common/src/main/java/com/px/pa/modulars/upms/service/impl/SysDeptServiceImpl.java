/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.px.pa.modulars.upms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.pa.modulars.upms.dto.DeptTree;
import com.px.pa.modulars.upms.dto.TreeNode;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.entity.SysDeptRelation;
import com.px.pa.modulars.upms.vo.TreeUtil;
import com.px.pa.modulars.upms.mapper.SysDeptMapper;
import com.px.pa.modulars.upms.service.SysDeptRelationService;
import com.px.pa.modulars.upms.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final SysDeptRelationService sysDeptRelationService;

    /**
     * 添加信息部门
     *
     * @param dept 部门
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDept(SysDept dept) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept, sysDept);
        this.save(sysDept);
        sysDeptRelationService.saveDeptRelation(sysDept);
        return Boolean.TRUE;
    }

    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDeptById(Integer id) {
        // 级联删除部门
        List<Integer> idList = sysDeptRelationService
                .list(Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getAncestor, id)).stream()
                .map(SysDeptRelation::getDescendant).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }

        // 删除部门级联关系
        sysDeptRelationService.removeDeptRelationById(id);
        return Boolean.TRUE;
    }

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDeptById(SysDept sysDept) {
        // 更新部门状态
        this.updateById(sysDept);
        // 更新部门关系
        SysDeptRelation relation = new SysDeptRelation();
        relation.setAncestor(sysDept.getParentId());
        relation.setDescendant(sysDept.getDeptId());
        sysDeptRelationService.updateDeptRelation(relation);
        return Boolean.TRUE;
    }

    @Override
    public List<SysDept> getVillages() {
        return super.lambdaQuery()
                .eq(SysDept::getParentId, 1)
                .eq(SysDept::getDelFlag, 0)
                .orderByAsc(SysDept::getSort)
                .list();
    }

    @Override
    public List<DeptTree> getVillagesTree() {
        List<SysDept> depts = super.lambdaQuery()
                .select(SysDept::getName, SysDept::getDeptId, SysDept::getParentId, SysDept::getSort)
                .ne(SysDept::getParentId, 0)
                .eq(SysDept::getDelFlag, 0)
                .list();
        return getDeptTree(depts, 1);
    }

    @Override
    public List<DeptTree> getAreas(List<Integer> pid, Integer cid) {
        List<SysDept> depts=super.lambdaQuery()
                .select(SysDept::getName, SysDept::getDeptId, SysDept::getParentId, SysDept::getSort)
                .in(SysDept::getDeptId,pid)
                .or()
                .eq(cid!=null,SysDept::getDeptId,cid)
                .in(cid==null,SysDept::getParentId,pid)
                .list();
        return getDeptTree(depts, 1);
    }

    /**
     * 查询全部部门树
     *
     * @return 树
     */
    @Override
    public List<DeptTree> listDeptTrees() {
        List<DeptTree> depts = getDeptTree(this.list(Wrappers.emptyWrapper()));
        return depts;
    }


    /**
     * 查询用户部门树
     *
     * @return
     */
    @Override
    public List<DeptTree> listCurrentUserDeptTrees() {
        Integer deptId = SecurityUtils.getUser().getDeptId();
        List<Integer> descendantIdList = sysDeptRelationService
                .list(Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getAncestor, deptId)).stream()
                .map(SysDeptRelation::getDescendant).collect(Collectors.toList());

        List<SysDept> deptList = baseMapper.selectBatchIds(descendantIdList);
        return getDeptTree(deptList);
    }

    /**
     * 构建部门树
     *
     * @param depts 部门
     * @return
     */
    private List<DeptTree> getDeptTree(List<SysDept> depts, Object root) {
        List<DeptTree> treeList = depts.stream().filter(dept -> !dept.getDeptId().equals(dept.getParentId()))
                .sorted(Comparator.comparingInt(SysDept::getSort)).map(dept -> {
                    DeptTree node = new DeptTree();
                    node.setId(dept.getDeptId());
                    node.setParentId(dept.getParentId());
                    node.setName(dept.getName());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(treeList, root);
    }

    private List<DeptTree> getDeptTree(List<SysDept> depts) {
        return getDeptTree(depts, 0);
    }

}
