package com.zzx.dao;

import com.zzx.model.pojo.UserLike;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserLikeDao {

    /**
    * @Description: 保存点赞记录
    * @Param: [userLike]
    * @return: com.zzx.model.pojo.UserLike
    * @Author: Tyson
    * @Date: 2020/5/26/0026
    */
    void saveUserLike(UserLike userLike);

    /**
    * @Description: 获取点赞记录
    * @Param: [blogId, userId]
    * @return: com.zzx.model.pojo.UserLike
    * @Author: Tyson
    * @Date: 2020/5/30/0030 14:47
    */
    UserLike getUserLike(Integer blogId, Integer userId);
}