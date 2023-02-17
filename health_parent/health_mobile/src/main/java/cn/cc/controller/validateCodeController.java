package cn.cc.controller;

import cn.cc.constant.MessageConstant;
import cn.cc.constant.RedisMessageConstant;
import cn.cc.entity.Result;
import cn.cc.utils.SMSUtils;
import cn.cc.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码操作
 */

@RestController
@RequestMapping("/validateCode")
public class validateCodeController {

    @Autowired
    private JedisPool jedisPool;

    //体检预约发送验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //随机生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //给用户发送验证码
        try{
            SMSUtils.sendMessage(telephone,validateCode.toString(),SMSUtils.templateId_validate);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //验证码保存到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //用户快速登录发送验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //随机生成6位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        //给用户发送验证码
        try{
            SMSUtils.sendMessage(telephone,validateCode.toString(),SMSUtils.templateId_validate);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //验证码保存到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


}
