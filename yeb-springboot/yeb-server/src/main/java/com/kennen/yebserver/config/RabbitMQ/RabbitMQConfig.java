package com.kennen.yebserver.config.RabbitMQ;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.kennen.yebserver.pojo.MailSendLog;
import com.kennen.yebserver.pojo.mail.MailConstants;
import com.kennen.yebserver.service.IMailSendLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/17 21:16
 * @Description: rabbitmq配置类
 */
@Configuration
public class RabbitMQConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);
    @Autowired private CachingConnectionFactory factory;
    @Resource private IMailSendLogService mailSendLogService;
    
    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        /**
         * 消息确认回调，确认消息是否到达broker
         * data：消息唯一标识
         * ack：确认结果
         * cause：失败原因
         */
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            if(ack){
                LOGGER.info("{}==========>消息发送成功！",data.getId());
                mailSendLogService.update(new UpdateWrapper<MailSendLog>().set("status",1).eq("msgId",data.getId()));
            }else{
                LOGGER.error("{}==========>消息发送失败！",data.getId());
            }
        });

        /**
         * 消息失败回调，比如没有路由到指定队列
         * msg：消息主体
         * repCode：响应码
         * repText：响应描述
         */
        rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingkey)->{
            LOGGER.error("{}==========>消息发送失败！",msg.getBody());
        });
        return rabbitTemplate;
    }
    
    
    
    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }
    
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }
    
    @Bean
    public Binding bingding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY);
    }
}
