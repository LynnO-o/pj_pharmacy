package com.example.controller;

import com.example.common.Result;
import com.example.entity.Forum;
import com.example.service.ForumService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/forum")
public class ForumController {

    @Resource
    private ForumService forumService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Forum forum) {
        forumService.add(forum);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Forum forum) {
        forumService.updateById(forum);
        return Result.success();
    }

    @PutMapping("/updateViewCount/{id}")
    public Result updateViewCount(@PathVariable Integer id) {
        forumService.updateViewCount(id);
        return Result.success();
    }


    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        forumService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        forumService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Forum forum = forumService.selectById(id);
        return Result.success(forum);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Forum forum) {
        List<Forum> list = forumService.selectAll(forum);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Forum forum,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Forum> pageInfo = forumService.selectPage(forum, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
