package com.ff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.Result;
import com.ff.doto.DishDto;
import com.ff.entity.Dish;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FF
 * @date 2021/11/24
 * @TIME:15:23
 */
public interface DishService  extends IService<Dish> {

    /**
     * 动态 条件 分页 查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    Result getDishByPage(int page, int pageSize, String name);

    /**
     * 修改菜品状态
     *
     * @param flag
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    Result updataStatusById(int flag, Long[] ids);

    /**
     * 添加 菜品
     * @param dishDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    Result addDish(DishDto dishDto);

    /**
     * 菜品   编辑  回显
     * @param id
     * @return
     */
    Result findById(Long id);

    /**
     * 编辑修改
     * @param dishDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    Result editDishById(DishDto dishDto);

    /**
     * 删除 菜品
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    Result deleteById(Long[] ids);
}
