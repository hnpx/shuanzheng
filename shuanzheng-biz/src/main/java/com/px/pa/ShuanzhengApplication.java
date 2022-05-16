package com.px.pa;

import com.px.basic.alone.security.annotation.EnablePigResourceServer;
import com.px.pa.config.RedisProperies;
import com.px.plugins.conversion.core.config.ConversionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author pig archetype
 * <p>
 * 项目启动类
 */
//@EnablePigFeignClients
@EnablePigResourceServer
//@SpringCloudApplication
@ServletComponentScan
@SpringBootApplication
@ComponentScan(basePackages = {"com.pig4cloud.pig", "com.px"})
@EnableConfigurationProperties({RedisProperies.class, ConversionConfig.class})
public class ShuanzhengApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(ShuanzhengApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
