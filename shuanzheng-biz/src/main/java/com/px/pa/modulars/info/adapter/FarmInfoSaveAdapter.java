package com.px.pa.modulars.info.adapter;

import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.plugins.conversion.core.adapter.AbsFileSaveAdapter;
import com.px.plugins.conversion.core.entity.ConversionTaskEntity;
import com.px.plugins.conversion.core.vo.SaveErrorsResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 村民信息导入处理
 *
 * @author zhouz
 */
@Component("farmInfoSaveAdapter")
@Slf4j
public class FarmInfoSaveAdapter extends AbsFileSaveAdapter<FarmInfoVo> {
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SzUserService userService;

    @Override
    protected ConversionTaskEntity start(ConversionTaskEntity conversionTaskEntity) {
        //TODO 任务开始
        return conversionTaskEntity;
    }

    @Override
    protected void end(ConversionTaskEntity conversionTaskEntity) {
        //TODO 任务结束
    }

    @Override
    protected SaveErrorsResult saveData(List<FarmInfoVo> list, String s, Map<String, Object> map) {
        SaveErrorsResult result = new SaveErrorsResult();
        list.forEach(farm -> {
            if (farm.getVillage() == null) {
                return;
            }
            SysDept village = this.getOrCreate(farm.getVillage(), 1);
            SysDept group = this.getOrCreate(farm.getGroup(), village.getDeptId());
            try {
                SzUser user = this.userService.lambdaQuery().eq(SzUser::getDid, village.getDeptId()).eq(SzUser::getCdid, group.getDeptId()).eq(SzUser::getName, farm.getName()).eq(SzUser::getCode, farm.getNumber()).one();
                if (user == null) {
                    user = new SzUser();
                    user.setName(farm.getName());
                    user.setPhone(farm.getPhone());
                    user.setDid(village.getDeptId());
                    user.setCdid(group.getDeptId());
                    user.setCode(farm.getNumber());
                    user.setRelation(farm.getRelation());
                    user.setSex(farm.getSex().equals("男") ? 1 : 2);
                    //户主可以登录
                    user.setLogin(farm.getRelation().equals("户主") ? 1 : 0);
                    //村民
                    user.setRole(1);
                    user.setIdcard(farm.getIdcard());
                    this.userService.save(user);
                } else {
                    user.setCode(farm.getNumber());
                    user.setPhone(farm.getPhone());//更新手机号。
                    this.userService.updateById(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    private SysDept getOrCreate(String name, Integer parentId) {
        SysDept dept = this.deptService.lambdaQuery().eq(SysDept::getName, name).eq(SysDept::getDelFlag, 0).eq(SysDept::getParentId, parentId).one();
        //如果没有，就新增
        if (dept == null) {
            dept = new SysDept();
            dept.setName(name);
            //大河塔镇
            dept.setParentId(parentId);
            dept.setUpdateTime(LocalDateTime.now());
            this.deptService.saveDept(dept);
        }
        return dept;
    }

    @Override
    protected String getKey() {
        return "farminfo";
    }

    @Override
    protected Class<FarmInfoVo> getClassT() {
        return FarmInfoVo.class;
    }
}
