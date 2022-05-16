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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.modulars.upms.entity.SysUserDept;
import com.px.pa.modulars.upms.entity.SysUserRole;
import com.px.pa.modulars.upms.mapper.SysUserDeptMapper;
import com.px.pa.modulars.upms.mapper.SysUserRoleMapper;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.modulars.upms.service.SysUserDeptService;
import com.px.pa.modulars.upms.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Service
@Transactional
public class SysUserDeptServiceImpl extends ServiceImpl<SysUserDeptMapper, SysUserDept> implements SysUserDeptService {


    @Override
    public boolean saveDept(Integer uid, String did,Integer pid) {
        Map<String,Object> map=new HashMap();
        map.put("user_id", uid);
        super.removeByMap(map);
        String [] ids=did.split(",");
        for(String d:ids){
            SysUserDept ud=new SysUserDept();
            ud.setUserId(uid);
            if(pid!=null){
                ud.setDeptId(pid);
                ud.setChildId(Integer.valueOf(d));
            }else{
                ud.setDeptId(Integer.valueOf(d));
            }
            super.save(ud);
        }
        return true;
    }
}
