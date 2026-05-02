package com.example.mapper;

import com.example.entity.GoodsOrders;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsOrdersMapper {

    int insert(GoodsOrders goodsOrders);

    void updateById(GoodsOrders goodsOrders);

    void deleteById(Integer id);

    @Select("select * from `goods_orders` where id = #{id}")
    GoodsOrders selectById(Integer id);

    List<GoodsOrders> selectAll(GoodsOrders goodsOrders);

    @Select("select ifnull(sum(price), 0) from `goods_orders` where shop_id = #{shopId} and status = '已完成'")
    Double selectByShopId(Integer shopId);
}
