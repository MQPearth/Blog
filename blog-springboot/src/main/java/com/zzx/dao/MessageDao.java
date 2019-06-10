package com.zzx.dao;


import com.zzx.model.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageDao {

    /**
     * 根据name查询留言
     *
     * @param name
     * @return
     */
    Message findMessageByName(String name);

    /**
     * 保存留言
     *
     * @param message
     */
    void saveMessage(Message message);

    /**
     * 根据id删除留言
     *
     * @param messageId
     */
    void deleteMessageById(Integer messageId);

    /**
     * 获取留言数量
     *
     * @return
     */
    Long getMessageCount();

    /**
     * 分页查询留言
     * @param start
     * @param showCount
     * @return
     */
    List<Message> findMessage(@Param("start") Integer start, @Param("showCount") Integer showCount);
}
