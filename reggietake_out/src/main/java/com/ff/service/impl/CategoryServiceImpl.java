package com.ff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.Result;
import com.ff.dao.CatagoryDao;
import com.ff.dao.DishDao;
import com.ff.dao.SetmealDao;
import com.ff.entity.Category;
import com.ff.entity.Dish;
import com.ff.entity.Employee;
import com.ff.entity.Setmeal;
import com.ff.exception.BusinessException;
import com.ff.service.CategoryService;
import com.ff.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.List;

/**
 * @author FF
 * @date 2021/11/19
 * @TIME:18:14
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CatagoryDao, Category> implements CategoryService {

    @Autowired
    private CatagoryDao catagoryDao;

    @Autowired
    private DishDao dishDao;

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 分页搜索
     * 默认 page 1  pageSize 5
     * @param page
     * @param pageSize
     * @return
     */
    public Result getByPage(Integer page, Integer pageSize) {
        Page pg = new Page(page, pageSize);
        LambdaQueryWrapper<Category> clqw = new LambdaQueryWrapper<>();
        //添加排序条件
        clqw.orderByAsc(Category::getSort);
        Page page1 = catagoryDao.selectPage(pg, clqw);
        return Result.success(page1,"查询成功");
    }

    /**
     * 添加分类
     *
     * @param category
     * @return
     */
    @Override
    public Result addCategory(Category category) {
        //校验参数
         if (category.getName() == null || category.getSort() == null || category.getType() == null) {
            return Result.error("参数错误重新输入");
        }

        //判断商品是否重复存在
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getName, category.getName());
        Category category1 = catagoryDao.selectOne(categoryLambdaQueryWrapper);
        if (category1 != null) {
            return Result.error("分类已经存在，请重新输入");
        }
        //添加商品
        catagoryDao.insert(category);

        return Result.success("添加成功");
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Result deleteById(Long id) {
        if (id == null) {
            return Result.error("参数错误,请重新输入");
        }
        //判断当前分类是否关联的了菜品，抛出异常
        LambdaQueryWrapper<Dish> dlqw = new LambdaQueryWrapper<>();
        dlqw.eq(Dish::getCategoryId, id);
        Integer dishByCgId = dishDao.selectCount(dlqw);
        if(dishByCgId>0){
            throw new BusinessException("当前分类有菜品关联，不可以删除");
        }
        //  判断当前分类是否关联了套餐，抛出异常
        LambdaQueryWrapper<Setmeal> slqw = new LambdaQueryWrapper<>();
        slqw.eq(Setmeal::getCategoryId,id);
        Integer stemealByCgId  = setmealDao.selectCount(slqw);
        if(stemealByCgId > 0){
            throw new BusinessException("当前分类有套餐关联，不可以删除");
        }

        // 判断id是否存在
        Category category = catagoryDao.selectById(id);
        if (category == null) {
            return Result.error("不存在");
        }

        catagoryDao.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @Override
    public Result editById(Category category) {
        catagoryDao.updateById(category);
        return Result.success("修改成功");
    }

    /**
     * 显示分类
     * @param type
     * @return
     */
    @Override
    public Result listByType(Integer type) {
        LambdaQueryWrapper<Category> clqw = new LambdaQueryWrapper<>();
        clqw.eq(Category::getType,type);

        List<Category> categories = catagoryDao.selectList(clqw);
        if(categories == null){
            throw  new BusinessException(404,"没有查询到分类");
        }
        return Result.success(categories,"查询成功");
    }

}
