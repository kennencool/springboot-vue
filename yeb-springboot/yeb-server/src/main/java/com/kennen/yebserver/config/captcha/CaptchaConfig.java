package com.kennen.yebserver.config.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/10 16:21
 * @Description: 验证码配置类
 */
@Configuration
public class CaptchaConfig {
    
    @Bean
    public DefaultKaptcha defaultKaptcha(){
        //  验证码生成器
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        //  配置
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border","yes"); //  边框
        properties.setProperty("kaptcha.border.color","105,179,90");    //  边框颜色
        properties.setProperty("kaptcha.session.key","code");   //  验证码
        properties.setProperty("kaptcha.textproducer.font.color","blue");
        properties.setProperty("kaptcha.textproducer.font.names","宋体,楷体,微软雅黑");
        properties.setProperty("kaptcha.textproducer.char.size","30");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        properties.setProperty("kaptcha.textproducer.char.space","4");
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.image.height","40");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
