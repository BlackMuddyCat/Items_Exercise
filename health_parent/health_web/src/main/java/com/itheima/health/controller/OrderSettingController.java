package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:yintao
 * @Date: 2020.09.22  18:07
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    //添加日志
    private static final Logger log = LoggerFactory.getLogger(OrderSettingController.class);

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * @param excelFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            // 读取excel内容
            List<String[]> strings = POIUtils.readExcel(excelFile);

            //创建集合 【OrderSetting作为泛型让代码更有可读性，strings.size()指定容积的大小，避免扩容，提高代码的执行效率】
            List<OrderSetting> list = new ArrayList<OrderSetting>(strings.size());

            //声明变量
            OrderSetting os = null;

            //指定os对象中日期的格式
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);

            for (String[] strArr : strings) {
                //strArr 代表模板表格的一行记录 数组索引下表为0：日期；数组索引下表为1：数量
                //parse()将字符串解析为Date对象
                Date orderDate = sdf.parse(strArr[0]);

                //os对象为t_ordersetting表格的一行记录，将模板中代表【数量：可预订的人数】传入到os对象中；
                os = new OrderSetting(orderDate, Integer.valueOf(strArr[1]));

                //将实例化的os对象添加到集合中
                list.add(os);
            }
            //调用服务添加集合
            orderSettingService.add(list);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("批量导入失败", e);
        }
        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }

    /**
     * 通过月份获取预约设置数据
     * @param month
     * @return
     */
    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        List<Map<String,Integer>> monthData = orderSettingService.getOrderSettingByMonth(month);
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,monthData);
    }

    /**
     * 基于日期的预约设置
     * @param os
     * @return
     */
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting os){
        // 调用服务更新这个日期的设置
        orderSettingService.editNumberByDate(os);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
