package com.px.pa.modulars.points.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.modulars.core.entity.SzUser;
import com.px.pa.modulars.core.service.SzUserService;
import com.px.pa.modulars.points.entity.SzRanking;
import com.px.pa.modulars.points.mapper.SzRankingMapper;
import com.px.pa.modulars.upms.entity.SysUserDept;
import com.px.pa.modulars.upms.service.SysUserDeptService;
import com.px.pa.modulars.upms.service.SysUserService;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 排行榜自动刷新服务
 */
@Service
@Slf4j
@Transactional
public class RankingFinishService extends ServiceImpl<SzRankingMapper, SzRanking> {
    @Autowired
    private SzUserService userService;

    @Autowired
    private SysUserDeptService userDeptService;

    /**
     * 刷新当前的排名信息
     */
    public void finish() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        this.finishUser(year);
    }

    /**
     * 刷新驻村干部的积分信息
     *
     * @param year
     */
    private void finishVillageCadres(int year) {
//        @ApiModelProperty(value="身份：1-村民，2-村干部，3-包村干部")
        List<SzUser> users=this.userService.lambdaQuery().eq(SzUser::getRole,3).eq(SzUser::getLogin,1).eq(SzUser::getDelFlag,0).list();
        for(SzUser user:users){
            //查询负责的有哪些村

        }
    }

    /**
     * 刷新用户排名信息
     */
    private void finishUser(int year) {
        //TODO 未完成
        Page<SzUser> szUserPage = new Page<>();
        szUserPage.setCurrent(1);
        szUserPage.setSize(1000);
        while (true) {
            //查询未删除、可以登录的用户信息
            szUserPage = this.userService.lambdaQuery().eq(SzUser::getDelFlag, 0).eq(SzUser::getLogin, 1).page(szUserPage);
            if (szUserPage.getRecords().size() == 0) {
                break;
            }
            szUserPage.getRecords().forEach(item -> {
                SzRanking ranking = super.lambdaQuery().eq(SzRanking::getYear, year).eq(SzRanking::getUid, item.getId()).eq(SzRanking::getDelFlag, 0).one();
                if (ranking == null) {
                    ranking = new SzRanking();
                    ranking.setCreateTime(LocalDateTime.now());
                    ranking.setDelFlag(0);
                    ranking.setUid(item.getId());
                    ranking.setName(item.getName());
                    ranking.setDid(item.getDid());
                }
            });
        }
    }

    /**
     * 清空排名积分信息，手动调用
     */
    public void clear() {

    }
}
