package cn.cc.controller;

import cn.cc.constant.MessageConstant;
import cn.cc.constant.RedisMessageConstant;
import cn.cc.entity.Result;
import cn.cc.pojo.Order;
import cn.cc.service.OrderService;
import cn.cc.utils.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 体检预约处理
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    //预约处理
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        //从redis中取出保存的验证码
        String valiadateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        //对比用户输入的验证码与保存的验证码
        if (valiadateCodeInRedis != null && validateCode != null && valiadateCodeInRedis.equals(validateCode)){
            map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型
            Result result = null;
            try {
                //对比成功，调用服务完成预约业务处理
                result = orderService.order(map);
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()){
                //预约成功，为用户发送短信
                try {
                    SMSUtils.sendMessage(telephone,(String) map.get("orderDate"),SMSUtils.templateId_order);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return  result;
        }else {
            //对比不成功，给出提示信息
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    //根据预约id查询预约信息
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Map map = null;
        try{
            map = orderService.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
    }


}
