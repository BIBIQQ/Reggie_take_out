package com.ff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.Result;
import com.ff.entity.Orders;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:14:50
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 下订单
     * @param orders
     * @return
     */
    Result submitOrder(Orders orders);

    /**
     * 历史订单的展示
     * @param page
     * @param pageSize
     * @return
     */
    Result userPage(Integer page, Integer pageSize);

    /**
     * 再来一单
     * @param orders
     * @return
     */
    Result againOrders(Orders orders);

    /**
     * 展示 订单 明细
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    Result findByPage(Integer page, Integer pageSize, String number, String beginTime, String endTime);

    /**
     * 修改状态
     * @param orders
     * @return
     */
    Result editStatus(Orders orders);
}
