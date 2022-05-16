package com.px.pa.modulars.points.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.modulars.vo.RankingItemQueryParam;
import com.px.pa.modulars.vo.result.SzRecordSimpleResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
@Api(value = "api/points", tags = "积分相关服务")
public class PointsApiController extends BaseApiController {
    @Autowired
    private SzUserService userService;
    @Autowired
    private SysDeptService deptService;

    @ApiOperation(value = "本村积分", notes = "本村积分")
    @GetMapping("/read/currentVillage")
    public R readCurrentVillage() {
        Integer currentId = super.getCurrentId();
        SzUser user = this.userService.getById(currentId);
        SysDept dept = this.readDept(user.getDid());
        return R.ok(dept.getAllScore());
    }

    private SysDept readDept(Integer id) {
        SysDept dept = this.deptService.getById(id);
        //如果上级是大河塔镇
        if (dept.getParentId() == 1) {
            return dept;
        }
        return this.readDept(dept.getParentId());
    }
}
