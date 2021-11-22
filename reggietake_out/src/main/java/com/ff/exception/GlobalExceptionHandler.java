package com.ff.exception;

import com.ff.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author FF
 * @date 2021/11/22
 * @TIME:11:56
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 用户已经存在
     * 数据库验证越界异常
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public Result doSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex){

        return  Result.error("用户已经存在");
    }
}
