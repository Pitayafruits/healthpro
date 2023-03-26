package cn.cc.service;

import cn.cc.dao.OrderSettingDao;
import cn.cc.pojo.OrderSetting;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 预约设置服务接口实现类
 */

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService{

    @Autowired
    private OrderSettingDao orderSettingDao;

    //批量导入预约设置数据
    public void add(List<OrderSetting> dataList) {
        if (dataList != null && dataList.size() > 0){
            for (OrderSetting orderSetting : dataList) {
                //首先判断当前日期是否已经进行了预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate > 0){
                    //当天已经进行过预约设置，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //当天还未进行过预约设置，执行插入操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据月份查询对应的预约设置数据
    public List<Map> getOrderSettingByMonth(String date) {
        //设置封装时间参数
        String begin = date + "-1";
        String end = date + "-31";
        Map<String,String> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        //调用DAO，根据日期设置范围查询预约设置数据
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(map);
        //处理查询结果
        List<Map> result = new ArrayList<>();
        if (orderSettingList != null && orderSettingList.size() > 0){
            for (OrderSetting orderSetting : orderSettingList) {
                Map<String,Object> remap = new HashMap<>();
                remap.put("date",orderSetting.getOrderDate().getDate());
                remap.put("number",orderSetting.getNumber());
                remap.put("reservations",orderSetting.getReservations());
                result.add(remap);
            }
        }
        return result;
    }

    //根据日期修改某一天的预约人数
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        //根据日期查询是否已经进行了预约设置
        long count = orderSettingDao.findCountByOrderDate(orderDate);
        if (count > 0){
            //当前日期已进行了预约设置，需要执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //当前日期未进行预约设置，需要执行插入操作
            orderSettingDao.add(orderSetting);
        }
    }

}
