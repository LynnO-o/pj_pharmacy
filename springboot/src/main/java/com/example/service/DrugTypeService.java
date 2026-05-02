package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.DrugType;
import com.example.mapper.DrugTypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class DrugTypeService {

    @Resource
    private DrugTypeMapper drugTypeMapper;

    public void add(DrugType drugType) {
        //drugType.setTime(DateUtil.now());
        drugTypeMapper.insert(drugType);
    }

    public void updateById(DrugType drugType) {
        drugTypeMapper.updateById(drugType);
    }

    public void deleteById(Integer id) {

        drugTypeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            drugTypeMapper.deleteById(id);
        }
    }

    public DrugType selectById(Integer id) {
        return drugTypeMapper.selectById(id);
    }

    public List<DrugType> selectAll(DrugType drugType) {
        return drugTypeMapper.selectAll(drugType);
    }

    public PageInfo<DrugType> selectPage(DrugType drugType, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DrugType> list = drugTypeMapper.selectAll(drugType);
        return PageInfo.of(list);
    }

}
