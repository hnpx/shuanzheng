package com.px.pa.modulars.points.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.pa.modulars.points.mapper.SzRankingMapper;
import com.px.pa.modulars.points.service.UserRankingService;
import com.px.pa.modulars.vo.RankingItemQueryParam;
import com.px.pa.modulars.vo.RankingQueryParam;
import com.px.pa.modulars.vo.result.RankingHomeResult;
import com.px.pa.modulars.vo.result.RankingItemResult;
import com.px.pa.modulars.vo.result.SzRecordSimpleResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouz
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
@Api(value = "api/ranking", tags = "用户榜单")
public class UserRankingApiController extends BaseApiController {
    @Autowired
    private UserRankingService rankingService;

    @ApiOperation(value = "查询乡镇公示", notes = "查询乡镇公示")
    @PutMapping("/read/publicity")
    public R queryPublicity(@RequestBody RankingItemQueryParam param) {
        Page<SzRecordSimpleResult> records = this.rankingService.queryPublicity(param);
        return R.ok(records);
    }

    @ApiOperation(value = "查询榜单信息", notes = "查询榜单信息")
    @PutMapping("/read/home")
    public R readHome(@RequestBody RankingQueryParam param) {
        RankingHomeResult result = this.rankingService.readHome(param);
        return R.ok(result);
    }


    @ApiOperation(value = "查询榜单信息", notes = "查询榜单信息")
    @PutMapping("/read/list")
    public R readList(@RequestBody RankingQueryParam param) {
        Page<RankingItemResult> result = this.rankingService.queryRanking(param);
        return R.ok(result);
    }

    @ApiOperation(value = "查询某个村干部的榜单信息", notes = "查询某个村干部的榜单信息")
    @PutMapping("/read/homeByCadres")
    public R readHomeByCadres(@RequestBody RankingQueryParam param) {
        Integer userId = super.getCurrentId();
        RankingHomeResult result = this.rankingService.readHomeByCadres(userId, param);
        return R.ok(result);
    }


    @ApiOperation(value = "查询某个驻村干部的榜单信息", notes = "查询某个驻村干部的榜单信息")
    @PutMapping("/read/homeByVillageCadres")
    public R readHomeByVillageCadres(@RequestBody RankingQueryParam param) {
        Integer userId = super.getCurrentId();
        RankingHomeResult result = this.rankingService.readHomeByVillageCadres(userId, param);
        return R.ok(result);
    }

    @ApiOperation(value = "查询某个驻村干部的榜单信息", notes = "查询某个驻村干部的榜单信息")
    @PutMapping("/read/listUserByVillageCadres")
    public R queryUserByVillageCadres(@RequestBody RankingQueryParam param) {
        Integer userId = super.getCurrentId();
        Page<RankingItemResult> result = this.rankingService.queryUserByVillageCadres(userId, param);
        return R.ok(result);
    }


    @ApiOperation(value = "查询某个村干部的榜单信息", notes = "查询某个村干部的榜单信息")
    @PutMapping("/read/listUserByCadres")
    public R queryUserByCadres(@RequestBody RankingQueryParam param) {
        Integer userId = super.getCurrentId();
        Page<RankingItemResult> result = this.rankingService.queryUserByCadres(userId, param);
        return R.ok(result);
    }
}
