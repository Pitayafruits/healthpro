package cn.cc.dao;

import cn.cc.pojo.Role;

import java.util.Set;

/**
 * 角色服务接口
 */

public interface RoleDao {

    //根据用户ID查询角色
    public Set<Role> findByUserId(Integer userId);
}
