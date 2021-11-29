package com.ff.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ff.common.Result;
import com.ff.doto.OrdersDto;
import com.ff.entity.Orders;
import com.ff.service.OrdersService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:14:50
 */
@RequestMapping("/order")
@RestController
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 下订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result submitOrder(@RequestBody Orders orders){
        Result result =   ordersService.submitOrder(orders);
        return result;
    }

    /**
     * 历史订单展示
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public  Result userPage(Integer page, Integer pageSize){
        Result result = ordersService.userPage(page,pageSize);
        return result;
    }

    /**
     * 再来一单
     * @param orders
     * @return
     */
    @PostMapping("/again")
    public  Result againOrders(@RequestBody Orders orders){
      Result result  =  ordersService.againOrders(orders);
      return result;
    }

    /**
     * 展示  订单  明细
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public Result findByPage(Integer page, Integer pageSize, String number, String beginTime, String endTime){
        Result result  =  ordersService.findByPage(page,pageSize,number,beginTime,endTime);
        return result;
    }

    /**
     * 修改状态
     * @param orders
     * @return
     */
    @PutMapping
    public Result editStatus(@RequestBody Orders orders){
       Result result  = ordersService.editStatus(orders);
        return Result.success("修改成功");
    }
}
