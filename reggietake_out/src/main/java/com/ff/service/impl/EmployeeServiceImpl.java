package com.ff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.Result;
import com.ff.dao.EmployeeDao;
import com.ff.entity.Employee;
import com.ff.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * @author FF
 * @date 2021/11/21
 * @TIME:17:07
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee>  implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private HttpServletRequest request;

    /**
     * 管理员登录
     * @param employee
     * @return
     */
    @Override
    public Result login(Employee employee) {
        //MD5  不可逆计算  不可以解码
        //1. 获得登录的用户名和密码
        String username = employee.getUsername();
        String password = employee.getPassword();
        //2. 查询用户名是否存在
        LambdaQueryWrapper<Employee> emlqw = new LambdaQueryWrapper<>();
        emlqw.eq(Employee::getUsername,username);
        Employee employeeByUserName = employeeDao.selectOne(emlqw);
        if(employeeByUserName == null){
            return  Result.error("账号或者密码错误，请重新输入！");
        }
        //3. 加密的用户的密码成MD5  32位
        String Md5PW = DigestUtils.md5DigestAsHex(password.getBytes());
        //4. 查询用户名的密码是否匹配
        if(!employeeByUserName.getPassword().equals(Md5PW)){
            return  Result.error("账号或者密码错误，请重新输入！");
        }
        //5. 员工的是否禁用
        if(employeeByUserName.getStatus() == 0){
            return  Result.error("该账号禁用，请联系管理员！");
        }
        //6. 用户id存储到Session中
        HttpSession session = request.getSession();
        session.setAttribute("userId",employeeByUserName.getId());

        return Result.success(employeeByUserName,"登录成功");
    }

    /**
     * 员工新增
     * @param employee
     * @return
     */
    @Override
    public Result addEmployee(Employee employee) {
        // 校验参数
        if(employee == null){
            return  Result.error("添加失败，不能为空");
        }
        //判断注册的用户是否注册过
      /*  LambdaQueryWrapper<Employee> emlqw = new LambdaQueryWrapper<>();
        emlqw.eq(Employee::getUsername,employee.getUsername());
        Employee employeeByUserName = employeeDao.selectOne(emlqw);
        if(employeeByUserName != null){
            return Result.error("用户已经注册过，请重新输入！");
        }*/
        //设置创建时间 和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置创建人 和   修改人
        Long userId = (Long) request.getSession().getAttribute("userId");
        employee.setCreateUser(userId);
        employee.setUpdateUser(userId);
        //设置密码为MD5格式
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);

        // 添加用户
        int insert = employeeDao.insert(employee);
        return insert>0? Result.success("添加成功"):Result.error("添加失败");
    }

    /**
     * 员工展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Result findAllEmployeeByPage(int page, int pageSize, String name) {
        //设置name条件
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(name!=null,Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
         employeeDao.selectPage(pageInfo, queryWrapper);
        return Result.success(pageInfo,"查询成功");
    }

    /**
     * 修改状态  用户信息
     * @param employee
     * @return
     */
    @Override
    public Result updetaEmployee(Employee employee) {
        //判断传输进来的id是否存在用户
        Employee emp = employeeDao.selectById(employee.getId());
        if(emp == null){
            return Result.error("用户不存在");
        }
        //获得修改人
        Long userId = (Long) request.getSession().getAttribute("userId");
        //设置修改人
        employee.setUpdateUser(userId);
        //设置修改时间
        employee.setUpdateTime(LocalDateTime.now());
        employeeDao.updateById(employee);
        return  Result.success("修改成功");
    }


}
