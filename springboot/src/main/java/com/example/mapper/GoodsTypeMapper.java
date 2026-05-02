package com.example.mapper;

import com.example.entity.GoodsType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsTypeMapper {

    int insert(GoodsType goodsType);

    void updateById(GoodsType goodsType);

    void deleteById(Integer id);

    @Select("select * from `goods_type` where id = #{id}")
    GoodsType selectById(Integer id);

    List<GoodsType> selectAll(GoodsType goodsType);

}
