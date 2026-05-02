package com.example.controller;

import com.example.common.Result;
import com.example.entity.DrugType;
import com.example.service.DrugTypeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/drugType")
public class DrugTypeController {

    @Resource
    private DrugTypeService drugTypeService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody DrugType drugType) {
        drugTypeService.add(drugType);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody DrugType drugType) {
        drugTypeService.updateById(drugType);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        drugTypeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        drugTypeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        DrugType drugType = drugTypeService.selectById(id);
        return Result.success(drugType);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(DrugType drugType) {
        List<DrugType> list = drugTypeService.selectAll(drugType);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(DrugType drugType,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<DrugType> pageInfo = drugTypeService.selectPage(drugType, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
