package com.px.pa.modulars.suggest.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import com.pig4cloud.pig.common.core.support.cache.RedisHelper;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.pa.constants.SuggestTypeEnum;
import com.px.pa.modulars.core.entity.SzGuide;
import com.px.pa.modulars.core.entity.SzGuideFlow;
import com.px.pa.modulars.core.entity.SzGuideRecord;
import com.px.pa.modulars.core.entity.SzSuggest;
import com.px.pa.modulars.core.service.*;
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
@RequestMapping("/api/suggest")
@RestController
@RequiredArgsConstructor
@Api(value = "/api/suggest", tags = "需求")
public class SuggestApiController extends BaseApiController {
    @Autowired
    private SzSuggestService suggestService;

    @PutMapping("/list")
    @ApiOperation("读取公示列表")
    public R list(@RequestBody InfoQueryParam param) {
        Page page = BaseQueryToPageUtil.createPage(param);
        SzSuggest p=new SzSuggest();
        p.setType(SuggestTypeEnum.REQUIREMENT.getValue());
        p.setTitle(param.getKw());
        p.setState(1);
        return R.ok(suggestService.getPage(page,p));
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("读取需求详情")
    public R detail(@PathVariable("id") Integer id) {
        if (id != null) {
            return R.ok(suggestService.getDetail(id,SuggestTypeEnum.REQUIREMENT.getValue()));
        } else {
            return R.failed("参数错误");
        }
    }

    @PostMapping("/save")
    @ApiOperation("提交需求")
    public R save(@RequestBody SzSuggest record) {
        if (record != null) {
            Integer userId = super.getCurrentId();
            record.setUid(userId);
            record.setType(SuggestTypeEnum.REQUIREMENT.getValue());
            suggestService.save(record);
            return R.ok("提交成功");
        } else {
            return R.failed("参数错误");
        }
    }

    @PostMapping("/mylist")
    @ApiOperation("办事指南记录")
    public R mylist(@RequestBody BasePageParamVo param) {
        Integer userId = super.getCurrentId();
        Page page = BaseQueryToPageUtil.createPage(param);
        SzSuggest p=new SzSuggest();
        p.setType(SuggestTypeEnum.REQUIREMENT.getValue());
        p.setUid(userId);
        return R.ok(suggestService.getPage(page,p));
    }

}
