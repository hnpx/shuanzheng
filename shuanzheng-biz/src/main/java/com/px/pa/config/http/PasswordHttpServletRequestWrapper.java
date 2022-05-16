package com.px.pa.config.http;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 处理密码的Request
 *
 * @author zhouz
 */
public class PasswordHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final String PASSWORD = "password";
    private static final String KEY_ALGORITHM = "AES";

    private PasswordEncoder encoder;

    private HashMap<String, String[]> newParam = null;
    private String encodeKey;

    public PasswordHttpServletRequestWrapper(HttpServletRequest request, String encodeKey, PasswordEncoder encoder) {
        super(request);
        this.encodeKey = encodeKey;
        this.encoder = encoder;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Enumeration<String> enumeration = super.getParameterNames();
        ArrayList<String> list = Collections.list(enumeration);
        //当有token字段时动态的添加uid字段
//        if (list.contains(PASSWORD)){
//            return Collections.enumeration(list);
//        }else {
        return super.getParameterNames();
//        }
    }

    @Override
    public String getParameter(String name) {
        if (PASSWORD.equals(name)) {
            String[] passwords = super.getParameterValues(name);
            //TODO 默认在第0个
            String password = passwords[0];
            password = decryptAES(password, encodeKey);
            password = password.trim();
            String pd = encoder.encode(password);

            return password;
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        HashMap<String, String[]> newMap = new HashMap<>();
        newMap.putAll(super.getParameterMap());
        newMap.put(PASSWORD, this.getParameterValues(PASSWORD));
        return Collections.unmodifiableMap(newMap);
    }


    private static String decryptAES(String data, String pass) {
        AES aes = new AES(Mode.CBC, Padding.NoPadding, new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(pass.getBytes()));
        byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
        return new String(result, StandardCharsets.UTF_8);
    }

    @Override
    public String[] getParameterValues(String name) {
        if (PASSWORD.equals(name)) {
            return new String[]{this.getParameter(name)};
        }
        return super.getParameterValues(name);
    }
}
