package cn.cc.controller;

import cn.cc.constant.MessageConstant;
import cn.cc.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录管理
 */

@RestController
@RequestMapping("/user")
public class UserController {

    //获得登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername(){
        //从框架提供的上下文对象中取得保存的用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }

}
