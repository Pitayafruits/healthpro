package cn.cc.service;


import cn.cc.dao.CheckItemDao;
import cn.cc.entity.PageResult;
import cn.cc.entity.QueryPageBean;
import cn.cc.pojo.CheckItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 */

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    //新增检查项
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //检查项分页查询
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //通过分页助手插件完成分页查询
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckItem> result = page.getResult();
        return new PageResult(total,result);
    }

    //删除检查项
    public void deleteById(Integer id) {
        //判断当前检查项是否已经关联到检查组
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0){
            //当前检查项已经被关联到检查组,不允许删除
            new RuntimeException();
        }
        //执行删除
        checkItemDao.deleteById(id);


    }

    //编辑检查项
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }
    //编辑检查项前的数据回显
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    //新建检查组时的展示所有检查项
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}