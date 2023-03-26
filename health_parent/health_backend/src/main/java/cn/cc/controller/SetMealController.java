package cn.cc.controller;

import cn.cc.constant.MessageConstant;
import cn.cc.constant.RedisConstant;
import cn.cc.entity.PageResult;
import cn.cc.entity.QueryPageBean;
import cn.cc.entity.Result;
import cn.cc.pojo.Setmeal;
import cn.cc.service.SetmealService;
import cn.cc.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;


import java.io.IOException;
import java.util.UUID;

/**
 * 套餐管理
 */

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    //使用JedisPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;

    //图片上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile) {
        //获取原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        //使用UUID随机产生文件名称，防止同名文件覆盖
        String extention = originalFilename.substring(index - 1);
        String fileName = UUID.randomUUID().toString() + extention;
        //将文件上传到七牛云
        try {
            QiniuUtils.uploadFileQiniu(imgFile.getBytes(),fileName);
            //当用户上传图片后，将图片名称保存到redis的一个Set集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    @Reference
    private SetmealService setmealService;

    //新增套餐
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
        try{
            setmealService.add(setmeal,checkgroupIds);
        }catch (Exception e){
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
       return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.findPage(queryPageBean);
    }
}
