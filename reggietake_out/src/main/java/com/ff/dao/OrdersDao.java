package com.ff.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ff.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:14:49
 */
@Mapper
public interface OrdersDao  extends BaseMapper<Orders> {
}
