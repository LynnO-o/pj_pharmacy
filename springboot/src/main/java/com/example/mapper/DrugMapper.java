package com.example.mapper;

import com.example.entity.Drug;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DrugMapper {

    int insert(Drug drug);

    void updateById(Drug drug);

    void deleteById(Integer id);

    Drug selectById(Integer id);

    List<Drug> selectAll(Drug drug);

}
