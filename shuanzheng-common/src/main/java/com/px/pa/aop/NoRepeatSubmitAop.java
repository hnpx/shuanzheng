package com.px.pa.aop;


import cn.hutool.extra.servlet.ServletUtil;
import com.pig4cloud.pig.common.core.support.cache.RedisHelper;
import com.pig4cloud.pig.common.core.support.http.HttpCode;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.security.util.SecurityUtils;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import com.px.pa.rule.NoRepeatSubmit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class NoRepeatSubmitAop {
    private static final Logger log = LoggerFactory.getLogger(NoRepeatSubmitAop.class);
    @Autowired
    private RedisHelper redisUtil;
    @Value("${api.token-key:Authorization}")
    private String tokenKey;

    public NoRepeatSubmitAop() {
    }

    @Around("execution(public * com.px..*Controller.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed();
        } catch (Throwable var3) {
            var3.printStackTrace();
            return null;
        }
    }

    @Around("execution(public * com.px..*Controller.*(..)) && @annotation(noRepeatSubmit)")
    public Object doAround(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String ip = ServletUtil.getClientIP(request, new String[0]);
            String url = request.getRequestURL().toString();
            String token = null;
            if (url.startsWith("/api")) {
                token = request.getHeader(this.tokenKey);
            } else {
                token = SecurityUtils.getUser() == null ? null : SecurityUtils.getUser().getUsername();
            }

            long now = System.currentTimeMillis();
            String key = "REQUEST_FORM_" + ip + "_" + token;
            if (this.redisUtil.exists(key)) {
                long lastTime = Long.parseLong(this.redisUtil.get(key).toString());
                if (now - lastTime > (long)noRepeatSubmit.time()) {
                    this.redisUtil.set(key, String.valueOf(now));
                    return pjp.proceed();
                } else {
                    return R.failed("重复请求");
                }
            } else {
                this.redisUtil.set(key, String.valueOf(now));
                return pjp.proceed();
            }
        } catch (Throwable var12) {
            log.error("校验表单重复提交时异常: {}", var12.getMessage());
            return R.failed("校验表单重复提交时异常!");
        }
    }
}
