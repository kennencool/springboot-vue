package com.kennen.yebmail;

import com.kennen.yebserver.pojo.Employee;
import com.kennen.yebserver.pojo.mail.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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
    
    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Employee employee){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
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
        } catch (MessagingException e) {
            LOGGER.error("邮件发送失败===============>{}",e.getMessage());
        }
    }
}
