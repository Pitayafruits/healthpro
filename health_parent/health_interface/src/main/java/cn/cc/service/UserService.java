package cn.cc.service;

import cn.cc.pojo.User;

/**
 * 用户服务接口
 */

public interface UserService {

    //根据用户名查询用户信息
    public User findByUsername(String username);
}
