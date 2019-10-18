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
     * 根据邮箱查询用户
     *
     * @param mail
     * @return
     */
    User findUserByMail(String mail);





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
     * 根据id 修改用户信息
     * @param user
     */
    void updateUser(User user);
}
