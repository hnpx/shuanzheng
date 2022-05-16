package com.px.pa.modulars.guide.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import com.pig4cloud.pig.common.core.support.cache.RedisHelper;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.pa.modulars.core.entity.SzGuide;
import com.px.pa.modulars.core.entity.SzGuideFlow;
import com.px.pa.modulars.core.entity.SzGuideRecord;
import com.px.pa.modulars.core.service.*;
import com.px.pa.rule.NoRepeatSubmit;
import com.px.pa.utils.bean.BaseQueryToPageUtil;
import com.px.pa.vo.param.GuideQueryParam;
import com.px.pa.vo.param.InfoQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 商城信息服务
 *
 * @author zhouz
 */
@RequestMapping("/api/guide")
@RestController
@RequiredArgsConstructor
@Api(value = "/api/guide", tags = "办事指南")
public class GuideApiController extends BaseApiController {
    @Autowired
    private SzUserService userService;
    @Autowired
    private SzGuideService guideService;
    @Autowired
    private SzGuideRecordService recordService;
    @Autowired
    private SzGuideFlowService flowService;
    @Autowired
    private RedisHelper redisHelper;

    @PutMapping("/list")
    @ApiOperation("读取办事指南列表")
    public R list(@RequestBody InfoQueryParam param) {
        List<SzGuide> list = guideService.lambdaQuery()
                .select(SzGuide::getId, SzGuide::getName)
                .like(StrUtil.isNotEmpty(param.getKw()), SzGuide::getName, param.getKw())
                .eq(SzGuide::getState, 1)
                .orderByDesc(SzGuide::getSort, SzGuide::getId).list();
        return R.ok(list);
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("读取办事指南列表")
    public R detail(@PathVariable("id") Integer id) {
        if (id != null) {
            Integer userId = super.getCurrentId();
            Integer did = userService.getById(userId).getDid();
            SzGuide obj = guideService.getById(id);
            obj.setUsers(guideService.getUsers(id, did));
            return R.ok(obj);
        } else {
            return R.failed("参数错误");
        }
    }

    @PostMapping("/save")
    @ApiOperation("提交办事指南")
    @NoRepeatSubmit()
    public R save(@RequestBody SzGuideRecord record) {
        if (record != null && record.getGid() != null) {
            Integer userId = super.getCurrentId();
            record.setUid(userId);
            String date=DatePattern.PURE_DATE_FORMAT.format(new Date());
            Object index= this.redisHelper.get(date);
            Integer temp=1;
            if(index!=null){
                temp= (Integer) index;
                temp++;
            }
            this.redisHelper.set(date,temp,86400);
            record.setCode(date+StrUtil.padPre(temp+"", 5, "0"));
            recordService.save(record);
            Integer did = userService.getById(userId).getDid();
            Integer uid = guideService.getFirstUser(record.getGid(), did);
            SzGuideFlow flow = new SzGuideFlow();
            flow.setRid(record.getId());
            flow.setUid(uid);
            flowService.save(flow);
            return R.ok("保存成功");
        } else {
            return R.failed("参数错误");
        }
    }

    @PostMapping("/mylist")
    @ApiOperation("办事指南记录")
    public R mylist(@RequestBody GuideQueryParam param) {
        Integer userId = super.getCurrentId();
        Page page = BaseQueryToPageUtil.createPage(param);
        SzGuideRecord record = new SzGuideRecord();
        record.setUid(userId);
        record.setState(param.getState());
        return R.ok(recordService.getPage(page, record));
    }

    @GetMapping("/flow/{id}")
    @ApiOperation("办事指南审核记录")
    public R flow(@PathVariable("id") Integer id) {
        SzGuideFlow szGuideFlow = new SzGuideFlow();
        szGuideFlow.setRid(id);
        return R.ok(flowService.flowList(szGuideFlow));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("办事指南详情")
    public R info(@PathVariable("id") Integer id) {
        SzGuideRecord r = recordService.getById(id);
        r.setGname(guideService.getById(r.getGid()).getName());
        r.setUser(userService.getById(r.getUid()));
        SzGuideFlow szGuideFlow = new SzGuideFlow();
        szGuideFlow.setRid(id);
        r.setFlow(flowService.flowList(szGuideFlow));
        return R.ok(r);
    }

    @PostMapping("/sh_list")
    @ApiOperation("审核办事指南列表")
    public R shList(@RequestBody GuideQueryParam param) {
        Integer userId = super.getCurrentId();
        Page page = BaseQueryToPageUtil.createPage(param);
        return R.ok(recordService.getPage(page, param.getState(), userId));
    }

    @PostMapping("/sh")
    @ApiOperation("审核办事指南")
    public R sh(@RequestBody SzGuideFlow flow) {
        Integer userId = super.getCurrentId();
        flow.setUid(userId);
        return R.ok(flowService.sh(flow));
    }

}
