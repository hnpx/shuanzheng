package com.px.pa.modulars.points.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.support.cache.RedisHelper;
import com.pig4cloud.pig.common.core.util.R;
import com.px.pa.constants.UserTypeEnum;
import com.px.pa.modulars.core.entity.SzCompany;
import com.px.pa.modulars.core.entity.SzTaskRecord;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.service.SzCompanyService;
import com.px.pa.modulars.core.service.SzTaskRecordService;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.points.entity.PointsLevel;
import com.px.pa.modulars.points.mapper.SzRankingMapper;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.entity.SysUserDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import com.px.pa.modulars.upms.service.SysUserDeptService;
import com.px.pa.modulars.vo.RankingItemQueryParam;
import com.px.pa.modulars.vo.RankingQueryParam;
import com.px.pa.modulars.vo.result.RankingHomeResult;
import com.px.pa.modulars.vo.result.RankingItemResult;
import com.px.pa.modulars.vo.result.SzRecordSimpleResult;
import com.px.pa.utils.bean.BaseQueryToPageUtil;
import com.px.pa.utils.bean.BeanToResultUtil;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
public class UserRankingService {
    @Autowired
    private SzTaskRecordService recordService;
    @Autowired
    private SzUserService userService;
    @Autowired
    private SysUserDeptService userDeptService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SzCompanyService companyService;
    @Autowired
    private PointsLevelService pointsLevelService;
    @Autowired
    private SzRankingMapper rankingMapper;

    @Autowired
    private RedisHelper redisHelper;

    @Value("${application.ranking.top-len}")
    private Integer topLen = 3;

    @Value("${application.ranking.record-len}")
    private Integer recordLen = 5;
    @Value("${application.ranking.cache-timeout}")
    private Integer cacheTimeout = 60000;
    @Value("${application.ranking.cache-key}")
    private String cacheKey;


    public Page<SzRecordSimpleResult> queryPublicity(RankingItemQueryParam param) {
        Page<SzTaskRecord> page = BaseQueryToPageUtil.createPage(param);

        page = this.recordService.lambdaQuery()

                //推介村民，存在加分项，按照时间倒序排列
                .gt(param.getType() != null && param.getType() == 1, SzTaskRecord::getPoints, 0)
                .eq(param.getType() != null && param.getType() == 1, SzTaskRecord::getState, SzTaskRecord.STATUS_TG)
                //待整改村民，存在减分项，未审核确认，按照时间倒序排列
                .lt(param.getType() != null && param.getType() == 2, SzTaskRecord::getPoints, 0)
                .eq(param.getType() != null && param.getType() == 2, SzTaskRecord::getState, SzTaskRecord.STATUS_DSH)
                //黑名单，存在减分项，已经审核确认，按照时间倒序排列
                .lt(param.getType() != null && param.getType() == 3, SzTaskRecord::getPoints, 0)
                .eq(param.getType() != null && param.getType() == 3, SzTaskRecord::getState, SzTaskRecord.STATUS_TG)
                //如果是空的话，说明查询所有

                //按照时间倒序排列
                .orderByDesc(SzTaskRecord::getCreateTime, SzTaskRecord::getUpdateTime)
                .page(page);
        Page<SzRecordSimpleResult> pageResult = BeanToResultUtil.beanToResult(page, SzRecordSimpleResult.class);
        this.recordService.packResult(pageResult.getRecords());
        return pageResult;
    }

