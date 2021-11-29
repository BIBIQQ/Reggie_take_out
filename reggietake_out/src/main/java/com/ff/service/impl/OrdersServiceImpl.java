package com.ff.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.BaseContext;
import com.ff.common.Result;
import com.ff.dao.OrdersDao;
import com.ff.doto.OrdersDto;
import com.ff.entity.*;
import com.ff.exception.BusinessException;
import com.ff.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:14:51
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements OrdersService {

    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 下订单
     *
     * @param orders
     * @return
     */
    @Override
    public Result submitOrder(Orders orders) {
        //  获取用户id
        Long userId = BaseContext.getCurrentId();
        //获取用户信息
        User user = userService.getById(userId);
        //  获取购物车 明细
        LambdaQueryWrapper<ShoppingCart> sclqw = new LambdaQueryWrapper<>();
        sclqw.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(sclqw);
        if (shoppingCartList == null || shoppingCartList.size() < 0) {
            return Result.error("购物车内没有商品，不能下单");
        }

        //查询用户地址
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new BusinessException("用户地址信息有误，不能下单");
        }

        //  获取 订单号
        long orderId = IdWorker.getId();
        //  创建金额
        AtomicInteger amount = new AtomicInteger(0);
        // 封装存储订单详情表
        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setName(shoppingCart.getName());
            //  添加订单详情表
            orderDetailService.save(orderDetail);
            // 计算出  订单 总金额
            amount.addAndGet(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())).intValue());
        }

        //组装数据
        //组装订单数据
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2); //设置订单状态
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        // 保存  订单
        ordersDao.insert(orders);
        // 下单成功后  清空购物车
        shoppingCartService.cleanShoppingCart();
        return Result.success("下单成功");
    }

    /**
     * 历史订单展示
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Result userPage(Integer page, Integer pageSize) {
        //获取用户ID
        Long userId = BaseContext.getCurrentId();
        // 筛选用户id
        LambdaQueryWrapper<Orders> olqw = new LambdaQueryWrapper<>();
        olqw.eq(Orders::getUserId, userId);
        olqw.orderByDesc(Orders::getOrderTime);
        // 创建分页
        Page objectPage = new Page<>(page, pageSize);
        //查询
        Page page1 = ordersDao.selectPage(objectPage, olqw);
        // 获取  订单集合
        List<Orders> orders = page1.getRecords();
        //  深克隆到  OrdersDto
        List<OrdersDto> ordersDtoList = JSON.parseArray(JSON.toJSONString(orders), OrdersDto.class);
        //  搜索出 用户信息  进行赋值
        for (OrdersDto ordersDto : ordersDtoList) {
            //获取 订单号
            Long orderId = ordersDto.getId();
            //  查询 用户  下单 菜品详情
            LambdaQueryWrapper<OrderDetail> odlqw = new LambdaQueryWrapper<>();
            odlqw.eq(OrderDetail::getOrderId,orderId);
            List<OrderDetail> orderDetailList = orderDetailService.list(odlqw);
            //  菜单详情 赋值
            ordersDto.setOrderDetails(orderDetailList);
        }


        page1.setRecords(ordersDtoList);
        return Result.success(page1, "查询成功");
    }

    /**
     * 再来一单
     *
     * @param orders
     * @return
     */
    @Override
    public Result againOrders(Orders orders) {
        //获取 订单Id
        Long orderId = orders.getId();
        // 搜索 该 订单里面的  菜品详情
        LambdaQueryWrapper<OrderDetail> odlqw = new LambdaQueryWrapper<>();
        odlqw.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetailList = orderDetailService.list(odlqw);
        // 获取用户id
        Long userId = BaseContext.getCurrentId();
        //  获取  菜品  数量  添加到  购物车
        for (OrderDetail orderDetail : orderDetailList) {
            // 赋值
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setName(orderDetail.getName());
            shoppingCart.setImage(orderDetail.getImage());
            shoppingCart.setUserId(userId);
            shoppingCart.setDishId(orderDetail.getDishId());
            shoppingCart.setSetmealId(orderDetail.getSetmealId());
            shoppingCart.setDishFlavor(orderDetail.getDishFlavor());
            shoppingCart.setNumber(orderDetail.getNumber());
            shoppingCart.setAmount(orderDetail.getAmount());
            shoppingCart.setCreateTime(LocalDateTime.now());
            // 添加到购物车
            shoppingCartService.save(shoppingCart);
        }

        return Result.success("再来一单");
    }

    /**
     * 展示订单详情
     *
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Result findByPage(Integer page, Integer pageSize, String number, String beginTime, String endTime) {
        // 创建分页 构造器
        Page objectPage = new Page<>(page, pageSize);
        // 条件搜索
        LambdaQueryWrapper<Orders> olqw = new LambdaQueryWrapper<>();
        if (number != null) {
            olqw.eq(Orders::getNumber, number);
        }
        if (beginTime != null && endTime != null) {
            olqw.between(Orders::getOrderTime, beginTime, endTime);
        }
        //  查询出  订单详情
        Page page1 = ordersDao.selectPage(objectPage, olqw);
        List<Orders> orders = page1.getRecords();


        return Result.success(page1, "查询成功");
    }

    /**
     * 修改状态
     *
     * @param orders
     * @return
     */
    @Override
    public Result editStatus(Orders orders) {

        ordersDao.updateById(orders);
        return Result.success("修改成功");
    }
}
