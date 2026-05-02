package com.example.controller;

import com.example.common.Result;
import com.example.entity.DrugOrders;
import com.example.service.DrugOrdersService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/drugOrders")
public class DrugOrdersController {

    @Resource
    private DrugOrdersService drugOrdersService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody DrugOrders drugOrders) {
        drugOrdersService.add(drugOrders);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody DrugOrders drugOrders) {
        drugOrdersService.updateById(drugOrders);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        drugOrdersService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        drugOrdersService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        DrugOrders drugOrders = drugOrdersService.selectById(id);
        return Result.success(drugOrders);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(DrugOrders drugOrders) {
        List<DrugOrders> list = drugOrdersService.selectAll(drugOrders);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(DrugOrders drugOrders,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<DrugOrders> pageInfo = drugOrdersService.selectPage(drugOrders, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
