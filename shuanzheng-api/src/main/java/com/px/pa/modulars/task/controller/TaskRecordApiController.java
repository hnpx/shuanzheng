package com.px.pa.modulars.task.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.pa.modulars.core.entity.SzTask;
import com.px.pa.modulars.core.entity.SzTaskRecord;
import com.px.pa.modulars.core.service.SzTaskRecordService;
import com.px.pa.modulars.core.service.SzTaskService;
import com.px.pa.modulars.vo.TaskQueryParam;
import com.px.pa.modulars.vo.TaskRecordParam;
import com.px.pa.modulars.vo.TaskRecordQueryParam;
import com.px.pa.modulars.vo.result.SzRecordSimpleResult;
import com.px.pa.modulars.vo.result.SzTaskDetailResult;
import com.px.pa.modulars.vo.result.SzTaskResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author zhouz
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task/record")
@Api(value = "api/task/record", tags = "任务完成记录")
public class TaskRecordApiController extends BaseApiController {
    @Autowired
    private SzTaskRecordService recordService;

    @PutMapping("/read/listByUser")
    @ApiOperation("查询某个用户的任务记录")
    public R queryByUser(@RequestBody TaskRecordQueryParam param) {
        Integer userId = super.getCurrentId();
        Page<SzRecordSimpleResult> page = this.recordService.queryByUserToSimple(userId, param);
        return R.ok(page);
    }

    @PutMapping("/read/list")
    @ApiOperation("查询任务完成记录")
    public R queryList(@RequestBody TaskRecordQueryParam param) {
        Page<SzRecordSimpleResult> page = this.recordService.queryToSimple(param);
        return R.ok(page);
    }

    @PutMapping("/read/fix_list")
    @ApiOperation("查询待整改记录")
    public R queryBadList(@RequestBody BasePageParamVo param) {
        Integer userId = super.getCurrentId();
        Page<SzRecordSimpleResult> page = this.recordService.queryBadList(userId,param);
        return R.ok(page);
    }


    @GetMapping("/read/detail/{id}")
    @ApiOperation("查询任务完成记录")
    public R readDetail(@PathVariable("id") Integer id) {
        SzTaskRecord record = this.recordService.getById(id);
        return R.ok(record);
    }

    @PostMapping("/result")
    @ApiOperation("提交任务结果")
    public R result(@RequestBody SzTaskRecord param) {
        if(param.getId() == null|| StrUtil.isEmpty(param.getResult())){
            return R.failed("参数错误");
        }
        this.recordService.lambdaUpdate()
                .eq(SzTaskRecord::getId,param.getId())
                .set(SzTaskRecord::getResult,param.getResult())
                .set(SzTaskRecord::getUpdateTime,LocalDateTime.now())
                .update();
        return R.ok();
    }
}
