package com.example.controller;

import com.example.common.Result;
import com.example.entity.GoodsType;
import com.example.service.GoodsTypeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/goodsType")
public class GoodsTypeController {

    @Resource
    private GoodsTypeService goodsTypeService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody GoodsType goodsType) {
        goodsTypeService.add(goodsType);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody GoodsType goodsType) {
        goodsTypeService.updateById(goodsType);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        goodsTypeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        goodsTypeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        GoodsType goodsType = goodsTypeService.selectById(id);
        return Result.success(goodsType);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(GoodsType goodsType) {
        List<GoodsType> list = goodsTypeService.selectAll(goodsType);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(GoodsType goodsType,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<GoodsType> pageInfo = goodsTypeService.selectPage(goodsType, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
