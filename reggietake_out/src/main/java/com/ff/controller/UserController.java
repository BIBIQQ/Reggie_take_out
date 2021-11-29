package com.ff.controller;

import com.ff.common.BaseContext;
import com.ff.common.Result;
import com.ff.doto.LoginDto;
import com.ff.entity.User;
import com.ff.service.UserService;
import com.ff.untils.CAPTCHA;
import com.ff.untils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FF
 * @date 2021/11/27
 * @TIME:16:07
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param request
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public Result sendMsg(HttpServletRequest request, @RequestBody User user){
        //生成一个验证码
        Integer integer = ValidateCodeUtils.generateValidateCode(6);
        //发送给手机
//        CAPTCHA.sendMsg(user.getPhone(),integer.toString());
        //  把验证码存储到 域中
        request.getSession().setAttribute(user.getPhone(),integer);
        log.info("验证码{}",integer);
        return Result.success("发送成功");
    }

    /**
     * 用户登录
     * @param loginDto
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto,HttpServletRequest request){
        Result result = userService.loginUser(loginDto);
        return result;
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public Result loginout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return Result.success("退出成功");
    }


}
