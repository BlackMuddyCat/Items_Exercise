package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Author:yintao
 * @Date: 2020.09.22  20:16
 */
public interface OrderSettingService {
    void add(List<OrderSetting> list);

    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    void editNumberByDate(OrderSetting os);

}
