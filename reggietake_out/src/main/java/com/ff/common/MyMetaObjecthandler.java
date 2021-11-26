package com.ff.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author FF
 * @date 2021/11/24
 * @TIME:9:18
 */

/**
 * 自定义原数据对象处理器
 */
@Component
public class MyMetaObjecthandler implements MetaObjectHandler {

    /**
     * 插入操作  自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentId = BaseContext.getCurrentId();
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser",currentId);
        metaObject.setValue("updateUser", currentId);
    }

    /**
     * 修改的时候操作   自动更新
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentId = BaseContext.getCurrentId();
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",currentId);
    }
}
