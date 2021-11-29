package com.ff.controller;

import com.ff.common.Result;
import com.ff.doto.DishDto;
import com.ff.entity.Dish;
import com.ff.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author FF
 * @date 2021/11/25
 * @TIME:9:06
 */

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 动态 条件搜索  分页
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result getDishByPage(int page, int pageSize, String name) {
        Result result = dishService.getDishByPage(page, pageSize, name);
        return result;
    }

    /**
     * 添加 菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result addDish(@RequestBody DishDto dishDto) {
        Result result = dishService.addDish(dishDto);
        return result;
    }

    /**
     * 修改销售状态
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/{flag}")
    public Result updataStatusByIds(@PathVariable int flag, Long[] ids) {
        Result result = dishService.updataStatusById(flag, ids);
        return result;
    }

    /**
     * 编辑回显
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        Result result = dishService.findById(id);
        return result;
    }

    /**
     * 菜品编辑
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result editDishById(@RequestBody DishDto dishDto) {
        Result result = dishService.editDishById(dishDto);
        return result;
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result deleteById(Long[] ids){
      Result result =  dishService.deleteById(ids);
        return result;
    }

    /**
     * 通过 分类id搜索  菜品内容
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    private Result findByCategroyId(Long categoryId,Integer status){
        Result result = dishService.findByCategroyId(categoryId,status);
        return result;
    }
}
