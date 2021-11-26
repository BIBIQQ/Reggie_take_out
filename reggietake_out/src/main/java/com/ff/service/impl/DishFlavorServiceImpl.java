package com.ff.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.dao.DishFlavorDao;
import com.ff.entity.DishFlavor;
import com.ff.service.DishFlavorService;
import com.ff.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @author FF
 * @date 2021/11/25
 * @TIME:20:37
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorDao,DishFlavor> implements DishFlavorService {
}
