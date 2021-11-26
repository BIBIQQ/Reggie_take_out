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
//@ControllerAdvice
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

    @ExceptionHandler(SystemException.class)
    @ResponseBody
    public  Result doSystemException(SystemException ex){
        return  Result.error(ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public  Result doBusinessException(BusinessException ex){
        return  Result.error(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public  Result doRuntimeException(RuntimeException ex){
        return  Result.error("操作错误，请重新操作");
    }
}
