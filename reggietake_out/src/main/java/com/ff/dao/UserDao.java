package com.ff.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ff.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FF
 * @date 2021/11/27
 * @TIME:16:06
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
}
