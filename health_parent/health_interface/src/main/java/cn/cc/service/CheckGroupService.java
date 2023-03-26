package cn.cc.service;

import cn.cc.entity.PageResult;
import cn.cc.entity.QueryPageBean;
import cn.cc.entity.Result;
import cn.cc.pojo.CheckGroup;

import java.util.List;


/**
 * 检查组服务接口
 */

public interface CheckGroupService {

    //新增检查组
    public void add(CheckGroup checkGroup,Integer[] checkitemIds);

    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean);

    //编辑页回显数据
    //回显检查组基本信息
    public CheckGroup findById(Integer id);
    //回显检查项复选框数据
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    //编辑检查组
    public void edit(CheckGroup checkGroup,Integer[] checkitemIds);

    //新建套餐时显示所有检查组
    public List<CheckGroup> findAll();
}
