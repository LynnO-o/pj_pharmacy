package com.example.controller;

import com.example.common.Result;
import com.example.entity.GoodsOrders;
import com.example.service.GoodsOrdersService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/goodsOrders")
public class GoodsOrdersController {

    @Resource
    private GoodsOrdersService goodsOrdersService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody List<GoodsOrders> goodsOrdersList) {
        goodsOrdersService.add(goodsOrdersList);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody GoodsOrders goodsOrders) {
        goodsOrdersService.updateById(goodsOrders);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        goodsOrdersService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        goodsOrdersService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        GoodsOrders goodsOrders = goodsOrdersService.selectById(id);
        return Result.success(goodsOrders);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(GoodsOrders goodsOrders) {
        List<GoodsOrders> list = goodsOrdersService.selectAll(goodsOrders);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(GoodsOrders goodsOrders,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<GoodsOrders> pageInfo = goodsOrdersService.selectPage(goodsOrders, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
