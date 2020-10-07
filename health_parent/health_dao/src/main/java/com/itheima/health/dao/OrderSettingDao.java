package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:yintao
 * @Date: 2020.09.22  21:43
 */
public interface OrderSettingDao {
    /**
     * 通过日期查询预约设置信息
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 通过日期更新最大预约数
     * @param orderSetting
     */
    void updateNumber(OrderSetting orderSetting);

    /**
     * 插入预约设置
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     *  通过月份获取预约设置数据
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String, Integer>> getOrderSettingBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
