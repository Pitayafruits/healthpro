package cn.cc.service;

import cn.cc.constant.RedisConstant;
import cn.cc.dao.SetmealDao;
import cn.cc.entity.PageResult;
import cn.cc.entity.QueryPageBean;
import cn.cc.pojo.Setmeal;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐服务实现类
 */

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;

    //新增套餐
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckgroup(setmealId,checkGroupIds);
        //将新增的套餐里的图片名称存入Redis中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);

    }

    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //用户端查询所有套餐
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    //根据套餐id查询套餐详细信息
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    //查询预约套餐种类占比情况
    public List<Map<String, Object>> findSetmeal() {
        return setmealDao.findSetmeal();
    }

    //设置套餐与检查组的多对多关系
    public void setSetmealAndCheckgroup(Integer setmealId,Integer[] checkGroupIds){
        if (checkGroupIds != null && checkGroupIds.length > 0){
            for (Integer checkGroupId : checkGroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkGroupId",checkGroupId);
                setmealDao.setSetmealAndCheckgroup(map);
            }
        }
    }

}
