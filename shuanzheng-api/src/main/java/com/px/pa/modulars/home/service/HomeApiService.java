package com.px.pa.modulars.home.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pig.common.core.support.cache.RedisHelper;
import com.px.pa.modulars.core.entity.*;
import com.px.pa.modulars.core.service.*;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.utils.bean.BeanToResultUtil;
import com.px.pa.vo.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class HomeApiService {
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
    private SzTaskService taskService;
    @Autowired
    private RedisHelper redisHelper;


    /**
     * 首页信息
     */
    @Value("${application.home.cache-key}")
    private String homeKey;
    @Value("${application.home.cache-timeout}")
    private Integer homeTimeout;
    @Value("${application.home.notice-len}")
    private Integer homeNoticeLen;
    @Value("${application.home.farm-len}")
    private Integer homeFarmLen;
    @Value("${application.home.org-cate-len}")
    private Integer homeOrgCateLen;
    @Value("${application.home.org-content-len}")
    private Integer homeOrgContentLen;

    /**
     * 获取首页信息
     *
     * @return
     */
    public HomeInfoResult queryHomeInfo() {
        //从缓存中读取，如果有的话，直接返回，如果没有，则查询
        Object hinfoObj = this.redisHelper.get(this.homeKey);
        if (hinfoObj != null) {
            String hinfoStr = hinfoObj.toString();
            HomeInfoResult result = JSONUtil.toBean(hinfoStr, HomeInfoResult.class);
            return result;
        }
        HomeInfoResult homeInfo = new HomeInfoResult();
        SzInfo szInfo = this.infoService.get();
        if (szInfo != null) {
            SzInfoResult result = BeanToResultUtil.beanToResult(szInfo, SzInfoResult.class);
            homeInfo.setInfo(result);
        }
//公告
        List<SzNotice> notices = this.noticeService.queryLimit(this.homeNoticeLen);
        List<SzNoticeResult> noticeResults = BeanToResultUtil.beanToResult(notices, SzNoticeResult.class);
        homeInfo.setNotices(noticeResults);
        //村庄介绍
        SysDept dept = this.deptService.lambdaQuery().eq(SysDept::getParentId, 0).one();
        SzDeptResult deptResult = BeanToResultUtil.beanToResult(dept, SzDeptResult.class);
        homeInfo.setRootDept(deptResult);
        //优秀村户介绍
        List<SzFarm> farms = this.farmService.queryByLimit(SzFarm.cid_cm, this.homeFarmLen);
        List<SzFarmResult> farmResults = BeanToResultUtil.beanToResult(farms, SzFarmResult.class);
        homeInfo.setFarms(farmResults);
        //政务公开信息
        List<SzOrgCate> cates = this.orgCateService.queryByLimit(this.homeOrgCateLen);
        List<SzOrgCateResult> cateResults = new ArrayList<>();
        for (SzOrgCate cate : cates) {
            SzOrgCateResult cateResult = BeanToResultUtil.beanToResult(cate, SzOrgCateResult.class);

            List<SzOrgContent> contents = this.orgContentService.queryByLimit(this.homeOrgContentLen, cate.getId());
            List<SzOrgContentResult> contentResults = BeanToResultUtil.beanToResult(contents, SzOrgContentResult.class);
            cateResult.setContents(contentResults);
            cateResults.add(cateResult);
        }
        homeInfo.setCates(cateResults);

        //写入缓存
        String hinfoStr = JSONUtil.toJsonStr(homeInfo);
        this.redisHelper.set(this.homeKey, hinfoStr, this.homeTimeout);
        return homeInfo;
    }

    /**
     * 获取汇聚三农的首页信息
     *
     * @return
     */
    public FarmInfoResult queryFarmInfo() {
        FarmInfoResult result = new FarmInfoResult();
        List<SzFarm> farms = this.farmService.queryByLimit(SzFarm.cid_cm, this.homeFarmLen);
        List<SzFarmResult> farmResults = BeanToResultUtil.beanToResult(farms, SzFarmResult.class);
        result.setFarms(farmResults);

        List<SzFarm> zcs = this.farmService.queryByLimit(SzFarm.cid_zc, this.homeFarmLen);
        List<SzFarmResult> zcResults = BeanToResultUtil.beanToResult(zcs, SzFarmResult.class);
        result.setZcs(zcResults);
        return result;
    }

    /**
     * 读取栓正村庄的介绍信息
     *
     * @param id
     * @return
     */
    public DeptInfoResult readDeptDetail(Integer id) {
        DeptInfoResult result = new DeptInfoResult();
        SysDept dept = this.deptService.getById(id);
        BeanUtil.copyProperties(dept, result);
        //如果是根目录节点的话，显示子节点，不显示任务
        if (dept.getDeptId() == 1) {
            List<SysDept> childs = this.deptService.lambdaQuery().eq(SysDept::getParentId, dept.getDeptId()).eq(SysDept::getDelFlag, 0).orderByDesc(SysDept::getSort, SysDept::getUpdateTime, SysDept::getCreateTime).list();
            List<SzDeptSimpleResult> childResults = BeanToResultUtil.beanToResult(childs, SzDeptSimpleResult.class);
            result.setChilds(childResults);
        } else {
//            List<SzTask> tasks = this.taskService.queryByCompletionStatusAndDept(id, SzTaskRecord.STATUS_TG);
//            List<SzTaskResult> taskResults=BeanToResultUtil.beanToResult(tasks,SzTaskResult.class);
//            result.setTasks(taskResults);
            List<SzFarm> farms = this.farmService.lambdaQuery().eq(SzFarm::getDept, id).eq(SzFarm::getDelFlag, 0).orderByDesc(SzFarm::getSort, SzFarm::getUpdateTime, SzFarm::getCreateTime).list();
            List<SzFarmResult> farmResults = BeanToResultUtil.beanToResult(farms, SzFarmResult.class);
            result.setFarms(farmResults);
        }
        return result;
    }
}
