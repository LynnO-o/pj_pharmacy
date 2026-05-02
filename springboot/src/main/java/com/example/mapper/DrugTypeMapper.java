package com.example.mapper;

import com.example.entity.DrugType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DrugTypeMapper {

    int insert(DrugType drugType);

    void updateById(DrugType drugType);

    void deleteById(Integer id);

    @Select("select * from `drug_type` where id = #{id}")
    DrugType selectById(Integer id);

    List<DrugType> selectAll(DrugType drugType);

}
