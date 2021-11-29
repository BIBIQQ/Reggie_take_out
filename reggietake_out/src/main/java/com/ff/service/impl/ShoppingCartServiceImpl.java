package com.ff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.BaseContext;
import com.ff.common.Result;
import com.ff.dao.ShoppingCartDao;
import com.ff.entity.ShoppingCart;
import com.ff.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:9:53
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    /**
     * 添加菜品或套餐
     *
     * @param shoppingCart
     * @return
     */
    @Override
    public Result addShoppingCart(ShoppingCart shoppingCart) {
        // 获取用户id
        Long userId = BaseContext.getCurrentId();
        // 赋值添加用户id
        shoppingCart.setUserId(userId);
        //  获取套餐 和  菜品ID
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        //  判断添加的商品是否在购物车内存在
        LambdaQueryWrapper<ShoppingCart> sclqw = new LambdaQueryWrapper<>();
        sclqw.eq(ShoppingCart::getUserId, userId);
        if (dishId != null) {
            sclqw.eq(dishId != null, ShoppingCart::getDishId, dishId);
        }
        if (setmealId != null) {
            sclqw.eq(setmealId != null, ShoppingCart::getSetmealId, setmealId);
        }
        ShoppingCart shoppingCartByUserId = shoppingCartDao.selectOne(sclqw);
        if (shoppingCartByUserId != null) {
            //  菜品数量加1
            shoppingCartByUserId.setNumber(shoppingCartByUserId.getNumber() + 1);
            //  商品价格累加
//            shoppingCartByUserId.setAmount(shoppingCartByUserId.getAmount().add(shoppingCart.getAmount()));
            //修改  购物车 菜品或者 套餐数量
            shoppingCartDao.updateById(shoppingCartByUserId);
        } else {
            //  添加购物车
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartDao.insert(shoppingCart);
        }

        return Result.success(shoppingCart, "添加成功");
    }

    /**
     * 展示购物车
     *
     * @return
     */
    @Override
    public Result shoppingCartList() {
        //获取  用户购物车
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> sclqw = new LambdaQueryWrapper<>();
        sclqw.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartDao.selectList(sclqw);
        return Result.success(shoppingCarts, "展示成功");
    }

    /**
     * 删减购物车菜品
     *
     * @param shoppingCart
     * @return
     */
    @Override
    public Result subShoppingCart(ShoppingCart shoppingCart) {
        //获取 用户Id
        Long userId = BaseContext.getCurrentId();
        //抽取 菜品 和 套餐Id
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> sclqw = new LambdaQueryWrapper<>();
        sclqw.eq(ShoppingCart::getUserId, userId);
        //判断是菜品 或  套餐
        if (dishId != null) {
            sclqw.eq(ShoppingCart::getDishId, dishId);
        }
        if (setmealId != null) {
            sclqw.eq(ShoppingCart::getSetmealId, setmealId);
        }
        //  查询出 商品
        ShoppingCart shoppingCartById = shoppingCartDao.selectOne(sclqw);
        //  判断菜品的 数量是否为 1   如果为1 就删除  不为 1  就  减  1
        if (shoppingCartById.getNumber() == 1) {
            //删减购物车菜品
            shoppingCartDao.delete(sclqw);
            return  Result.success(shoppingCartById,"减少成功");
        }
        // 修改数量
        shoppingCartById.setNumber(shoppingCartById.getNumber() - 1);
        shoppingCartDao.updateById(shoppingCartById);
        return Result.success(shoppingCartById,"减少成功");
    }

    /**
     * 清空购物车
     * @return
     */
    @Override
    public Result cleanShoppingCart() {
        //获取 用户Id
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> sclqw = new LambdaQueryWrapper<>();
        sclqw.eq(ShoppingCart::getUserId, userId);
        //根据用户信息删除购物车
        shoppingCartDao.delete(sclqw);
        return Result.success("清空成功");
    }
}
