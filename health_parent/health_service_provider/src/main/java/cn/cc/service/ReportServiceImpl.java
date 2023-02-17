package cn.cc.service;

import cn.cc.dao.MemberDao;
import cn.cc.dao.OrderDao;
import cn.cc.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService{

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;
    //运营信息统计
    public Map<String, Object> getBusinessReportData() throws Exception {
        /**
         * Map数据格式：
         *todayNewMember -> number
         *totalMember -> number
         *thisWeekNewMember -> number
         *thisMonthNewMember -> number
         *todayOrderNumber -> number
         *todayVisitsNumber -> number
         *thisWeekOrderNumber -> number
         *thisWeekVisitsNumber -> number
         *thisMonthOrderNumber -> number
         *thisMonthVisitsNumber -> number
         * hotSetmeal -> List<Setmeal>
         */
        //获得一些需要的日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());//当前日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());//本周一的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());//本月第一天的日期
        //今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount();
        //本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);
        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //今日到诊人数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        //本周预约人数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);
        //本周到诊人数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);
        //本月预约人数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        //本月到诊人数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);
        //热门套餐
        List<Map> hotSetmeal = orderDao.findHotSetmeal();
        //把结果封装到map集合
        Map<String,Object> result = new HashMap<>();
        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);
        return result;
    }
}
