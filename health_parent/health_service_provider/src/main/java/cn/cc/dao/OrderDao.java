package cn.cc.dao;

import cn.cc.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    //新增预约信息
    public void add(Order order);

    //预约信息查询
    public List<Order> findByCondition(Order order);

    //根据预约id查询预约信息
    public Map findById4Detail(Integer id);

    //根据日期查询预约人数
    public Integer findOrderCountByDate(String date);

    //根据当前日期查询后n个日期预约人数
    public Integer findOrderCountAfterDate(String date);

    //根据日期查询到诊人数
    public Integer findVisitsCountByDate(String date);

    //根据当前日期查询后n个日期的到诊人数
    public Integer findVisitsCountAfterDate(String date);

    //查询热门套餐
    public List<Map> findHotSetmeal();
}
