package com.zzx.dao;

import com.zzx.model.pojo.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReplyDao {
    /**
     * 保存回复
     * @param reply
     */
    void saveReply(Reply reply);

    /**
     * 根据id查询回复
     * @param replyId
     * @return
     */
    Reply findReplyById(Integer replyId);

    /**
     * 根据id删除回复
     * @param replyId
     */
    void deleteReplyById(Integer replyId);

    /**
     * 根据评论id查询回复
     * @param id
     * @return
     */
    List<Reply> findReplyByDiscussId(Integer id);

    /**
     * 根据评论id删除回复
     * @param discussId
     * @return 受影响行数
     */
    Integer deleteReplyByDiscussId(Integer discussId);
}
