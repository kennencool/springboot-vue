package com.kennen.yebserver.pojo.chat;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/18 17:14
 * @Description: 聊天消息体
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChatMsg {
    private String from;
    private String to;
    private String content;
    private LocalDateTime date;
    private String formNickName;
}
