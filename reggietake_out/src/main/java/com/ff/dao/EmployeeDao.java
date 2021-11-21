package com.ff.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ff.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FF
 * @date 2021/11/21
 * @TIME:17:05
 */
@Mapper
public interface EmployeeDao extends BaseMapper<Employee> {
}
