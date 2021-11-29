package com.ff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.Result;
import com.ff.entity.ShoppingCart;

import java.util.List;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:9:52
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    Result addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 展示购物车
     *
     * @return
     */
    Result shoppingCartList();

    /**
     * 删减购物车
     *
     * @param shoppingCart
     * @return
     */
    Result subShoppingCart(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @return
     */
    Result cleanShoppingCart();
}
