package com.kennen.yebserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.kennen.yebserver.mapper")
public class YebApplication {
    public static void main(String[] args){
        SpringApplication.run(YebApplication.class, args);
    }
}
