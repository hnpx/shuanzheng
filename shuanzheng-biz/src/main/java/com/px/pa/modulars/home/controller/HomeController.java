package com.px.pa.modulars.home.controller;


import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.security.annotation.Inner;
import com.px.pa.modulars.home.service.HomeInfoService;
import com.px.pa.vo.result.HomeDataInfoResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouz
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@Api(value = "home", tags = "首页服务")
public class HomeController {
    @Autowired
    private HomeInfoService homeInfoService;

    @GetMapping("/read")
    @ApiOperation("首页信息查询")
//    @Inner(false)
    public R readHomeInfo() {
        HomeDataInfoResult result = this.homeInfoService.readHome();

        return R.ok(result);
    }
}
