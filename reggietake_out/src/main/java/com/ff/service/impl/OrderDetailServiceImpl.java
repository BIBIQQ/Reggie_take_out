package com.ff.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.dao.OrderDetailDao;
import com.ff.entity.OrderDetail;
import com.ff.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:14:53
 */
@Service
public class OrderDetailServiceImpl  extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {
}
