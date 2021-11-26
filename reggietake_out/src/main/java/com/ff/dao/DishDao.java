package com.ff.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ff.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FF
 * @date 2021/11/24
 * @TIME:15:21
 */
@Mapper
public interface DishDao  extends BaseMapper<Dish> {
}
