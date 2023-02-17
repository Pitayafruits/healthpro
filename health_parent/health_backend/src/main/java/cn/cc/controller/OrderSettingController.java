package cn.cc.controller;

import cn.cc.constant.MessageConstant;
import cn.cc.entity.Result;
import cn.cc.pojo.OrderSetting;
import cn.cc.service.OrderSettingService;
import cn.cc.utils.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置
 */

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    //上传模板
    @RequestMapping("upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            //使用POI解析表格数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            //处理表格数据
            List<OrderSetting> dataList = new ArrayList<>();
            for (String[] strings : list) {
                String orderDate = strings[0];
                String number = strings[1];
                OrderSetting orderSetting = new OrderSetting(new Date(orderDate),new Integer(number));
                dataList.add(orderSetting);
            }
            //通过dubbo调用服务实现数据批量导入到数据库
            orderSettingService.add(dataList);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    //根据月份查询对应的预约设置数据
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        List<Map> mapList = null;
        try {
            mapList = orderSettingService.getOrderSettingByMonth(date);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,mapList);
    }

    //根据日期修改某一天的预约人数
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }

}
