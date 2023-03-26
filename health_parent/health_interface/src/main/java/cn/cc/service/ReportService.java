package cn.cc.service;

import java.util.Map;

/**
 * 运营管理接口
 */
public interface ReportService {

    //运营信息统计
    public Map<String,Object> getBusinessReportData() throws Exception;
}
