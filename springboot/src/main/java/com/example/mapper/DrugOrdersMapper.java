package com.example.mapper;

import com.example.entity.DrugOrders;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DrugOrdersMapper {

    int insert(DrugOrders drugOrders);

    void updateById(DrugOrders drugOrders);

    void deleteById(Integer id);

    @Select("select * from `drug_orders` where id = #{id}")
    DrugOrders selectById(Integer id);

    List<DrugOrders> selectAll(DrugOrders drugOrders);

    @Select("select ifnull(sum(price), 0) from `drug_orders` where shop_id = #{shopId} and status = '已完成'")
    Double selectByShopId(Integer shopId);
}
