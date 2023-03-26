package cn.cc.dao;

import cn.cc.pojo.CheckGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * 检查组DAO接口
 */

public interface CheckGroupDao {

    //新增检查组
    //增加检查组
    public void add(CheckGroup checkGroup);
    //设置检查组与检查项的多对多关系
    public void setCheckGroupAndCheckItem(Map map);

    //分页查询
    public Page<CheckGroup> findByCondition(String queryString);

    //编辑页回显数据
    //回显检查组基本信息
    public CheckGroup findById(Integer id);
    //回显检查项复选框数据
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    //编辑检查组
    //修改检查组基本信息
    public void edit(CheckGroup checkGroup);
    //清理当前检查组关联的检查项并为其重新关联新的检查项
    public void deleteAssociation(Integer id);

    //新建套餐时显示所有检查组
    public List<CheckGroup> findAll();
}
