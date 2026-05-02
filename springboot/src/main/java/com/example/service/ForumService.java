package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Forum;
import com.example.mapper.ForumMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class ForumService {

    @Resource
    private ForumMapper forumMapper;

    public void add(Forum forum) {
        Account currentUser = TokenUtils.getCurrentUser();
        forum.setUserId(currentUser.getId());
        forum.setTime(DateUtil.now());
        forum.setStatus("待审核");
        forum.setViewCount(0);
        forumMapper.insert(forum);
    }

    public void updateById(Forum forum) {
        Account currentUser = TokenUtils.getCurrentUser();
        // 用户编辑帖子 都重新设置待审核状态
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            forum.setStatus("待审核");
        }
        forumMapper.updateById(forum);
    }

    public void deleteById(Integer id) {

        forumMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            forumMapper.deleteById(id);
        }
    }

    public Forum selectById(Integer id) {
        return forumMapper.selectById(id);
    }

    public List<Forum> selectAll(Forum forum) {
        return forumMapper.selectAll(forum);
    }

    public PageInfo<Forum> selectPage(Forum forum, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && RoleEnum.USER.name().equals(currentUser.getRole())) {
            forum.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Forum> list = forumMapper.selectAll(forum);
        return PageInfo.of(list);
    }

    public void updateViewCount(Integer id) {
        forumMapper.updateViewCount(id);
    }
}
