package com.px.pa.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.px.pa.config.http.PasswordHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * 密码解密过滤器
 *
 * @author zhouz
 */
@WebFilter(urlPatterns = "/*", filterName = "PasswordDecoderFilter")
@Slf4j
public class PasswordDecoderFilter extends HttpFilter {


    @Value("${security.encode.key:1234567812345678}")
    private String encodeKey;

//    @Autowired
//    private PasswordEncoder encoding;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 不是登录请求，直接向下执行
        if (!StrUtil.containsAnyIgnoreCase(request.getRequestURL(), SecurityConstants.OAUTH_TOKEN_URL)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        request = new PasswordHttpServletRequestWrapper(request, this.encodeKey, new BCryptPasswordEncoder());
        chain.doFilter(request, servletResponse);
        return;
    }
}
