package com.kennen.yebserver.controller.websocket;

import com.kennen.yebserver.pojo.Hr;
import com.kennen.yebserver.pojo.chat.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/18 17:15
 * @Description: websocket接口
 */
@Controller
public class WsController {
    @Autowired private SimpMessagingTemplate simpMessagingTemplate;
    
    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg){
        Hr hr = (Hr)authentication.getPrincipal();
        chatMsg.setFrom(hr.getUsername());
        chatMsg.setFormNickName(hr.getName());
        chatMsg.setDate(LocalDateTime.now());
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(),"/queue/chat",chatMsg);
    }
}
