package com.zzx.service;

import com.zzx.dao.BlogDao;
import com.zzx.dao.TagDao;
import com.zzx.dao.UserDao;
import com.zzx.model.pojo.Tag;
import com.zzx.model.pojo.User;
import com.zzx.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 新增标签
     *
     * @param tagName
     */
    public void saveTag(String tagName) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userDao.findUserByName(username);


        if (tagDao.findTagByTagName(tagName) != null) //mysql where tag_name 忽略大小写
            throw new RuntimeException("标签重复");

        Tag tag = new Tag();
        tag.setUser(user);
        tag.setName(tagName);
        tagDao.saveTag(tag);
    }

    /**
     * 删除标签
     *
     * @param tagId
     */
    @Transactional
    public void deleteTagById(Integer tagId) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userDao.findUserByName(username);
        Tag tag = tagDao.findTagById(tagId);
        if (user.getId() != tag.getUser().getId())
            throw new RuntimeException("无权删除此标签");

        //查询此标签下是否有博文
        if (blogDao.findBlogCountByTagId(tagId) > 0)
            throw new RuntimeException("此标签关联了博客");

        tagDao.deleteTag(tagId);
    }

    /**
     * 更改标签
     *
     * @param tagId
     * @param tagName
     */
    public void updateTag(Integer tagId, String tagName) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userDao.findUserByName(username);
        Tag tag = tagDao.findTagById(tagId);
        if (user.getId() != tag.getUser().getId())
            throw new RuntimeException("无权修改此标签");
        tag.setName(tagName);
        tagDao.updateTagName(tag);
    }

    /**
     * 查询该user下的所有标签
     */
    public List<Tag> findTagByUserId() {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userDao.findUserByName(username);
        return tagDao.findTagByUserId(user.getId());
    }
}
