package com.zzx.dao;

import com.zzx.model.pojo.Mailkey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MailkeyDao {

    /**
     * 保存邮件发送信息
     *
     * @param mailkey
     */
    void saveMailkey(Mailkey mailkey);


    /**
     * 根据mail查询mailkey
     *
     * @param mail
     * @return
     */
    Mailkey findMailkeyByMail(String mail);


    /**
     * 根据mail 删除 mailkey
     *
     * @param mail
     */
    void deleteMailkeyByMail(String mail);


    /**
     * 根据code和mail查询mailkey
     *
     * @param code
     * @param mail
     * @return
     */
    Mailkey findMailkeyByCodeAndMail(@Param("code") String code, @Param("mail") String mail);
}
