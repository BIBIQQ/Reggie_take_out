package com.ff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.RedisKeys;
import com.ff.common.Result;
import com.ff.dao.UserDao;
import com.ff.doto.LoginDto;
import com.ff.entity.User;
import com.ff.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Action;

/**
 * @author FF
 * @date 2021/11/27
 * @TIME:16:07
 */
@Service
public class UserServiceImpl  extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    @Override
    public Result loginUser(LoginDto loginDto) {
        //抽取出  参数
        String phone = loginDto.getPhone();
        String code = loginDto.getCode();
        // 取出域中的  验证码
        HttpSession session = request.getSession();
//        Integer Scode = (Integer) session.getAttribute(phone);
        // 取出缓存中的验证码
        String Scode = (String) redisTemplate.opsForValue().get(RedisKeys.RG_CODE + phone);
        //  校验 域中的 验证码
        if(null == Scode ){
            return  Result.error("登录失败，请先发送验证码");
        }
        // 对比验证码
        if(!Scode.toString().equals(code)){
            return  Result.error("登录失败，验证码匹配失败");
        }
        // 匹配成功，匹配该用户是否注册过  没有注册  自动注册
        LambdaQueryWrapper<User> ulqw = new LambdaQueryWrapper<>();
        ulqw.eq(User::getPhone,phone);
        User user = userDao.selectOne(ulqw);
        if(user == null){
            user = new User();
            // 注册用户信息
            user.setPhone(phone);
            userDao.insert(user);
        }
        //登录成功后需要吧 用户id存储到域中
        session.setAttribute("user",user.getId());
        //  去除缓存中的 验证码
        redisTemplate.delete(RedisKeys.RG_CODE + phone);
        return Result.success("登录成功");
    }
}
