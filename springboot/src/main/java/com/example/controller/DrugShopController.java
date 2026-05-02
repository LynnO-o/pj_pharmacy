package com.example.controller;

import com.example.common.Result;
import com.example.entity.DrugShop;
import com.example.service.DrugShopService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/drugShop")
public class DrugShopController {

    @Resource
    private DrugShopService drugShopService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody DrugShop drugShop) {
        drugShopService.add(drugShop);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody DrugShop drugShop) {
        drugShopService.updateById(drugShop);
        return Result.success();
    }

    @PutMapping("/identify")
    public Result identify(@RequestBody DrugShop drugShop) {
        drugShopService.identify(drugShop);
        return Result.success();
    }
    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        drugShopService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        drugShopService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        DrugShop drugShop = drugShopService.selectById(id);
        return Result.success(drugShop);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(DrugShop drugShop) {
        List<DrugShop> list = drugShopService.selectAll(drugShop);
        return Result.success(list);
    }

    @GetMapping("/selectRank")
    public Result selectRank() {
        List<DrugShop> list = drugShopService.selectRank();
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(DrugShop drugShop,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<DrugShop> pageInfo = drugShopService.selectPage(drugShop, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
