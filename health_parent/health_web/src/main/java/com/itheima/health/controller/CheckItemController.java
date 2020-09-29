package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.CheckItemService;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:yintao
 * @Date: 2020.09.18  21:22
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    /* Restful
    @GetMapping, 查询数据 RequestMethod.GET
    @PutMapping  修改数据 RequestMethod.PUT
    @PostMapping  添加数据RequestMethod.POST
    @DeleteMapping 删除数据 RequestMethod.DELETE
    */

    /**
     * 查询所有的检查项
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll() {
        List<CheckItem> list = checkItemService.findAll();
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
    }

    /**
     * 添加检查项
     * @param checkItem
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        // 调用服务添加
        checkItemService.add(checkItem);
        //响应
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     *
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用业务服务分页查询
        PageResult<CheckItem> pageResult=checkItemService.findPage(queryPageBean);

        //返回结果
        return  new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);

    }
}
