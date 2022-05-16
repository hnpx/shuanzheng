package com.px.pa.modulars.home.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.pa.modulars.core.entity.*;
import com.px.pa.modulars.core.service.*;
import com.px.pa.modulars.home.service.HomeApiService;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.utils.bean.BaseQueryToPageUtil;
import com.px.pa.utils.bean.BeanToResultUtil;
import com.px.pa.vo.param.InfoQueryParam;
import com.px.pa.vo.result.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
@Api(value = "api/home", tags = "首页相关的服务接口")
public class HomeApiController extends BaseApiController {
    @Autowired
    private HomeApiService homeApiService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SzFarmService farmService;
    @Autowired
    private SzInfoService infoService;
    @Autowired
    private SzNoticeService noticeService;
    @Autowired
    private SzOrgCateService orgCateService;
    @Autowired
    private SzOrgContentService orgContentService;
    @Autowired
    private SzServiceService serviceService;


    @GetMapping("/read")
    @ApiOperation("读取首页信息")
    public R read() {
        HomeInfoResult homeInfoResult = this.homeApiService.queryHomeInfo();
        return R.ok(homeInfoResult);
    }

    @GetMapping("/read/info")
    @ApiOperation("读取首页信息")
    public R readInfo() {
        SzInfo info = this.infoService.get();
        return R.ok(info);
    }

    /**
     * @return
     */
    @PutMapping("/read/notices")
    @ApiOperation("读取公告信息")
    public R queryNotice(@RequestBody InfoQueryParam param) {
        Page<SzNotice> page = BaseQueryToPageUtil.createPage(param);
        page = this.noticeService.lambdaQuery().like(StrUtil.isNotBlank(param.getKw()), SzNotice::getName, param.getKw()).orderByDesc(SzNotice::getSort, SzNotice::getUpdateTime, SzNotice::getCreateTime).page(page);
        Page<SzNoticeResult> pageResult = BeanToResultUtil.beanToResult(page, SzNoticeResult.class);
        return R.ok(pageResult);
    }

    /**
     * @return
     */
    @GetMapping("/read/notices/detail/{id}")
    @ApiOperation("读取公布信息的详细数据")
    public R readNoticeDetail(@PathVariable("id") Integer id) {
        SzNotice szNotice = noticeService.getById(id);
        if (szNotice.getViews() == 0 || szNotice.getViews() != null) {
            szNotice.setViews(1);
        } else {
            szNotice.setViews(szNotice.getViews() + 1);
        }
        noticeService.updateById(szNotice);
        SzNotice notice = this.noticeService.detail(id);
        return R.ok(notice);
    }

    /**
     * @return
     */
    @GetMapping("/read/farmInfo")
    @ApiOperation("读取公告信息")
    public R readFarmInfo() {
        FarmInfoResult farmInfoResult = this.homeApiService.queryFarmInfo();
        return R.ok(farmInfoResult);
    }

    /**
     * @return
     */
    @PutMapping("/read/farm/cm")
    @ApiOperation("读取村民信息")
    public R queryFarmCm(@RequestBody InfoQueryParam param) {
        Page<SzFarm> page = BaseQueryToPageUtil.createPage(param);
        page = this.farmService.lambdaQuery().like(StrUtil.isNotBlank(param.getKw()), SzFarm::getName, param.getKw()).eq(SzFarm::getCid, SzFarm.cid_cm).orderByDesc(SzFarm::getSort, SzFarm::getUpdateTime, SzFarm::getCreateTime).page(page);
        Page<SzFarmResult> pageResult = BeanToResultUtil.beanToResult(page, SzFarmResult.class);
        return R.ok(pageResult);
    }

    /**
     * @return
     */
    @PutMapping("/read/farm/zc")
    @ApiOperation("读取村民信息")
    public R queryFarmZc(@RequestBody InfoQueryParam param) {
        Page<SzFarm> page = BaseQueryToPageUtil.createPage(param);
        page = this.farmService.lambdaQuery().like(StrUtil.isNotBlank(param.getKw()), SzFarm::getName, param.getKw()).eq(SzFarm::getCid, SzFarm.cid_zc).orderByDesc(SzFarm::getSort, SzFarm::getUpdateTime, SzFarm::getCreateTime).page(page);
        Page<SzFarmResult> pageResult = BeanToResultUtil.beanToResult(page, SzFarmResult.class);
        return R.ok(pageResult);
    }

    /**
     * @return
     */
    @GetMapping("/read/farm/detail/{id}")
    @ApiOperation("读取村民信息")
    public R queryFarmZc(@PathVariable("id") Integer id) {
        SzFarm farm = this.farmService.detail(id);
        return R.ok(farm);
    }


    /**
     * @return
     */
    @PutMapping("/read/orgByCate/{id}")
    @ApiOperation("读取某个栏目的信息列表")
    public R queryOrgByCate(@RequestBody InfoQueryParam param, @PathVariable("id") Integer id) {
        Page<SzOrgContent> page = BaseQueryToPageUtil.createPage(param);
        page = this.orgContentService.lambdaQuery().like(StrUtil.isNotBlank(param.getKw()), SzOrgContent::getName, param.getKw())
                .eq(SzOrgContent::getCid, id).orderByDesc(SzOrgContent::getSort, SzOrgContent::getUpdateTime, SzOrgContent::getCreateTime).page(page);

        Page<SzOrgContent> pageResult = BeanToResultUtil.beanToResult(page, SzOrgContent.class);
        return R.ok(pageResult);
    }

    /**
     * @return
     */
    @GetMapping("/read/org/detail/{id}")
    @ApiOperation("读取政务公开信息详情")
    public R readOrgDetail(@PathVariable("id") Integer id) {
        //更新浏览量
        SzOrgContent szOrgContent = orgContentService.getById(id);
        if (szOrgContent.getViews() == null || szOrgContent.getViews() == 0) {
            szOrgContent.setViews(1);
        } else {
            szOrgContent.setViews(szOrgContent.getViews() + 1);
        }
        orgContentService.updateById(szOrgContent);
        SzOrgContent content = this.orgContentService.detail(id);
        return R.ok(content);
    }

    /**
     * @return
     */
    @GetMapping("/read/dept/detail/{id}")
    @ApiOperation("读取乡村详情")
    public R readDeptDetail(@PathVariable("id") Integer id) {
        //更新浏览量
        DeptInfoResult result = this.homeApiService.readDeptDetail(id);
        return R.ok(result);
    }

    @GetMapping("/read/service")
    @ApiOperation("便民服务")
    public R service() {
        return R.ok(serviceService.lambdaQuery()
                .eq(SzService::getState,1)
                .orderByDesc(SzService::getSort)
                .list());
    }
}
