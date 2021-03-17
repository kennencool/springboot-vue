package com.kennen.yebmail;


import com.kennen.yebserver.pojo.mail.MailConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class YebMailApplication {
    public static void main(String[] args){
        SpringApplication.run(YebMailApplication.class, args);
    }
    
    //  创建队列
    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }
}
