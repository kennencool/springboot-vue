package com.kennen.yebserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@MapperScan("com.kennen.yebserver.mapper")
public class YebApplication {
    public static void main(String[] args){
        SpringApplication.run(YebApplication.class, args);
    }
}
