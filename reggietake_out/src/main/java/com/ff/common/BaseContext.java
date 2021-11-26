package com.ff.common;

/**
 * @author FF
 * @date 2021/11/24
 * @TIME:10:24
 */

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户的id
 */
public class BaseContext {

    private  static  ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    /**
     * 设置值
     * @param id
     */
    public  static  void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public  static  Long getCurrentId(){
        return threadLocal.get();
    }
}
