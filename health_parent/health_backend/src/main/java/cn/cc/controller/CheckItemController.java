package cn.cc.controller;

import cn.cc.constant.MessageConstant;
import cn.cc.entity.PageResult;
import cn.cc.entity.QueryPageBean;
import cn.cc.entity.Result;
import cn.cc.pojo.CheckItem;
import cn.cc.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 检查项管理
 */

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    //新增检查项
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try{
            checkItemService.add(checkItem);//发送请求
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //检查项分页查询
    @RequestMapping("/findPage")
    public PageResult findpage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        return pageResult;
    }

    //删除检查项
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try{
            checkItemService.deleteById(id);//发送请求
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //编辑检查项
    @RequestMapping("/edit")
    public Result delete(@RequestBody CheckItem checkItem){
        try{
            checkItemService.edit(checkItem);//发送请求
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
    //编辑检查项时的数据回显
    @RequestMapping("/findById")
    public Result findById(Integer id){
        CheckItem checkItem = null;
        try{
            checkItem = checkItemService.findById(id);//发送请求
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    //新建检查组时的展示所有检查项
    @RequestMapping("/findAll")
    public Result findAll(){
        List<CheckItem> checkItemList = null;
        try{
            checkItemList = checkItemService.findAll();//发送请求
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
    }
}
