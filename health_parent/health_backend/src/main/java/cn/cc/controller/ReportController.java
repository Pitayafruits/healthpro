package cn.cc.controller;

import cn.cc.constant.MessageConstant;
import cn.cc.entity.Result;
import cn.cc.service.MemberService;
import cn.cc.service.ReportService;
import cn.cc.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表统计管理
 */

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    //会员数量统计
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //map用来装返回的月份和每月会员数量，list用来装月份数组和会员数量数组
        Map<String,Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();
        //通过日历对象获取当前时间的前12个月的月份
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            Date date = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months",months);
        //根据月份查询会员数量
        List<Integer> memberCount = memberService.findmemberCountByMonths(months);
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    //套餐预约统计
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        Map<String,Object> map = null;
        try{
            //定义一个map集合来返回data
            map = new HashMap<>();
            //定义一个list集合来接收setmealCount
            List<Map<String,Object>> countList = setmealService.findSetmeal();
            map.put("setmealCount",countList);
            //定义一个list集合来接收setmealNames
            List<String> nameList = new ArrayList<>();
            for (Map<String, Object> sm : countList) {
                String name = (String) sm.get("name");
                nameList.add(name);
            }
            map.put("setmealNames",nameList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }

    //运营情况统计
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        Map<String,Object> businessMap = null;
        try {
            businessMap = reportService.getBusinessReportData();
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,businessMap);
    }

    //运营报表导出
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        ServletOutputStream out = null;
        XSSFWorkbook workbook = null;
        try {
            //Dubbo调用报表服务获取报表数据
            Map<String, Object> result = reportService.getBusinessReportData();
            //取出返回结果数据
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            //获得Excel模板文件绝对路径
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //读取模板文件创建Excel表格对象
            workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            //使用第一个工作表
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载,基于浏览器作为客户端下载
            out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//向客户端浏览器声明文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定客户端下载类型（附件）
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }finally {
            try {
                out.flush();
                out.close();
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
