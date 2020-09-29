package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

/**
 * @Author:yintao
 * @Date: 2020.09.25  21:01
 */
public interface SetmealDao {

    void add(Setmeal setmeal);

    void addSetmealCheckgroup(Integer setmealId, Integer checkgroupId);

    Page<Setmeal> findByCondition(String queryString);

    Setmeal findById(int id);

    List<Integer> findCheckGroupIdsBySetmealId(int id);

    void update(Setmeal setmeal);

    void deleteSetmealCheckGroup(Integer id);

    int findCountBySetmealId(int id);

    void deleteById(int id);
}
