package cn.cc.controller;

import cn.cc.constant.MessageConstant;
import cn.cc.entity.Result;
import cn.cc.pojo.Setmeal;
import cn.cc.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户端套餐服务
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    //查询所有套餐信息
    @RequestMapping ("/getAllSetmeal")
    public Result getSetmeal(){
        List<Setmeal> setmealList = null;
        try{
            setmealList = setmealService.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
        return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmealList);
    }

    //根据套餐id查询套餐详细信息
    @RequestMapping ("/findById")
    public Result findById(Integer id){
        Setmeal setmeal = null;
        try{
            setmeal = setmealService.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
