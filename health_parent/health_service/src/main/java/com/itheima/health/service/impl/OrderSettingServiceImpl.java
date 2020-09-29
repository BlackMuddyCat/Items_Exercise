package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.HealthException;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author:yintao
 * @Date: 2020.09.22  20:16
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入预约设置
     *
     * @param list
     */
    @Override
    public void add(List<OrderSetting> list) {
        //遍历集合
        for (OrderSetting orderSetting : list) {
            //通过日期查询预约设置的信息
            OrderSetting osInDb = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());

            //判断该日期是否已经设置预约信息
            if (null != osInDb) {
                //如果存在预约的信息（要设置的最大预约数量 < 已预约数量），判断修改值（list对象中【number】）是否小于t_ordersetting中对应的guo【reservations】的值
                if (orderSetting.getNumber() < osInDb.getReservations()) {
                    //是，则抛出异常
                    throw new HealthException("最大预约数量不能小于已预约数量");
                }
                //否，添加对象
                orderSettingDao.updateNumber(orderSetting);

            } else {
                //如果不存在预约的信息，添加预约的设置
                orderSettingDao.add(orderSetting);
            }
        }
    }
}
