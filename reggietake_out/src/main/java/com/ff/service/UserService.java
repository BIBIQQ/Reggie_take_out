package com.ff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.Result;
import com.ff.doto.LoginDto;
import com.ff.entity.User;

/**
 * @author FF
 * @date 2021/11/27
 * @TIME:16:07
 */
public interface UserService  extends IService<User> {
    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    Result loginUser(LoginDto loginDto);
}
