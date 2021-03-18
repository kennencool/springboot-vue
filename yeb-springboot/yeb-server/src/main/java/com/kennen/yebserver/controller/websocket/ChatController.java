package com.kennen.yebserver.controller.websocket;

import com.kennen.yebserver.pojo.Hr;
import com.kennen.yebserver.service.IHrService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/18 17:20
 * @Description: 聊天
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Resource private IHrService hrService;
    
    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/admin")
    public List<Hr> getAllHrs(String keywords){
        return hrService.getAllHrs(keywords);
    }
}
