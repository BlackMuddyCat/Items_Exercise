package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.service.CheckItemService;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author:yintao
 * @Date: 2020.09.18  22:05
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     *提供查询所有检查项的服务
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     *提供添加检查项的服务
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 提供分页查询检查项的服务
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        // 使用PageHelper分页插件
        // 检查配置了插件没有sqlMapConfig.xml  sqlSessionFactoryBean

        // 放入threadlocal 页码及大小 Page
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        // 判断是否有查询条件，有则要拼接%
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        //调用dao分页查询，返回一个Page对象，Page对象中封装了当前页【每条】数据（集合中装了许多个CheckItem的对象）
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());

        // web与service代码解耦
        PageResult<CheckItem> pageResult = new PageResult<>(page.getTotal(),page.getResult());

        return pageResult;

    }

}
