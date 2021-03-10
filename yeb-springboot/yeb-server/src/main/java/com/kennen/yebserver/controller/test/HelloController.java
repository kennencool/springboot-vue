package com.kennen.yebserver.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/10 14:35
 * @Description: 文档测试controller
 */
@RestController
public class HelloController {
    
    @GetMapping("hello")
    public String hello(){
        return "hello";
    }
}
