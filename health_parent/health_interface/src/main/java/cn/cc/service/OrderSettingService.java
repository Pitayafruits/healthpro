package cn.cc.service;

import cn.cc.pojo.OrderSetting;

import java.util.List;
import java.util.Map;


/**
 * 预约设置服务接口
 */

public interface OrderSettingService {

    //文件数据导入
    public void add(List<OrderSetting> dataList);

    //根据月份查询对应的预约设置数据
    public List<Map> getOrderSettingByMonth(String date);

    //根据日期修改某一天的预约人数
    public void editNumberByDate(OrderSetting orderSetting);
}
