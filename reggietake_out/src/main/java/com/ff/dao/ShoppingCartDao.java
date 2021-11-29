package com.ff.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ff.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:9:51
 */
@Mapper
public interface ShoppingCartDao  extends BaseMapper<ShoppingCart> {
}
