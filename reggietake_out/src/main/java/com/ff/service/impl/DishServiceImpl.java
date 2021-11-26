package com.ff.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.Result;
import com.ff.dao.DishDao;
import com.ff.dao.DishFlavorDao;
import com.ff.doto.DishDto;
import com.ff.entity.Category;
import com.ff.entity.Dish;
import com.ff.entity.DishFlavor;
import com.ff.entity.SetmealDish;
import com.ff.exception.BusinessException;
import com.ff.service.CategoryService;
import com.ff.service.DishService;
import com.ff.service.SetmealDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FF
 * @date 2021/11/24
 * @TIME:15:23
 */

/**
 * 菜品管理
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService {

    @Autowired
    private DishDao dishDao;

    @Autowired
    private DishFlavorDao dishFlavorDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 动态 多条件 分页    查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Result getDishByPage(int page, int pageSize, String name) {
        //设置name条件
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行查询
        Page pageByDish = dishDao.selectPage(pageInfo, queryWrapper);
        //  获取Dish对象  重新封装到 DishDto 对象
        List<Dish> dishs = pageByDish.getRecords();
        //深克隆
        List<DishDto> dishDtos = JSON.parseArray(JSON.toJSONString(dishs),DishDto.class);

        //  Dish  复制到  dishDtos
//        BeanUtils.copyProperties(dishs, dishDtos);

        dishDtos = dishDtos.stream().map((dishDto) -> {
            // 获取分类
            Category categorybyId = categoryService.getById(dishDto.getCategoryId());
            //获取分类名字
            dishDto.setCategoryName(categorybyId.getName());
            return dishDto;
        }).collect(Collectors.toList());

        pageByDish.setRecords(dishDtos);

        return Result.success(pageByDish, "查询成功");
    }

    /**
     * 修改   菜品  状态
     *
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
            Dish dishByid = dishDao.selectById(id);
            if(dishByid == null){
                return Result.error("修改失败");
            }
            //修改  是否启用
            dishByid.setStatus(flag == 1? 1:0);
            dishDao.updateById(dishByid);
        }

        return Result.success("修改状态成功");
    }

    /**
     * 添加菜品
     *
     * @param dishDto
     * @return
     */
    @Override
    public Result addDish(DishDto dishDto) {
        //判断添加的菜品名字不重复
        String name = dishDto.getName();
        LambdaQueryWrapper<Dish> dlqw = new LambdaQueryWrapper<>();
        dlqw.eq(Dish::getName, name);
        Dish dish = dishDao.selectOne(dlqw);
        if (dish != null) {
            return Result.error("菜品不能重复");
        }

        //先添加菜品
        dishDao.insert(dishDto);
        //抽取出菜品的口味
        List<DishFlavor> dishflavors = dishDto.getFlavors();
        //抽取出  添加菜品的ID
        Long dishId = dishDto.getId();
        //添加菜品口味
        dishflavors.stream().forEach(dishflavor -> {
            dishflavor.setDishId(dishId);
            dishFlavorDao.insert(dishflavor);
        });
        return Result.success("添加成功");
    }

    /**
     * 编辑 回显
     * @param id
     * @return
     */
    @Override
    public Result findById(Long id) {
        //  校验参数是否正确
        if(id == null){
            throw  new BusinessException(404,"参数错误");
        }
        // 根据id 搜索 dish
        Dish dishById = dishDao.selectById(id);
        //  根据ID  搜索  dish flavor
        LambdaQueryWrapper<DishFlavor> dflqw = new LambdaQueryWrapper<>();
        dflqw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorDao.selectList(dflqw);
        DishDto dishDto = new DishDto();

        //  复制过去
        BeanUtils.copyProperties(dishById,dishDto);
        //  赋值dish口味
        dishDto.setFlavors(dishFlavors);
        return Result.success(dishDto,"回显成功");
    }

    /**
     * 修改 菜品信息
     * @param dishDto
     * @return
     */
    @Override
    public Result editDishById(DishDto dishDto) {
        //判断修改后的 菜品信息是否重复
        String name = dishDto.getName();
        LambdaQueryWrapper<Dish> dlqw = new LambdaQueryWrapper<>();
        dlqw.eq(Dish::getName, name);
        if(!dishDao.selectById(dishDto.getId()).getName().equals(name)){
            Dish dish = dishDao.selectOne(dlqw);
            if (dish != null) {
                return Result.error("菜品不能重复");
            }
        }

        //修改菜品
        dishDao.updateById(dishDto);
        //  修改口味    先删除 所有  再添加
        LambdaQueryWrapper<DishFlavor> dflqw = new LambdaQueryWrapper<>();
        dflqw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorDao.delete(dflqw);
        //  获取 修改后的口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //  添加 修改后的口味
        flavors.stream().forEach(dishflavor -> {
            dishflavor.setDishId(dishDto.getId());
            dishflavor.setId(null);
            dishFlavorDao.insert(dishflavor);
        });

        return Result.success("修改成功");
    }

    /**
     *  删除菜品
     * @param ids
     * @return
     */
    @Override
    public Result deleteById(Long[] ids) {
        LambdaQueryWrapper<DishFlavor> dflqw = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SetmealDish> sdlqw = new LambdaQueryWrapper<>();

        //  逻辑删除
        for (Long id : ids) {
            // 判断菜品是否在套餐存在
            sdlqw.eq(SetmealDish::getDishId,id);
            List<SetmealDish> list = setmealDishService.list(sdlqw);
            if(list.size() > 0){
                return  Result.error("套餐下还存在菜品，不允许删除");
            }
            //删除  菜品
            dishDao.deleteById(id);
            // 删除  菜品口味
            dflqw.eq(DishFlavor::getDishId,id);
            dishFlavorDao.delete(dflqw);
        }
        return Result.success("删除成功");
    }

}
