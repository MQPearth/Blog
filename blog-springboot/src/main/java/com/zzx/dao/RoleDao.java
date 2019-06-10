package com.zzx.dao;

import com.zzx.model.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoleDao {
    /**
     * 根据用户id查询角色
     * @param id
     * @return
     */
    List<Role> findUserRoles(Integer id);

    /**
     * 保存用户角色
     * @param uesrId
     * @param roleId
     */
    void saveUserRoles(@Param("userId") Integer uesrId,@Param("roleId") Integer roleId);

    /**
     * 根据角色名查询角色
     * @param name
     * @return
     */
    Role findRoleByName(String name);


    /**
     * 查询角色数量
     * 返回值建议使用Integer包装类型，Integer 默认值为null
     * @return
     */
    Integer findAdminRoleCount(String roleName);

    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAllRole();
}
