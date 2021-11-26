package com.ff.controller;

import com.ff.common.Result;
import com.ff.entity.Category;
import com.ff.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FF
 * @date 2021/11/19
 * @TIME:18:12
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

   @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result getByPage(Integer page,Integer pageSize){
        Result result = categoryService.getByPage(page, pageSize);
        return result;
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @PostMapping
    public Result addCategory(@RequestBody Category category){
        Result results = categoryService.addCategory(category);
        return results;
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result deleteById(Long id){
         Result results = categoryService.deleteById(id);
         return results;
    }

    /**
     * 回显
     * @param request
     * @return
     */
    @GetMapping("/findById")
    public Category findById(HttpServletRequest request){
        long id = Integer.parseInt(request.getParameter("id"));
        Category byId = categoryService.getById(id);
        return byId;
    }

    /**
     * 删除
     * @param category
     * @return
     */
    @PutMapping
    public Result updataById(@RequestBody Category category){
        Result result = categoryService.editById(category);
        return result;
    }

    @GetMapping("/list")
    public Result listByType(Integer type){
      Result result =  categoryService.listByType(type);
        return result;
    }
}
