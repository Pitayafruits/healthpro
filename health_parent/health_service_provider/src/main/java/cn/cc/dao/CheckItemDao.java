package cn.cc.dao;

import com.github.pagehelper.Page;
import cn.cc.pojo.CheckItem;

import java.util.List;

/**
 * 检查项DAO接口
 */
public interface CheckItemDao {
    //新增检查项
    public void add(CheckItem checkItem);

    //检查项分页查询
    public Page<CheckItem> selectByCondition(String queryString);

    //删除检查项
    public long findCountByCheckItemId(Integer id);
    public void deleteById(Integer id);

    //编辑检查项
    public void edit(CheckItem checkItem);
    //编辑检查项前的数据回显
    public CheckItem findById(Integer id);

    //新建检查组时的展示所有检查项
    public List<CheckItem> findAll();
}