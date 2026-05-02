package com.example.mapper;

import com.example.entity.DrugShop;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DrugShopMapper {

    int insert(DrugShop drugShop);

    void updateById(DrugShop drugShop);

    void deleteById(Integer id);

    @Select("select * from `drug_shop` where id = #{id}")
    DrugShop selectById(Integer id);

    List<DrugShop> selectAll(DrugShop drugShop);

    @Select("select * from `drug_shop` where username = #{username}")
    DrugShop selectByUsername(String username);
}