    /**
     * 查询排行榜
     *
     * @param param
     * @return
     */
    public RankingHomeResult readHome(RankingQueryParam param) {
        RankingHomeResult result = null;
        Object homeObj = this.redisHelper.get(this.cacheKey);
        if (homeObj != null) {
            try {
                result = JSONUtil.toBean(homeObj.toString(), RankingHomeResult.class);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result = new RankingHomeResult();
        //栓正播报信息
        RankingItemQueryParam itemParam = new RankingItemQueryParam();
        itemParam.setPageSize(this.recordLen);
        itemParam.setPageNo(1);
        Page<SzRecordSimpleResult> records = this.queryPublicity(itemParam);
        result.setRecords(records.getRecords());
        //TOP INFO
        RankingQueryParam topParam = new RankingQueryParam();
        BeanUtil.copyProperties(param, topParam);
        topParam.setPageSize(this.topLen);
        Page<RankingItemResult> topRankingPage = this.queryRanking(topParam);
        result.setTopItems(topRankingPage.getRecords());
        //Home Ranking Items
        result.getTopItems().forEach(item -> {
            param.addSkip(item.getId());
        });
        Page<RankingItemResult> rankingPage = this.queryRanking(param);
        result.setRankings(rankingPage);

//如果是村民排行榜的话，带着村信息返回
        if (param.getType() == 1) {
            List<SysDept> depts = this.deptService.lambdaQuery()
                    .eq(SysDept::getParentId,1)
                    .eq(SysDept::getDelFlag, 0)
                    .orderByDesc(SysDept::getSort, SysDept::getUpdateTime, SysDept::getCreateTime)
                    .list();
            result.setDepts(depts);
        }

//        this.redisHelper.set(this.cacheKey, JSONUtil.toJsonStr(result),this.cacheTimeout);
        return result;
    }


    public Page<RankingItemResult> queryRanking(RankingQueryParam param) {

//        @ApiModelProperty("榜单类型，1、居民；2、驻村干部；3、村庄榜；4、村干部榜；5、企业榜")
        switch (param.getType()) {
            case 1:
                return this.queryUser(param);
            case 2:
                return this.queryVillageCadres(param);
            case 3:
                return this.queryVillage(param);
            case 4:
                return this.queryCadres(param);
            case 5:
                return this.queryCompany(param);
        }
        return null;
    }


    /**
     * 公司的积分排行榜
     *
     * @param param
     * @return
     */
    public Page<RankingItemResult> queryCompany(RankingQueryParam param) {
        Page<SzCompany> page = BaseQueryToPageUtil.createPage(param);
        Page<RankingItemResult> result = BaseQueryToPageUtil.createPage(param);
        //TODO 统计当前年份，如果不是当年年份，在查询历史记录
        //TODO 查询村庄级别的，暂时不查询大队级别
        page = this.companyService.lambdaQuery().eq(SzCompany::getDelFlag, 0).notIn(param.getSkipIds().size() > 0, SzCompany::getId, param.getSkipIds()).orderByDesc(SzCompany::getAllScore, SzCompany::getUpdateTime, SzCompany::getCreateTime).page(page);
        result.setTotal(page.getTotal());
        List<RankingItemResult> list = new ArrayList<>();
        List<PointsLevel> levels = this.pointsLevelService.lambdaQuery().eq(PointsLevel::getDelFlag, 0).eq(PointsLevel::getType, 5).orderByAsc(PointsLevel::getBeginScore).list();
        page.getRecords().forEach(item -> {
            RankingItemResult ri = new RankingItemResult();
            //户
            ri.setArea(1);
            ri.setName(item.getName());
            ri.setId(item.getId());
            ri.setScore(item.getScore());
            // 计算星级
            ri.setNum(this.pointsLevelService.getLevel(ri.getScore(), levels));
            list.add(ri);
        });
        result.setRecords(list);
        return result;
    }

    /**
     * 栓正村庄的积分排行榜
     *
     * @param param
     * @return
     */
    public Page<RankingItemResult> queryVillage(RankingQueryParam param) {
        Page<SysDept> page = BaseQueryToPageUtil.createPage(param);
        Page<RankingItemResult> result = BaseQueryToPageUtil.createPage(param);
        //TODO 统计当前年份，如果不是当年年份，在查询历史记录
        //TODO 查询村庄级别的，暂时不查询大队级别
        page = this.deptService.lambdaQuery().eq(SysDept::getDelFlag, 0).eq(SysDept::getParentId, 1).notIn(param.getSkipIds().size() > 0, SysDept::getDeptId, param.getSkipIds()).orderByDesc(SysDept::getAllScore, SysDept::getUpdateTime, SysDept::getCreateTime).page(page);
        result.setTotal(page.getTotal());
        List<RankingItemResult> list = new ArrayList<>();
        List<PointsLevel> levels = this.pointsLevelService.lambdaQuery().eq(PointsLevel::getDelFlag, 0).eq(PointsLevel::getType, 3).orderByAsc(PointsLevel::getBeginScore).list();
        page.getRecords().forEach(item -> {
            RankingItemResult ri = new RankingItemResult();
            //户
            ri.setArea(1);
            ri.setName(item.getName());
            ri.setId(item.getDeptId());
            ri.setScore(item.getScore());
            // 计算星级
            ri.setNum(this.pointsLevelService.getLevel(ri.getScore(), levels));
            list.add(ri);
        });
        result.setRecords(list);
        return result;
    }

    /**
     * 村干部的积分排行榜
     *
     * @param param
     * @return
     */
    public Page<RankingItemResult> queryCadres(RankingQueryParam param) {
        Page<RankingItemResult> result = BaseQueryToPageUtil.createPage(param);
        List<SzUser> users = this.userService.lambdaQuery().eq(SzUser::getRole, 2).eq(SzUser::getLogin, 1).eq(SzUser::getDelFlag, 0).notIn(param.getSkipIds().size() > 0, SzUser::getId, param.getSkipIds()).list();
        List<RankingItemResult> rankings = new ArrayList<>();
        List<PointsLevel> levels = this.pointsLevelService.lambdaQuery().eq(PointsLevel::getDelFlag, 0).eq(PointsLevel::getType, 4).orderByAsc(PointsLevel::getBeginScore).list();
        for (SzUser user : users) {
            //查询负责的有哪些村
            RankingItemResult item = new RankingItemResult();
            item.setArea(1);
            item.setPhoto(user.getAvatar());
            item.setId(user.getId());
            item.setName(user.getName());
            item.setDid(user.getDid());
            SysDept dept = this.deptService.getById(user.getDid());
            item.setDeptName(dept != null ? dept.getName() : "");
            item.setScore(queryScoreByVillageCadres(user));
            item.setNum(this.pointsLevelService.getLevel(item.getScore(), levels));
            rankings.add(item);
        }
        rankings.sort((a, b) -> {
            return b.getScore() - a.getScore();
        });
        rankings=rankings.subList(0, rankings.size()>param.getPageSize()?param.getPageSize():rankings.size());
        result.setRecords(rankings);
        result.setTotal(rankings.size());
        result.setSize(rankings.size());
        return result;
    }

    /**
     * 驻村干部的积分排行榜
     *
     * @param param
     * @return
     */
    public Page<RankingItemResult> queryVillageCadres(RankingQueryParam param) {
        Page<RankingItemResult> result = BaseQueryToPageUtil.createPage(param);
        List<SzUser> users = this.userService.lambdaQuery().eq(SzUser::getRole, 3).eq(SzUser::getLogin, 1).eq(SzUser::getDelFlag, 0).notIn(param.getSkipIds().size() > 0, SzUser::getId, param.getSkipIds()).list();
        List<RankingItemResult> rankings = new ArrayList<>();
        List<PointsLevel> levels = this.pointsLevelService.lambdaQuery().eq(PointsLevel::getDelFlag, 0).eq(PointsLevel::getType, 2).orderByAsc(PointsLevel::getBeginScore).list();
        for (SzUser user : users) {
            //查询负责的有哪些村
            RankingItemResult item = new RankingItemResult();
            item.setArea(1);
            item.setPhoto(user.getAvatar());
            item.setId(user.getId());
            item.setName(user.getName());
            item.setDid(user.getDid());
            SysDept dept = this.deptService.getById(user.getDid());
            item.setDeptName(dept != null ? dept.getName() : "");
            item.setScore(queryScoreByVillageCadres(user));
            item.setNum(this.pointsLevelService.getLevel(item.getScore(), levels));
            rankings.add(item);
        }

        rankings.sort((a, b) -> {
            return b.getScore() - a.getScore();
        });
        rankings=rankings.subList(0, rankings.size()>param.getPageSize()?param.getPageSize():rankings.size());
        result.setRecords(rankings);
        result.setTotal(rankings.size());
        result.setSize(rankings.size());
        return result;
    }

    /**
     * 查询用户的排行榜
     *
     * @param param
     * @return
     */
    public Page<RankingItemResult> queryUser(RankingQueryParam param) {
        Page<SzUser> page = BaseQueryToPageUtil.createPage(param);
        Page<RankingItemResult> result = BaseQueryToPageUtil.createPage(param);
        //TODO 统计当前年份，如果不是当年年份，在查询历史记录
        page = this.userService.lambdaQuery()
                .eq(SzUser::getLogin, 1)
                .eq(SzUser::getDelFlag, 0)
                .eq(param.getDept()!=null,SzUser::getDid,param.getDept())
                .notIn(param.getSkipIds().size() > 0, SzUser::getId, param.getSkipIds())
                .orderByDesc(SzUser::getScore, SzUser::getUpdateTime).page(page);
        result.setTotal(page.getTotal());
        List<RankingItemResult> list = new ArrayList<>();
        List<PointsLevel> levels = this.pointsLevelService.lambdaQuery().eq(PointsLevel::getDelFlag, 0).eq(PointsLevel::getType, 1).orderByAsc(PointsLevel::getBeginScore).list();
        page.getRecords().forEach(item -> {
            RankingItemResult ri = new RankingItemResult();
            //户
            ri.setArea(1);
            SysDept dept = this.deptService.getById(item.getDid());
            ri.setDeptName(dept != null ? dept.getName() : "");
            ri.setDid(item.getDid());
            ri.setName(item.getName());
            ri.setId(item.getId());
            ri.setPhoto(item.getAvatar());
            ri.setScore(item.getScore());
            // 计算星级
            ri.setNum(this.pointsLevelService.getLevel(ri.getScore(), levels));
            list.add(ri);
        });
        result.setRecords(list);
        return result;
    }

    /**
     * 查询某个驻村干部负责的村的用户的排行榜
     *
     * @param param
     * @param userId 驻村干部的ID
     * @return
     */
    public Page<RankingItemResult> queryUserByCadres(Integer userId, RankingQueryParam param) {
        Page<SzUser> page = BaseQueryToPageUtil.createPage(param);
        Page<RankingItemResult> result = BaseQueryToPageUtil.createPage(param);

        List<SysUserDept> suds = this.userDeptService.lambdaQuery().eq(SysUserDept::getUserId, userId).list();
        SysUserDept sud=null;
        SysDept udept=null;
        if(suds.size()>0){
            sud=suds.get(0);
            udept=this.deptService.getById(sud.getDeptId());
        }else{
            return result;
        }



        //TODO 统计当前年份，如果不是当年年份，在查询历史记录
        page = this.userService.lambdaQuery().eq(SzUser::getLogin, 1).eq(SzUser::getDelFlag, 0)
                .eq(udept.getParentId()==1, SzUser::getDid, udept.getDeptId())//判断是否是大队权限
                .eq(udept.getParentId()!=1,SzUser::getCdid,udept.getDeptId())
                .notIn(param.getSkipIds().size() > 0, SzUser::getId, param.getSkipIds())
                .orderByDesc(SzUser::getAllScore, SzUser::getUpdateTime).page(page);
        result.setTotal(page.getTotal());
        List<RankingItemResult> list = new ArrayList<>();
        List<PointsLevel> levels = this.pointsLevelService.lambdaQuery().eq(PointsLevel::getDelFlag, 0).eq(PointsLevel::getType, 1).orderByAsc(PointsLevel::getBeginScore).list();
        page.getRecords().forEach(item -> {
            RankingItemResult ri = new RankingItemResult();
            //户
            ri.setArea(1);
            SysDept dept = this.deptService.getById(item.getDid());
            ri.setDeptName(dept != null ? dept.getName() : "");
            ri.setDid(item.getDid());
            ri.setName(item.getName());
            ri.setId(item.getId());
            ri.setPhoto(item.getAvatar());
            ri.setScore(item.getScore());
            // 计算星级
            ri.setNum(this.pointsLevelService.getLevel(ri.getScore(), levels));
            list.add(ri);
        });
        result.setRecords(list);
        return result;
    }

    /**
     * 查询某个驻村干部负责的村的用户的排行榜
     *
     * @param param
     * @param userId 驻村干部的ID
     * @return
     */
    public Page<RankingItemResult> queryUserByVillageCadres(Integer userId, RankingQueryParam param) {
        Page<SzUser> page = BaseQueryToPageUtil.createPage(param);
        Page<RankingItemResult> result = BaseQueryToPageUtil.createPage(param);

        List<SysUserDept> suds = this.userDeptService.lambdaQuery().eq(SysUserDept::getUserId, userId).list();
        List<Integer> deptIds = new ArrayList<>();
        suds.forEach(item -> {
            deptIds.add(item.getDeptId());
        });

        if (deptIds.size() == 0) {
            return result;
        }
        //TODO 统计当前年份，如果不是当年年份，在查询历史记录
        page = this.userService.lambdaQuery().eq(SzUser::getLogin, 1)
                .eq(SzUser::getDelFlag, 0)
                .in(SzUser::getDid, deptIds)
                .eq(param.getDept()!=null,SzUser::getDid,param.getDept())
                .notIn(param.getSkipIds().size() > 0, SzUser::getId, param.getSkipIds())
                .orderByDesc(SzUser::getAllScore, SzUser::getUpdateTime).page(page);
        result.setTotal(page.getTotal());
        List<RankingItemResult> list = new ArrayList<>();
        List<PointsLevel> levels = this.pointsLevelService.lambdaQuery().eq(PointsLevel::getDelFlag, 0).eq(PointsLevel::getType, 1).orderByAsc(PointsLevel::getBeginScore).list();
        page.getRecords().forEach(item -> {
            RankingItemResult ri = new RankingItemResult();
            //户
            ri.setArea(1);
            SysDept dept = this.deptService.getById(item.getDid());
            ri.setDeptName(dept != null ? dept.getName() : "");
            ri.setDid(item.getDid());
            ri.setName(item.getName());
            ri.setId(item.getId());
            ri.setPhoto(item.getAvatar());
            ri.setScore(item.getAllScore());
            // 计算星级
            ri.setNum(this.pointsLevelService.getLevel(ri.getScore(), levels));
            list.add(ri);
        });
        result.setRecords(list);
        return result;
    }


    /**
     * 查询驻村干部的排行榜首页
     *
     * @param param
     * @return
     */
    public RankingHomeResult readHomeByVillageCadres(Integer userId, RankingQueryParam param) {
        RankingHomeResult result = null;
        Object homeObj = this.redisHelper.get(this.cacheKey + "_villageCadres_" + userId);
        if (homeObj != null) {
            try {
                result = JSONUtil.toBean(homeObj.toString(), RankingHomeResult.class);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result = new RankingHomeResult();
        //栓正播报信息
        RankingItemQueryParam itemParam = new RankingItemQueryParam();
        itemParam.setPageSize(this.recordLen);
        itemParam.setPageNo(1);
        Page<SzRecordSimpleResult> records = this.queryPublicity(itemParam);
        result.setRecords(records.getRecords());
        //TOP INFO
        RankingQueryParam topParam = new RankingQueryParam();
        BeanUtil.copyProperties(param, topParam);
        topParam.setPageSize(this.topLen);
        Page<RankingItemResult> topRankingPage = this.queryUserByVillageCadres(userId, topParam);
        result.setTopItems(topRankingPage.getRecords());
        //Home Ranking Items
        result.getTopItems().forEach(item -> {
            param.addSkip(item.getId());
        });
        Page<RankingItemResult> rankingPage = this.queryUserByVillageCadres(userId, param);
        result.setRankings(rankingPage);

        List<SysUserDept> suds = this.userDeptService.lambdaQuery().eq(SysUserDept::getUserId, userId).list();

        List<Integer> deptIds = new ArrayList<>();
        suds.forEach(item -> {
            deptIds.add(item.getDeptId());
        });

        List<SysDept> depts = this.deptService.lambdaQuery().in(SysDept::getDeptId, deptIds).orderByDesc(SysDept::getSort, SysDept::getUpdateTime, SysDept::getCreateTime).list();
        result.setDepts(depts);

//        this.redisHelper.set(this.cacheKey+"_villageCadres_"+userId, JSONUtil.toJsonStr(result),this.cacheTimeout);
        return result;
    }

    /**
     * 查询排行榜
     *
     * @param param
     * @return
     */
    public RankingHomeResult readHomeByCadres(Integer userId, RankingQueryParam param) {
        RankingHomeResult result = null;
        Object homeObj = this.redisHelper.get(this.cacheKey + "_cadres_" + userId);
        if (homeObj != null) {
            try {
                result = JSONUtil.toBean(homeObj.toString(), RankingHomeResult.class);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result = new RankingHomeResult();
        //栓正播报信息
        RankingItemQueryParam itemParam = new RankingItemQueryParam();
        itemParam.setPageSize(this.recordLen);
        itemParam.setPageNo(1);
        Page<SzRecordSimpleResult> records = this.queryPublicity(itemParam);
        result.setRecords(records.getRecords());
        //TOP INFO
        RankingQueryParam topParam = new RankingQueryParam();
        BeanUtil.copyProperties(param, topParam);
        topParam.setPageSize(this.topLen);
        Page<RankingItemResult> topRankingPage = this.queryUserByCadres(userId, topParam);
        result.setTopItems(topRankingPage.getRecords());
        //Home Ranking Items
        result.getTopItems().forEach(item -> {
            param.addSkip(item.getId());
        });
        Page<RankingItemResult> rankingPage = this.queryUserByCadres(userId, param);
        result.setRankings(rankingPage);

//        this.redisHelper.set(this.cacheKey+"_cadres_"+userId, JSONUtil.toJsonStr(result),this.cacheTimeout);
        return result;
    }
    public Integer queryScoreByVillageCadres(SzUser user){
        QueryWrapper query=new QueryWrapper<>();
        query.eq("login", 1);
        query.eq("del_flag", 0);
        List<SysUserDept> list=userDeptService.lambdaQuery().eq(SysUserDept::getUserId,user.getId()).list();
        if(user.getRole().equals(UserTypeEnum.LEADER.getValue())){
            if(list.size()==1){
                SysUserDept dept=list.get(0);
                if(dept.getChildId()!=null){
                    query.eq("cdid", dept.getDeptId());
                }else{
                    query.eq("did", dept.getDeptId());
                }
            }else{
                return 0;
            }
        }else if(user.getRole().equals(UserTypeEnum.TOPLEADER.getValue())){
            if(list.size()>0){
                query.in("did",list.stream().map(SysUserDept::getDeptId).collect(Collectors.toList()));
            }else{
                return 0;
            }
        }
        return this.rankingMapper.queryScoreByVillageCadres(query).intValue();
    }
}
