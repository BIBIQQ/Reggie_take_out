package com.ff.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.Result;
import com.ff.dao.SetmealDao;
import com.ff.doto.DishDto;
import com.ff.doto.SetmealDto;
import com.ff.entity.Category;
import com.ff.entity.Dish;
import com.ff.entity.Setmeal;
import com.ff.entity.SetmealDish;
import com.ff.exception.BusinessException;
import com.ff.service.CategoryService;
import com.ff.service.DishService;
import com.ff.service.SetmealDishService;
import com.ff.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FF
 * @date 2021/11/24
 * @TIME:15:45
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    /**
     * 分页 多条件搜索
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Result getSetmealByPage(int page, int pageSize, String name) {
        //  判断 name 是否为空
        LambdaQueryWrapper<Setmeal> slqw = new LambdaQueryWrapper<>();
        slqw.eq(name != null, Setmeal::getName, name);
        // 创建分页构造器
        Page<Setmeal> sPage = new Page<>(page, pageSize);
        //分页查询菜品
        Page setmealByPage = setmealDao.selectPage(sPage, slqw);
        //  获取套餐信息
        List<Setmeal> setmealList = setmealByPage.getRecords();
        //  创建一个  SetmealDto    //  深克隆
        List<SetmealDto> setmealDtos = JSON.parseArray(JSON.toJSONString(setmealList), SetmealDto.class);
        //  设置 套餐分类名称
        setmealDtos = setmealDtos.stream().map(setmealDto -> {
            Category byId = categoryService.getById(setmealDto.getCategoryId());
            setmealDto.setCategoryName(byId.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        //设置Page
        setmealByPage.setRecords(setmealDtos);
        return Result.success(setmealByPage, "查询成功");
    }

    /**
     * 修改订单状态
     *
     * @param flag
     * @param ids
     * @return
     */
    @Override
    public Result updataStatusById(int flag, Long[] ids) {
        //判断参数是否准确
        if (ids.length < 1 || flag != 1 && flag != 0) {
            throw new BusinessException(404, "参数错误");
        }

        //  修改状态
        for (Long id : ids) {
            Setmeal setmeal = setmealDao.selectById(id);
            if (setmeal == null) {
                return Result.error("修改失败");
            }
            //修改  是否启用
            setmeal.setStatus(flag == 1 ? 1 : 0);
            setmealDao.updateById(setmeal);
        }

        return Result.success("修改状态成功");
    }

    /**
     * 修改回显
     *
     * @param setmealId
     * @return
     */
    @Override
    public Result findById(Long setmealId) {
        if (setmealId == null) {
            throw new BusinessException(404, "参数错误");
        }
        //搜索套餐信息
        Setmeal setmeal = setmealDao.selectById(setmealId);
        SetmealDto setmealDto = new SetmealDto();
        //复制过去
        BeanUtils.copyProperties(setmeal, setmealDto);
        //搜索 套餐菜品信息
        LambdaQueryWrapper<SetmealDish> sdlqw = new LambdaQueryWrapper<>();
        sdlqw.eq(SetmealDish::getSetmealId, setmealId);
        List<SetmealDish> setmealDishList = setmealDishService.list(sdlqw);
        // 菜品信息赋值
        setmealDto.setSetmealDishes(setmealDishList);
        //反馈结果
        return Result.success(setmealDto, "显示成功");
    }

    /**
     * 添加 套餐
     *
     * @param setmealDto
     * @return
     */
    @Override
    public Result addSetmeal(SetmealDto setmealDto) {
        //  判断添加的套餐信息是否重复
        String name = setmealDto.getName();
        LambdaQueryWrapper<Setmeal> sdlqw = new LambdaQueryWrapper<>();
        sdlqw.eq(Setmeal::getName, name);
        Setmeal setmealByName = setmealDao.selectOne(sdlqw);
        if (setmealByName != null) {
            return Result.error("套餐名称不能够重复");
        }
        //  添加  套餐
        setmealDao.insert(setmealDto);
        //  获取  添加套餐的ID
        Long setmeaId = setmealDto.getId();
        //  获取 添加菜品信息  校验 数据信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        if (setmealDishes.size() < 2) {
            throw new BusinessException("最少需要添加两个菜品内容");
        }
        //  校验  添加的菜品是否存在
        //  获取  添加菜品id
        for (SetmealDish setmealDish : setmealDishes) {
            Dish byId = dishService.getById(setmealDish.getDishId());
            setmealDish.setSetmealId(setmeaId);
            if (byId == null) {
                throw new BusinessException("添加的菜品不存在");
            }
            //  校验  添加的菜品是否是停售
            if (byId.getStatus() == 0) {
                throw new BusinessException("添加的菜品停售，请重新添加");
            }
            //  添加  菜品和 套餐的关系表
            setmealDishService.save(setmealDish);
        }
        return Result.success("添加成功");
    }

    /**
     * 修改套餐
     *
     * @param setmealDto
     * @return
     */
    @Override
    public Result editSetmeal(SetmealDto setmealDto) {
        //  修改  套餐
        try {
            setmealDao.updateById(setmealDto);
        } catch (Exception e) {
            throw new BusinessException("不能够重复");
        }

        //  获取  添加套餐的ID
        Long setmeaId = setmealDto.getId();

        //  删除  套餐内的菜品
        LambdaQueryWrapper<SetmealDish> sdlqw = new LambdaQueryWrapper<>();
        sdlqw.eq(SetmealDish::getSetmealId, setmeaId);
        setmealDishService.remove(sdlqw);

        //  添加  套餐内的菜品
        //  获取 添加菜品信息  校验 数据信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        if (setmealDishes.size() < 2) {
            throw new BusinessException("最少需要添加两个菜品内容");
        }

        //  校验  添加的菜品是否存在
        //  获取  添加菜品id
        for (SetmealDish setmealDish : setmealDishes) {
            Dish byId = dishService.getById(setmealDish.getDishId());
            setmealDish.setSetmealId(setmeaId);
            if (byId == null) {
                throw new BusinessException("添加的菜品不存在");
            }
            //  校验  添加的菜品是否是停售
            if (byId.getStatus() == 0) {
                throw new BusinessException("添加的菜品停售，请重新添加");
            }
            //  添加  菜品和 套餐的关系表
            setmealDishService.save(setmealDish);
        }
        return Result.success("修改成功");
    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @Override
    public Result deleteSetmeal(Long[] ids) {
        if (ids.length < 0) {
            throw new BusinessException(404, "参数错误");
        }
        LambdaQueryWrapper<SetmealDish> sdlqw = new LambdaQueryWrapper<>();
        // 抽取出 套餐id
        for (Long id : ids) {
            //  校验 套餐id存在
            Setmeal setmeal = setmealDao.selectById(id);
            if (setmeal == null) {
                throw new BusinessException("删除的套餐不存在");
            }
            //删除套餐
            setmealDao.deleteById(id);
            // 删除 套餐中的 菜品的关系
            sdlqw.eq(SetmealDish::getSetmealId, id);
            setmealDishService.remove(sdlqw);
        }
        return Result.success("删除成功");
    }

    /**
     * 套餐展示
     *
     * @param categoryId
     * @param status
     * @return
     */
    @Override
    public Result setmealList(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> slqw = new LambdaQueryWrapper<>();
        slqw.eq(categoryId != null, Setmeal::getCategoryId, categoryId).eq(status != null, Setmeal::getStatus, status);
        List<Setmeal> setmealList = setmealDao.selectList(slqw);
        return Result.success(setmealList, "查询成功");
    }

    /**
     * 搜索套餐下的菜品
     * @param setmealId
     * @return
     */
    @Override
    public Result findSetmealDish(Long setmealId) {
        LambdaQueryWrapper<SetmealDish> sdlqw = new LambdaQueryWrapper<>();
        sdlqw.eq(setmealId!= null,SetmealDish::getSetmealId,setmealId);
        List<SetmealDish> setmealDishlist = setmealDishService.list(sdlqw);
        List<Dish> dishDtoList = new ArrayList<>();
        for (SetmealDish setmealDish : setmealDishlist) {
            Long dishId = setmealDish.getDishId();
            Dish byId = dishService.getById(dishId);
            dishDtoList.add(byId);

        }
        return Result.success(dishDtoList,"查询成功");
    }


}
