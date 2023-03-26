package cn.cc.service;

import cn.cc.dao.CheckGroupDao;
import cn.cc.entity.PageResult;
import cn.cc.entity.QueryPageBean;
import cn.cc.pojo.CheckGroup;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组服务
 */

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService{

    @Autowired
    private CheckGroupDao checkGroupDao;

    //新增检查组,同时检查组关联检查项关系
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项的多对多关联关系
        //获得自增id
        Integer checkGroupId = checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();//当前页
        Integer pageSize = queryPageBean.getPageSize();//每页显示的数据
        String queryString = queryPageBean.getQueryString();//查询条件
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //编辑页回显数据
    //检查组基本信息回显
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }
    //回显检查项复选框数据
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    //编辑检查组
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //清理当前检查组关联的检查项并为其重新关联新的检查项
        checkGroupDao.deleteAssociation(checkGroup.getId());
        this.setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        //修改检查组基本信息
        checkGroupDao.edit(checkGroup);
    }

    //新建套餐时显示所有检查组
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    //抽取的关联检查组与检查项的公共方法
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if (checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> checkMap = new HashMap<>();
                checkMap.put("checkGroupId",checkGroupId);
                checkMap.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(checkMap);
            }
        }
    }

}
