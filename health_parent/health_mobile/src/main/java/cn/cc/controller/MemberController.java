package cn.cc.controller;

import cn.cc.constant.MessageConstant;
import cn.cc.constant.RedisMessageConstant;
import cn.cc.entity.Result;
import cn.cc.pojo.Member;
import cn.cc.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 用户管理
 */

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    //用户登录
    @RequestMapping("/login")
    public Result login(HttpServletResponse response,@RequestBody Map map){
        //对比用户输入的验证码与Redis中存储的验证码
        //先获取输入的手机号与验证码
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从Redis中获取验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCode != null && validateCodeInRedis != null && validateCode.equals(validateCodeInRedis)){
            //验证码验证正确
            //判断当前用户是会员
            Member member = memberService.findByTelephone(telephone);
            if (member == null){
                //该用户不是会员，把它注册为会员,添加一些必要的信息
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            //向客户端浏览器写入cookie中，内容为手机号，追踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);
            //将会员信息保存到Redis中
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else{
            //验证码校验错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }



}
