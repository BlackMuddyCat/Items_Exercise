package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.HealthException;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * @Author:yintao
 * @Date: 2020.09.21  21:40
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 添加检查组
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 添加检查组
        checkGroupDao.add(checkGroup);

        // 获取检查组id
        Integer checkGroupId = checkGroup.getId();

        // 循环遍历选中检查项id，
        if(null != checkitemIds){
            for (Integer checkitemId : checkitemIds) {
                //添加检查组与检查项的关系
                checkGroupDao.addCheckGroupCheckItem(checkGroupId,checkitemId);
            }
        }
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
       //调用分页插件进行分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //判断是否是条件查询，如果是有条件查询则对查询的条件进行拼接
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 拼接 %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        // 模糊查询，条件查询
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());

        PageResult<CheckGroup> pageResult = new PageResult<CheckGroup>(page.getTotal(), page.getResult());

        return pageResult;
    }

    /**
     *根据id查询检查组的信息
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(int id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组的id查询出相对性检查项的id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 先更新检查组信息
        checkGroupDao.update(checkGroup);
        // 删除旧关系
        checkGroupDao.deleteCheckGroupCheckItem(checkGroup.getId());
        // 添加新关系
        if(null != checkitemIds){
            for (Integer checkitemId : checkitemIds) {
                //添加检查组与检查项的关系
                checkGroupDao.addCheckGroupCheckItem(checkGroup.getId(),checkitemId);
            }
        }
    }

    /**
     * 删除检查组
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) {
        // 判断该检查组是否被套餐使用
        int count = checkGroupDao.findSetmealCountByCheckGroupId(id);
        // 如果检查组被套餐使用就不能删除，需要抛出异常
        if(count > 0){
            throw new HealthException("访检查组已经被套餐使用了，不能删除");
        }
        // 如果检查组未被套餐使用
        // 先删除检查组与检查项关系
        checkGroupDao.deleteCheckGroupCheckItem(id);
        // 再删除检查组
        checkGroupDao.deleteById(id);
    }


}
