package com.ff.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.Result;
import com.ff.entity.Category;

import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FF
 * @date 2021/11/19
 * @TIME:18:14
 */

public interface CategoryService extends IService<Category> {
    /**
     * 分页
     * @param page
     * @param pageSize
     * @return
     */
    Result getByPage(Integer page, Integer pageSize);
    /**
     * 添加
     */
    Result addCategory(Category category);

    /**
     * 删除
     * @param id
     * @return
     */
    Result deleteById(Long id);

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    Result editById(Category category);

    /**
     * 显示分类
     * @param type
     * @return
     */
    Result listByType(Integer type);
}
