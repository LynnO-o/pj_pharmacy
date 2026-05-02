package com.example.mapper;

import com.example.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper {

    int insert(Comment comment);

    void updateById(Comment comment);

    void deleteById(Integer id);

    @Select("select * from `comment` where id = #{id}")
    Comment selectById(Integer id);

    List<Comment> selectAll(Comment comment);

    List<Comment> selectByFid(Integer fid);

    @Select("select * from `comment` where pid = #{id}")
    List<Comment> selectByPid(Integer pid);
}
