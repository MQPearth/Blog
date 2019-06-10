package com.zzx.dao;

import com.zzx.model.pojo.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AnnouncementDao {

    /**
     * 保存公告
     *
     * @param announcement
     */
    void saveAnnouncement(Announcement announcement);

    /**
     * 更新公告删除状态
     *
     * @param announcementId
     */
    void deleteAnnouncementById(Integer announcementId);

    /**
     * 更新公告置顶状态
     *
     * @param announcement
     */
    void updateAnnouncementTop(Announcement announcement);

    /**
     * 获取公告数量
     *
     * @return
     */
    Long getAnnouncementCount();

    /**
     * 分页查询公告
     *
     * @param start
     * @param showCount
     * @return
     */
    List<Announcement> findAnnouncement(@Param("start") Integer start, @Param("showCount") Integer showCount);
}
