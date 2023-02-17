package cn.cc.dao;

import cn.cc.pojo.Permission;

import java.util.Set;

/**
 * 权限服务接口
 */

public interface PermissionDao {

    //根据角色id查询权限信息
    public Set<Permission> findByRoleId(Integer roleID);
}
