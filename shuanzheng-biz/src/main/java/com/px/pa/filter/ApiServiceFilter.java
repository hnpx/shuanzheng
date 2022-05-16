package com.px.pa.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pig.common.core.support.cache.RedisHelper;
import com.pig4cloud.pig.common.core.support.http.HttpCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * API请求过滤器
 *
 * @author zhouz
 */
@WebFilter(urlPatterns = "/api/*", filterName = "ApiServiceFilter")
@Slf4j
public class ApiServiceFilter extends HttpFilter {
    @Autowired
    private RedisHelper redisHelper;
//    @Autowired
//    private CircleUserMapper circleUserMapper;

    @Value("${api.token-key}")
    private String apiTokenKey;

    @Value("${api.jwt-secret}")
    private String jwtSecret;

    @Value("${api.jwt-expire-seconds}")
    private Long expireSeconds;

    @Value("${api.skip-paths}")
    private String openPaths;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //如果是登录操作
        if (this.checkPathContains(request.getRequestURL().toString(), this.openPaths)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        //如果不是，则判断用户Token，如果没有报错401
        String token = request.getHeader(this.apiTokenKey);
        log.info(this.apiTokenKey + "=======================" + token);
        if (token == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", HttpCode.LOGIN_FAIL.value());
            servletResponse.getWriter().print(JSONUtil.toJsonStr(result));
            return;
        }

        // Object idObj = this.redisHelper.hget(Constants.Auth.TOKEN_CACHE_KEY, token);
        Object idObj = this.redisHelper.get(token);
        log.info("toke2================================================" + idObj);
        if (idObj == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", HttpCode.LOGIN_FAIL.value());
            servletResponse.getWriter().print(JSONUtil.toJsonStr(result));
            return;
        }
        //判断用户是否已经注销
//         circleUser = circleUserMapper.getCircleByid(Integer.parseInt(idObj.toString()));
//        if (circleUser != null) {
//            if (circleUser.getIsDel().equals(IsDelEnum.IS_DEL_ENUM_TWO.getValue())) {
//                Map<String, Object> result = new HashMap<>();
//                result.put("code", HttpCode.LOGIN_FAIL.value());
//                servletResponse.getWriter().print(JSONUtil.toJsonStr(result));
//                return;
//            }
//        }
        chain.doFilter(servletRequest, servletResponse);
        return;
    }

    /**
     * 检查地址是否包含
     *
     * @param url
     * @param paths
     * @return
     */
    private boolean checkPathContains(String url, String paths) {
        String[] pathArr=paths.split(",");
        if (StrUtil.containsAnyIgnoreCase(url, pathArr)) {
            return true;
        }
        return false;
    }
}
