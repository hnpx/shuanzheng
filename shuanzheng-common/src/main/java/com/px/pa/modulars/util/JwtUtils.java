package com.px.pa.modulars.util;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.px.pa.modulars.core.entity.SzUser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

public class JwtUtils {

    private static KeyStore store;

    private static Object lock = new Object();

    private static String key="";


//    keytool -genkey -alias shuanzheng -keypass shuanzheng -keyalg RSA -storetype PKCS12 -keysize 1024 -validity 365 -keystore jwt.jks -storepass shuanzheng  -dname "CN=(Felord), OU=(felordcn), O=(felordcn), L=(zz), ST=(hn), C=(cn)"
    private static KeyPair create(String keyPath, String alias, String storepass) {
        ClassPathResource resource = new ClassPathResource(keyPath);
        char[] pem = storepass.toCharArray();
        try {
            String k=SecureUtil.md5(keyPath+alias+storepass);
            if (!k.equals(key)) {
                store = KeyStore.getInstance("jks");
                store.load(resource.getInputStream(), pem);
            }
            RSAPrivateCrtKey key = (RSAPrivateCrtKey) store.getKey(alias, pem);
            RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
            return new KeyPair(publicKey, key);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load keys from store: " + resource, e);
        }
    }

    public static String jwtToken(SzUser user,String path,String alias,String pass) {
        RSAPrivateKey privateKey = (RSAPrivateKey) create(path,alias,pass).getPrivate();
        RsaSigner signer = new RsaSigner(privateKey);
        JSONObject obj=new JSONObject();
        obj.put("user",user);
        obj.put("time",System.currentTimeMillis());
        return JwtHelper.encode(obj.toString(), signer).getEncoded();
    }
    public static SzUser parseToken(String token,String path,String alias,String pass){
        RSAPublicKey rsaPublicKey = (RSAPublicKey) create(path,alias,pass).getPublic();
        SignatureVerifier rsaVerifier = new RsaVerifier(rsaPublicKey);
//        Jwt jwt = JwtHelper.decode(token);
        Jwt jwt = JwtHelper.decodeAndVerify(token, rsaVerifier);
        String claims = jwt.getClaims();
        JSONObject obj= (JSONObject) JSONObject.parse(claims);
        return (SzUser) JSONObject.toJavaObject((JSON) obj.get("user"),SzUser.class);
    }
}
