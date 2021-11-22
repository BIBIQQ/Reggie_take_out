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
import org.springframework.web.bind.annotation.*;

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

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        request.getSession().removeAttribute("userId");
        return Result.success("退出成功");
    }

    /**
     * 员工添加
     * @param employee
     * @return
     */
    @PostMapping
    public Result addEmployee(@RequestBody Employee employee){
        Result result =  employeeService.addEmployee(employee);
        return result;
    }

    /**
     * 员工展示 动态 条件查询  分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result findAllEmployeeByPage(int page,int pageSize,String name){
        Result result = employeeService.findAllEmployeeByPage(page,pageSize,name);
        return result;
    }

    /**
     * 修改回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        System.out.println(id);
        Employee byId = employeeService.getById(id);
        System.out.println(byId);
        return Result.success(byId,"回显成功");
    }

    /**
     * 修改状态  修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public Result updetaEmployee(@RequestBody Employee employee){
        Result result = employeeService.updetaEmployee(employee);
        return result;
    }
}
