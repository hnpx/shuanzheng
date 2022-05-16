package com.px.pa.modulars.task.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.pa.constants.OrgTypeEnum;
import com.px.pa.modulars.core.entity.SzTask;
import com.px.pa.modulars.core.entity.SzTaskRecord;
import com.px.pa.modulars.core.service.SzTaskCateService;
import com.px.pa.modulars.core.service.SzTaskRecordService;
import com.px.pa.modulars.core.service.SzTaskService;
import com.px.pa.modulars.upms.dto.TaskCateTree;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.entity.SysUserDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.modulars.upms.service.SysUserDeptService;
import com.px.pa.modulars.vo.TaskQueryParam;
import com.px.pa.modulars.vo.result.SzRecordSimpleResult;
import com.px.pa.modulars.vo.result.SzTaskDetailResult;
import com.px.pa.modulars.vo.result.SzTaskResult;
import com.px.pa.modulars.vo.DeductTaskQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhouz
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task/info")
@Api(value = "api/task/info", tags = "任务信息服务")
public class TaskInfoApiController extends BaseApiController {
    @Autowired
    private SzTaskService taskService;
    @Autowired
    private SzTaskCateService taskCateService;
    @Autowired
    private SzTaskRecordService recordService;
    @Autowired
    private SysUserDeptService userDeptService;
    @Autowired
    private SysDeptService deptService;


    @GetMapping("/read/isRectified")
    @ApiOperation("查询某个用户的待整改任务记录")
    public R queryIsRectifiedByUser() {
        Integer userId = super.getCurrentId();
        Integer num=this.recordService.queryIsRectified(userId,SzTask.AREA_H);
        return R.ok(num);
    }


    @GetMapping("/read/rectified")
    @ApiOperation("查询某个用户的待整改任务记录")
    public R queryRectifiedByUser() {
        Integer userId = super.getCurrentId();
        List<SzRecordSimpleResult> result=this.recordService.queryRectified(userId,SzTask.AREA_H);
        return R.ok(result);
    }

    @GetMapping("/read/cates/{type}")
    @ApiOperation("查询某个用户的任务记录")
    public R queryTaskCate(@PathVariable("type") Integer type) {
        List<TaskCateTree> tree = this.taskCateService.cateTree(type, null);
        return R.ok(tree);
    }

    @PutMapping("/read/list")
    @ApiOperation("查询某个用户的任务记录")
    public R queryByUser(@RequestBody TaskQueryParam param) {
        Integer userId = super.getCurrentId();
        Page<SzTaskResult> page = this.taskService.queryByUser(userId, param);
        return R.ok(page);
    }

    @GetMapping("/read/detail/{id}")
    @ApiOperation("查看某个任务详情")
    public R readDetail(@PathVariable("id") Integer id,Integer uid) {
        Integer userId = super.getCurrentId();
        SzTaskDetailResult detail = this.taskService.readDetail(userId, id, uid);
        return R.ok(detail);
    }

    @PostMapping("/deduct_finish")
    @ApiOperation("提交扣分任务")
    public R deductFinish(@RequestBody SzTaskRecord param) {
        Integer userId = super.getCurrentId();
        SzTask task = this.taskService.getById(param.getTid());
        if(task.getState()!=1){
            return R.failed("任务未开始或已结束");
        }
        Integer count;
        count=recordService.lambdaQuery()
                .eq(SzTaskRecord::getTid,param.getTid())
                .eq(SzTaskRecord::getDelFlag,0)
                .count();
        if(task.getAllNum()!=0&&count>=task.getAllNum()){
            return R.failed("任务总完成次数已达上限");
        }
        if(task.getSingleNum()!=0&&taskService.checkNum(param.getUid(),task,OrgTypeEnum.HOUSE.getValue())>=task.getSingleNum()){
            return R.failed("单个周期内完成次数已达上限");
        }
        param.setArea(task.getArea());
        param.setHelp(userId);
        param.setXcx(1);
        param.setState(SzTaskRecord.STATUS_DSH);
        param.setDelFlag("0");
        param.setImage(param.getResult());
        param.setResult(null);
        param.setPoints(-task.getPoints());
        param.setCreateTime(LocalDateTime.now());
        param.setUpdateTime(param.getCreateTime());
        this.recordService.save(param);
        return R.ok();
    }

    @PostMapping("/finish")
    @ApiOperation("完成任务")
    public R finish(@RequestBody SzTaskRecord param) {
        Integer userId = super.getCurrentId();
        SzTask task = this.taskService.getById(param.getTid());
        if(task.getState()!=1){
            return R.failed("任务未开始或已结束");
        }
        Integer count;
        count=recordService.lambdaQuery()
                .eq(SzTaskRecord::getTid,param.getTid())
                .eq(SzTaskRecord::getDelFlag,0)
                .ne(SzTaskRecord::getState,2)
                .count();
        if(task.getAllNum()!=0&&count>=task.getAllNum()){
            return R.failed("任务总完成次数已达上限");
        }
        param.setArea(task.getArea());
        if(task.getArea().equals(OrgTypeEnum.VILLAGE.getValue())){
            Integer deptid=userDeptService.lambdaQuery()
                    .eq(SysUserDept::getUserId,userId)
                    .last("limit 1")
                    .one().getDeptId();
            deptid=deptService.lambdaQuery().eq(SysDept::getDeptId,deptid).one().getParentId();
            if(task.getSingleNum()!=0&&taskService.checkNum(deptid,task,OrgTypeEnum.VILLAGE.getValue())>=task.getSingleNum()){
                return R.failed("单个周期内完成次数已达上限");
            }
            param.setHelp(userId);
            param.setUid(deptid);
        }else{
            if(task.getSingleNum()!=0&&taskService.checkNum(userId,task,OrgTypeEnum.HOUSE.getValue())>=task.getSingleNum()){
                return R.failed("单个周期内完成次数已达上限");
            }
            param.setUid(userId);
        }
        param.setState(SzTaskRecord.STATUS_DSH);
        param.setDelFlag("0");
        param.setCreateTime(LocalDateTime.now());
        param.setUpdateTime(param.getCreateTime());
        param.setPoints(task.getPoints());
        this.recordService.save(param);
        return R.ok();
    }

    //巡查员查看任务列表
    @PostMapping("/deduct_list")
    @ApiOperation("扣分任务完成记录")
    public R deductList(@RequestBody DeductTaskQueryParam param) {
        return R.ok(recordService.deductList(param));
    }

    //巡查员审核
    @PostMapping("/deduct_result")
    @ApiOperation("扣分任务审核")
    public R deductResult(@RequestBody DeductTaskQueryParam param) {
        Integer userId = super.getCurrentId();
        return R.ok(recordService.deductResult(param,userId));
    }
}
