package com.ff.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ff.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:14:53
 */
@Mapper
public interface OrderDetailDao  extends BaseMapper<OrderDetail> {
}
