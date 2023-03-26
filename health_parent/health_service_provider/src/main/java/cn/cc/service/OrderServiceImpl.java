package cn.cc.service;

import cn.cc.constant.MessageConstant;
import cn.cc.dao.MemberDao;
import cn.cc.dao.OrderDao;
import cn.cc.dao.OrderSettingDao;
import cn.cc.entity.Result;
import cn.cc.pojo.Member;
import cn.cc.pojo.Order;
import cn.cc.pojo.OrderSetting;
import cn.cc.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务接口实现类
 */

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    //体检预约
    public Result order(Map map) throws Exception {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate"); //用户预约日期
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting == null){
            //指定日期没有进行预约设置，无法完成体检预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber(); //总预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = (String) map.get("telephone"); //获取用户输入的手机号
        Member member = memberDao.findByTelephone(telephone);
        if (member != null){
            //判断是否重复预约
            Integer id = member.getId(); //会员id
            Date order_Date = DateUtils.parseString2Date(orderDate); //预约日期
            String setmealId = (String) map.get("setmealId"); //套餐ID
            Order order = new Order(id,order_Date,Integer.parseInt(setmealId));
            //根据条件查询
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0){
                //说明用户在重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //5、预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId()); //会员ID
        order.setOrderDate(DateUtils.parseString2Date(orderDate)); //预约日期
        order.setOrderType((String) map.get("orderType")); //预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        //更新预约人数
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //根据预约id查询预约信息
    public Map findById(Integer id) throws Exception {
        Map ordermap = orderDao.findById4Detail(id);
        if (ordermap != null){
            //处理日期格式
            Date orderDate = (Date) ordermap.get("orderDate");
            ordermap.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return ordermap;
    }
}
