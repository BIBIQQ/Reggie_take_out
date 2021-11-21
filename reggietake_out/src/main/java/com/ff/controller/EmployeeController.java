package com.ff.controller;

/**
 * @author FF
 * @date 2021/11/21
 * @TIME:17:08
 */

import com.ff.common.Result;
import com.ff.entity.Employee;
import com.ff.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 管理员登录
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result login( @RequestBody Employee employee){
        Result result  =employeeService.login(employee);
        return result;
    }
}
