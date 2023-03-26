package cn.cc.dao;

import cn.cc.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置DAO
 */

public interface OrderSettingDao {

    //批量导入预约设置数据
    //若日期不存在的插入操作
    public void add(OrderSetting orderSetting);
    //若日期已存在的更新操作
    public void editNumberByOrderDate(OrderSetting orderSetting);
    //检查日期是否已经存在操作
    public long findCountByOrderDate(Date orderDate);

    //根据月份查询对应的预约设置数据
    public List<OrderSetting> getOrderSettingByMonth(Map map);

    //检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
    public OrderSetting findByOrderDate(Date orderDate);

    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);

}
