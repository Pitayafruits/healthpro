package cn.cc.dao;

import cn.cc.entity.PageResult;
import cn.cc.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * 套餐DAO
 */
public interface SetmealDao {

    //新增套餐
    //添加套餐信息
    public void add(Setmeal setmeal);
    //添加套餐关联的检查组id
    public void setSetmealAndCheckgroup(Map map);

    // 分页查询
    public Page<Setmeal> findByCondition(String queryString);

    //用户端查询所有套餐
    public List<Setmeal> findAll();

    //根据套餐id查询套餐详细信息
    public Setmeal findById(Integer id);

    //查询预约套餐种类占比情况
    public List<Map<String,Object>> findSetmeal();
}
