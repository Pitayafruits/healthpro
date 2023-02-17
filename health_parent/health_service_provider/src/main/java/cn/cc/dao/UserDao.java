package cn.cc.dao;

import cn.cc.pojo.User;

/**
 * 用户服务DAO
 */

public interface UserDao {

    //根据用户名查询用户信息
    public User findByUsername(String username);
}
