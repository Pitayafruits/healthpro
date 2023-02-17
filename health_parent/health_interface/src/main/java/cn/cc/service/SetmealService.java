package cn.cc.service;

import cn.cc.entity.PageResult;
import cn.cc.entity.QueryPageBean;
import cn.cc.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐服务接口
 */

public interface SetmealService {

    //增加新套餐
    public void add(Setmeal setmeal, Integer[] checkGroupIds);

    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean);

    //用户端查询所有套餐
    public List<Setmeal> findAll();

    //根据套餐id查询套餐详细信息
    public Setmeal findById(Integer id);

    //查询预约套餐种类占比情况
    public List<Map<String,Object>> findSetmeal();
}
