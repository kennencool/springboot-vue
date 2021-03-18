package com.kennen.yebmail;

import com.kennen.yebserver.pojo.Employee;
import com.kennen.yebserver.pojo.mail.MailConstants;
import com.kennen.yebserver.utils.RedisUtil;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/17 18:22
 * @Description: 邮件接收列
 */
@Component
public class MailReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);
    @Autowired private JavaMailSender javaMailSender;
    @Autowired private MailProperties mailProperties;
    @Autowired private TemplateEngine templateEngine;
    @Resource private RedisUtil redisUtil;
    
    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel){
        Employee employee = (Employee)message.getPayload();
        MessageHeaders headers = message.getHeaders();
        //  消息序号
        long tag = (long)headers.get(AmqpHeaders.DELIVERY_TAG);
        //  message ID，参考存的时候使用的 new CorrelationData(msgId)
        String msgId = (String)headers.get("spring_returned_message_correlation");
        
        try {
            if(redisUtil.hContains("mail_log",msgId)){
                LOGGER.error("消息已经被消费过============>{}",msgId);
                channel.basicAck(tag,false);
                return;
            }
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            //  发件人
            helper.setFrom(mailProperties.getUsername());
            //  收件人
            helper.setTo(employee.getEmail());
            //  主题
            helper.setSubject("入职欢迎邮件");
            //  发送日期
            helper.setSentDate(new Date());
            //  邮件内容
            Context context = new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("posName",employee.getPosition().getName());
            context.setVariable("jobLevelName",employee.getJoblevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());
            String mail = templateEngine.process("mail",context);
            helper.setText(mail,true);
            javaMailSender.send(mimeMessage);
            LOGGER.info("邮件发送成功");
            //  将消息id存入redis
            redisUtil.setHash("mail_log",msgId,"OK");
            //  手动确认消息
            channel.basicAck(tag,false);
        } catch (Exception e) {
            try {
                channel.basicNack(tag,false,true);
            } catch (IOException ioException) {
                LOGGER.error("邮件发送失败===============>{}",e.getMessage());
            }
            LOGGER.error("邮件发送失败===============>{}",e.getMessage());
        }
    }
}
