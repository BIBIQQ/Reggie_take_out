package com.ff.controller;

import com.ff.common.Result;
import com.ff.entity.ShoppingCart;
import com.ff.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:9:52
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @return
     */
    @GetMapping("/list")
    public Result SClist() {
        Result result = shoppingCartService.shoppingCartList();
        return result;
    }

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        Result result = shoppingCartService.addShoppingCart(shoppingCart);
        return result;
    }

    /**
     * 删减购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public Result subShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        Result result = shoppingCartService.subShoppingCart(shoppingCart);
        return result;
    }

    @DeleteMapping("/clean")
    public Result cleanShoppingCart(){
        Result result = shoppingCartService.cleanShoppingCart();
        return  result;
    }
}
