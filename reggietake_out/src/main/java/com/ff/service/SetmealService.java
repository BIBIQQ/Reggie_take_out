package com.ff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.Result;
import com.ff.doto.SetmealDto;
import com.ff.entity.Setmeal;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FF
 * @date 2021/11/24
 * @TIME:15:44
 */
public interface SetmealService  extends IService<Setmeal> {
    /**
     * 动态  查询  分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    Result getSetmealByPage(int page, int pageSize, String name);

    /**
     * 修改订单状态
     * @param flag
     * @param ids
     * @return
     */
    Result updataStatusById(int flag, Long[] ids);

    /**
     * 修改回显信息
     * @param setmealId
     * @return
     */
    Result findById(Long setmealId);

    /**
     * 套餐 添加
     * @param setmealDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    Result addSetmeal(SetmealDto setmealDto);

    /**
     * 修改 套餐
     * @param setmealDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    Result editSetmeal(SetmealDto setmealDto);

    /**
     * 删除 套餐
     * @param ids
     * @return
     */
    Result deleteSetmeal(Long[] ids);

    /**
     * 展示套餐
     * @param categoryId
     * @param status
     * @return
     */
    Result setmealList(Long categoryId, Integer status);

    /**
     * 搜索  套餐下的菜品
     * @param setmealId
     * @return
     */
    Result findSetmealDish(Long setmealId);
}
