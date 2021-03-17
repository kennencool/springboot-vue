package com.kennen.yebserver.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kennen.yebserver.config.RabbitMQ.RabbitMQConfig;
import com.kennen.yebserver.pojo.Employee;
import com.kennen.yebserver.pojo.MailSendLog;
import com.kennen.yebserver.pojo.mail.MailConstants;
import com.kennen.yebserver.service.IEmployeeService;
import com.kennen.yebserver.service.IMailSendLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/17 21:30
 * @Description: 邮件发送任务
 */
@Component
public class MailTask {
    @Resource private IMailSendLogService mailSendLogService;
    @Resource private IEmployeeService employeeService;
    @Resource private RabbitTemplate rabbitTemplate;
    
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask(){
        List<MailSendLog> mailSendLogList = mailSendLogService.list(new QueryWrapper<MailSendLog>()
                            .eq("status", 0)
                            .lt("tryTime", LocalDateTime.now()));
        mailSendLogList.forEach(mailSendLog -> {
            if(mailSendLog.getCount() >= 3){
                mailSendLogService.update(new UpdateWrapper<MailSendLog>()
                        .set("status","2")
                        .eq("msgId",mailSendLog.getMsgId()));
            }
            mailSendLogService.update(new UpdateWrapper<MailSendLog>()
                        .set("count",mailSendLog.getCount()+1)
                        .set("updateTime",LocalDateTime.now())
                        .set("tryTime",LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT)));
            Employee employee = employeeService.getEmployee(mailSendLog.getEmpId()).get(0);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY,
                    employee,new CorrelationData(mailSendLog.getMsgId()));
        });
    }
}
