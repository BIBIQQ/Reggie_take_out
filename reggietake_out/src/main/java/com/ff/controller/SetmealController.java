package com.ff.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.ff.common.Result;
import com.ff.doto.SetmealDto;
import com.ff.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * @author FF
 * @date 2021/11/27
 * @TIME:9:11
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * 分页  多条件查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result getSetmealByPage(int page, int pageSize, String name) {
        Result result = setmealService.getSetmealByPage(page, pageSize, name);
        return result;
    }

    /**
     * 修改销售状态
     *
     * @param ids
     * @return
     */
    @CacheEvict(value = "setmealCache",allEntries = true)
    @PostMapping("/status/{flag}")
    public Result updataStatusByIds(@PathVariable int flag, Long[] ids) {
        Result result = setmealService.updataStatusById(flag, ids);
        return result;
    }

    /**
     * 套餐 修改 回显
     * @param setmealId
     * @return
     */
    @GetMapping("/{setmealId}")
    public Result findById(@PathVariable Long setmealId){
        Result result = setmealService.findById(setmealId);
        return result;
    }

    /**
     * 套餐添加
     * @param setmealDto
     * @return
     */
    @CacheEvict(value = "setmealCache",key = "#setmealDto.categoryId+'_'+#setmealDto.status")
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDto setmealDto){
       Result result = setmealService.addSetmeal(setmealDto);
       return result;
    }

    /**
     * 套餐  内容  修改
     * @param setmealDto
     * @return
     */
    @CacheEvict(value = "setmealCache",key = "#setmealDto.categoryId+'_'+#setmealDto.status")
    @PutMapping
    public Result editSetmeal(@RequestBody SetmealDto setmealDto){
       Result result = setmealService.editSetmeal(setmealDto);
        return  result;
    }

    /**
     * 删除 套餐
     * @param ids
     * @return
     */
    @CacheEvict(value = "setmealCache",allEntries = true)
    @DeleteMapping
    public Result deleteSetmeal(Long[] ids){
       Result result = setmealService.deleteSetmeal(ids);
       return result;
    }

    /**
     * 展示套餐
     * @param categoryId
     * @param status
     * @return
     */
    @Cacheable(value = "setmealCache",key = "#categoryId+'_'+#status")
    @GetMapping("/list")
    public Result setmealList(Long categoryId,Integer status){
        Result result =  setmealService.setmealList(categoryId,status);
        return result;
    }
    @Cacheable(value = "setmealCache",key = "#setmealId")
    @GetMapping("/dish/{setmealId}")
    public Result setmealDish(@PathVariable Long setmealId){
       Result result = setmealService.findSetmealDish(setmealId);
        return null;
    }
}
