package com.zzx.mq;


import com.zzx.config.MailConfig;
import com.zzx.config.RabbitMqConfig;
import com.zzx.model.entity.MailMessage;
import com.zzx.utils.DateUtil;
import com.zzx.utils.LoggerUtil;
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
public class MailListener {

    private Logger logger = LoggerUtil.loggerFactory(this.getClass());



    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailMessage mailMessage;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RabbitHandler
    public void executeSms(Map<String, String> map) {
        String mail = map.get("mail");
        String code = map.get("code");


        try {
            this.sendMail(mail, code);
            logger.info(mail + "-" + code + "-发送成功");
        } catch (Exception e) {
            logger.error(mail + code + "发送失败-" + e.getMessage());
        }
    }

    private void sendMail(String mail, String code) {
        //发送邮件
        mailSender.send(mailMessage
                .create(mail, "邮箱验证码", "邮箱验证码：" + code + "，" + MailConfig.EXPIRED_TIME + "分钟内有效"));


    }
}
