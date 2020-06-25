package com.zzx.mq;


import com.zzx.config.MailConfig;
import com.zzx.config.RabbitMqConfig;
import com.zzx.model.entity.MailMessage;
import com.zzx.service.UserService;
import com.zzx.utils.DateUtil;
import com.zzx.utils.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 发送邮件的队列消费者
 */
@Component
@RabbitListener(queues = RabbitMqConfig.MAIL_QUEUE)
@Slf4j
public class MailListener {


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private MailMessage mailMessage;


    @RabbitHandler
    public void executeSms(Map<String, String> map) {
        String mail = map.get("mail");
        String code = map.get("code");

        try {
//            this.sendMail(mail, code);
            Thread.sleep(6000);
            userService.updateMailSendState(mail, code, MailConfig.MAIL_STATE_OK);
            log.info(mail + "-" + code + "-发送成功");
        } catch (Exception e) {
            userService.updateMailSendState(mail, code, MailConfig.MAIL_STATE_ERROR);
            log.error(mail + code + "发送失败-" + e.getMessage());
        }
    }

    private void sendMail(String mail, String code) {
        //发送邮件
        mailSender.send(mailMessage
                .create(mail, "邮箱验证码", "邮箱验证码：" + code + "，" + MailConfig.EXPIRED_TIME + "分钟内有效"));

    }
}
