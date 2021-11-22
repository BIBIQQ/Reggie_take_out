package com.ff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.Result;
import com.ff.entity.Employee;

/**
 * @author FF
 * @date 2021/11/21
 * @TIME:17:06
 */
public interface EmployeeService  extends IService<Employee> {
    //  管理员登录
    Result login(Employee employee);
    //  新增员工
    Result addEmployee(Employee employee);
    //  员工展示
    Result findAllEmployeeByPage(int page, int pageSize, String name);
    //  修改状态
    Result updetaEmployee(Employee employee);
}
