package com.zzx.dao;

import com.zzx.model.pojo.Discuss;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论dao
 */
@Repository
@Mapper
public interface DiscussDao {
    /**
     * 保存回复
     *
     * @param discuss
     */
    void saveDiscuss(Discuss discuss);

    /**
     * 根据id查询评论
     *
     * @param discussId
     * @return
     */
    Discuss findDiscussById(Integer discussId);

    /**
     * 根据id删除评论
     *
     * @param discussId
     */
    void deleteDiscussById(Integer discussId);

    /**
     * 查询博文下的评论
     *
     * @param blogId
     * @return
     */
    List<Discuss> findDiscussByBlogId(@Param("blogId") Integer blogId, @Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 获取博文下评论数量
     *
     * @param blogId
     * @return
     */
    Long getDiscussCountByBlogId(Integer blogId);

    /**
     * 获取最新count 条评论
     *
     * @param count
     * @return
     */
    List<Discuss> findNewDiscuss(Integer count);

    /**
     * 查询
     *
     * @param userId    用户id
     * @param count 显示数量
     * @return
     */
    List<Discuss> findUserNewDiscuss(@Param("userId") Integer userId,@Param("count") Integer count);
}
