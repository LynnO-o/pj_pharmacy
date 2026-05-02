package com.example.mapper;

import com.example.entity.Slideshow;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SlideshowMapper {

    int insert(Slideshow slideshow);

    void updateById(Slideshow slideshow);

    void deleteById(Integer id);

    @Select("select * from `slideshow` where id = #{id}")
    Slideshow selectById(Integer id);

    List<Slideshow> selectAll(Slideshow slideshow);

}
