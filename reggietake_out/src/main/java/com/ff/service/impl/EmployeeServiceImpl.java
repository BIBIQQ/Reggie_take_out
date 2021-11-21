package com.ff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.Result;
import com.ff.dao.EmployeeDao;
import com.ff.entity.Employee;
import com.ff.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
}
