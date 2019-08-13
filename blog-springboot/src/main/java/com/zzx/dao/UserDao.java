package com.zzx.dao;

import com.zzx.model.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDao {

    /**
     * 根据用户名查询用户
     *
     * @param name
     * @return
     */
    User findUserByName(String name);

    /**
     * 保存用户
     *
     * @param user
     */
    void saveUser(User user);


    /**
     * 封禁或解禁用户
     *
     * @param user
     */
    void updateUserState(User user);


    /**
     * 根据邮箱查询用户
     *
     * @param mail
     * @return
     */
    User findUserByMail(String mail);


    /**
     * 根据用户id 更新密码
     * 需要id，password字段
     *
     * @param user
     */
    void updateUserPasswordById(User user);

    /**
     * 根据用户id更新邮箱
     * 需要id ,mail 两字段
     *
     * @param user
     */
    void updateUserMailById(User user);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    User findUserById(Integer id);

    /**
     * 根据用户名搜索用户
     *
     * @param userName
     * @return
     */
    List<User> searchUserByName(@Param("userName") String userName, @Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 查询用户数
     *
     * @return
     */
    Long getUserCount();

    /**
     * 分页查询用户
     *
     * @param start
     * @param showCount
     * @return
     */
    List<User> findUser(@Param("start") Integer start, @Param("showCount") Integer showCount);


    /**
     * 模糊查询用户名 返回记录数
     *
     * @param userName
     * @return
     */
    Long getUserCountByName(String userName);

    /**
     * 更新用户打赏码
     *
     * @param user
     */
    void updateUserRewardById(User user);
}
