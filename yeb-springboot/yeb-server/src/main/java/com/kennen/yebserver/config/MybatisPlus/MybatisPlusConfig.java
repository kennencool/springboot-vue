package com.kennen.yebserver.config.MybatisPlus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/15 10:07
 * @Description: Mybatis分页配置
 */
@Configuration
public class MybatisPlusConfig {
    
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
