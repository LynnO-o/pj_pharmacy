package com.example.controller;

import com.example.common.Result;
import com.example.entity.Drug;
import com.example.service.DrugService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/drug")
public class DrugController {

    @Resource
    private DrugService drugService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Drug drug) {
        drugService.add(drug);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Drug drug) {
        drugService.updateById(drug);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        drugService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        drugService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Drug drug = drugService.selectById(id);
        return Result.success(drug);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Drug drug) {
        List<Drug> list = drugService.selectAll(drug);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Drug drug,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Drug> pageInfo = drugService.selectPage(drug, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
