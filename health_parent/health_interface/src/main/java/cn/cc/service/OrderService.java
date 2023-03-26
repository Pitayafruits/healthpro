package cn.cc.service;

import cn.cc.entity.Result;

import java.util.Map;

/**
 * 体检预约服务接口
 */

public interface OrderService {

    //体检预约
    public Result order(Map map) throws Exception;

    ////根据预约id查询预约信息
    public Map findById(Integer id) throws Exception;
}
