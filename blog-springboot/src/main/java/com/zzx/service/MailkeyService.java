package com.zzx.service;

import com.zzx.dao.MailkeyDao;
import com.zzx.model.entity.MailMessage;
import com.zzx.model.pojo.Mailkey;
import com.zzx.utils.DateUtil;
import com.zzx.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MailkeyService {

    @Autowired
    private MailkeyDao mailkeyDao;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailMessage mailMessage;

    @Autowired
    private RandomUtil randomUtil;

    /**
     * 发送邮件
     *
     * @param mail
     */
    @Transactional
    public void sendMail(String mail) {
        //发送邮件
        Integer random = randomUtil.nextInt(100000, 999999);//貌似线程不安全 范围100000 - 999999
        mailSender.send(mailMessage.create(mail, "主题：邮箱验证码", "博客--邮箱验证码：" + random + "，五分钟内有效"));
        //保存发送记录
        mailkeyDao.deleteMailkeyByMail(mail);//删除原有记录 无论有没有
        Mailkey mailkey = new Mailkey();
        mailkey.setMail(mail);
        mailkey.setCode(random.toString());
        mailkey.setSendTime(dateUtil.getCurrentDate());
        mailkeyDao.saveMailkey(mailkey);
    }


    /**
     * 根据邮箱查询邮箱验证码
     *
     * @param mail
     * @return
     */
    public Mailkey findMailkeyByMail(String mail) {
        return mailkeyDao.findMailkeyByMail(mail);
    }
}
