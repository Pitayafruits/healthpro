package cn.cc.controller;

import cn.cc.constant.MessageConstant;
import cn.cc.entity.PageResult;
import cn.cc.entity.QueryPageBean;
import cn.cc.entity.Result;
import cn.cc.pojo.CheckGroup;
import cn.cc.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查组管理
 */

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference//发现服务
    private CheckGroupService checkGroupService;

    //新增检查组
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkGroupService.findPage(queryPageBean);
    }

    //编辑页回显数据
    //回显检查组基本信息
    @RequestMapping("/findById")
    public Result findById(Integer id){
        CheckGroup checkGroup = null;
        try {
            checkGroup = checkGroupService.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }
    //回显检查组关联的检查项
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        List<Integer> checkItemIds = null;
        try {
            checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
    }

    //编辑检查组
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){

        try {
            checkGroupService.edit(checkGroup,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    //新建套餐时的全部检查组查询
    @RequestMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> checkGroupList = null;
        try {
            checkGroupList = checkGroupService.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
    }
}
