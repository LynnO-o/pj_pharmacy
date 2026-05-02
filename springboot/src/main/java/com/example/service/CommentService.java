package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
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
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    public void add(Comment comment) {
        Account currentUser = TokenUtils.getCurrentUser();
        comment.setUserId(currentUser.getId());
        comment.setTime(DateUtil.now());
        commentMapper.insert(comment);
        if (comment.getRootId() == null) {
            comment.setRootId(comment.getId());  // 根节点
            commentMapper.updateById(comment);
        }
    }

    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            commentMapper.deleteById(id);
        }
    }

    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }

    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            comment.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }

    public List<Comment> selectTree(Integer fid) {
        List<Comment> commentList = commentMapper.selectByFid(fid);
        // 没有 pid的评论 就是根节点评论
        List<Comment> rootList = commentList.stream().filter(comment -> comment.getPid() == null).toList();
        for (Comment root : rootList) {
            Integer rootId = root.getRootId();
            List<Comment> children = commentList.stream().filter(comment -> comment.getPid() != null && comment.getRootId().equals(rootId)).toList();
            root.setChildren(children);
        }
        return rootList;
    }

    public Integer selectCount(Integer fid) {
        List<Comment> commentList = commentMapper.selectByFid(fid);
        return commentList.size();
    }

    public void deepDelete(Integer id) {
        deleteAllChildren(id);
    }

    private void deleteAllChildren(Integer pid) {
        commentMapper.deleteById(pid);
        List<Comment> commentList = commentMapper.selectByPid(pid);
        if (commentList.isEmpty()) {
            return;
        }
        for (Comment comment : commentList) {
            deleteAllChildren(comment.getId());
        }
    }

}