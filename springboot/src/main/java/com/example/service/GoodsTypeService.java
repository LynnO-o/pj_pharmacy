package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.GoodsType;
import com.example.mapper.GoodsTypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class GoodsTypeService {

    @Resource
    private GoodsTypeMapper goodsTypeMapper;

    public void add(GoodsType goodsType) {
        //goodsType.setTime(DateUtil.now());
        goodsTypeMapper.insert(goodsType);
    }

    public void updateById(GoodsType goodsType) {
        goodsTypeMapper.updateById(goodsType);
    }

    public void deleteById(Integer id) {

        goodsTypeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            goodsTypeMapper.deleteById(id);
        }
    }

    public GoodsType selectById(Integer id) {
        return goodsTypeMapper.selectById(id);
    }

    public List<GoodsType> selectAll(GoodsType goodsType) {
        return goodsTypeMapper.selectAll(goodsType);
    }

    public PageInfo<GoodsType> selectPage(GoodsType goodsType, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsType> list = goodsTypeMapper.selectAll(goodsType);
        return PageInfo.of(list);
    }

}
