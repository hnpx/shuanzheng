package com.px.pa.modulars.user.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.pa.modulars.core.entity.SzSuggest;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.service.SzSuggestService;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.vo.SzSuggestQueryParam;
import com.px.pa.utils.bean.BaseQueryToPageUtil;
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
@RequestMapping("/api/user/suggest")
@Api(value = "api/user/suggest", tags = "村民需求服务")
public class UserSuggestApiController extends BaseApiController {
    @Autowired
    private SzSuggestService suggestService;
    @Autowired
    private SzUserService userService;

    /**
     * 发布
     *
     * @return
     */
    @ApiOperation(value = "发布需求", notes = "发布需求")
    @PostMapping("/release")
    public R release(@RequestBody SzSuggest param) {
        param.setState(0);
        param.setDelFlag("0");
        param.setCreateTime(LocalDateTime.now());
        param.setUpdateTime(param.getCreateTime());
        param.setUid(super.getCurrentId());
//        param.setUid(1);
//        SzUser user=this.userService.getById(param.getUid());
//        param.setUname(user.getName());
        this.suggestService.save(param);
        return R.ok();
    }

    @ApiOperation(value = "查询需求", notes = "查询需求")
    @PutMapping("/read/list")
    public R queryByUser(@RequestBody SzSuggestQueryParam param) {
        SzSuggest queryParam = new SzSuggest();
        BeanUtil.copyProperties(param, queryParam);
        queryParam.setUid(super.getCurrentId());
        queryParam.setType(param.getType());
        queryParam.setState(param.getState());
//        queryParam.setUid(1);il
        Page<SzSuggest> page = BaseQueryToPageUtil.createPage(param);
        page = this.suggestService.getPage(page, queryParam);
        return R.ok(page);
    }

    @ApiOperation(value = "删除需求", notes = "删除   需求")
    @GetMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer id) {
        SzSuggest ss = this.suggestService.getById(id);
        Integer userId = super.getCurrentId();
        if (!userId.equals(ss.getUid())) {
            return R.failed("您没有删除该需求的权限");
        } else {
            this.suggestService.removeById(id);
            return R.ok();
        }
    }


}
