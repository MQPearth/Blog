package com.zzx.service;

import com.zzx.dao.AnnouncementDao;
import com.zzx.model.pojo.Announcement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementDao announcementDao;

    public void saveAnnouncement(String announcementTitle, String announcementBody) {
        announcementBody = announcementBody.replaceAll("\n", "<br />");
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementTitle);
        announcement.setBody(announcementBody);
        announcement.setTop(1);
        announcementDao.saveAnnouncement(announcement);
    }

    /**
     * 根据公告id删除公告
     *
     * @param announcementId
     */
    public void deleteAnnouncementById(Integer announcementId) {
        announcementDao.deleteAnnouncementById(announcementId);
    }


    /**
     * 置顶或取消置顶公告
     *
     * @param announcementId
     * @param top
     */
    public void updateAnnouncementTop(Integer announcementId, Integer top) {
        Announcement announcement = new Announcement();
        announcement.setId(announcementId);
        announcement.setTop(top);
        announcementDao.updateAnnouncementTop(announcement);
    }


    /**
     * 获取公告数量
     *
     * @return
     */
    public Long getAnnouncementCount() {
        return announcementDao.getAnnouncementCount();
    }

    /**
     * 分页查询公告
     *
     * @param page
     * @param showCount
     * @return
     */
    public List<Announcement> findAnnouncement(Integer page, Integer showCount) {
        return announcementDao.findAnnouncement((page - 1) * showCount, showCount);
    }
}
