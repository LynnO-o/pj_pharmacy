package com.example.mapper;

import com.example.entity.Cart;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CartMapper {

    int insert(Cart cart);

    void updateById(Cart cart);

    void deleteById(Integer id);

    @Select("select * from `cart` where id = #{id}")
    Cart selectById(Integer id);

    List<Cart> selectAll(Cart cart);

    @Select("select * from `cart` where user_id = #{userId} and goods_id = #{goodsId}")
    Cart selectByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId")Integer goodsId);

    @Select("delete from `cart` where user_id = #{userId} and goods_id = #{goodsId}")
    void deleteByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId")Integer goodsId);
}
