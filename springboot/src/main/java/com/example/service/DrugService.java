package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.entity.Drug;
import com.example.mapper.CollectMapper;
import com.example.mapper.DrugMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class DrugService {

    @Resource
    private DrugMapper drugMapper;
    @Resource
    CollectMapper collectMapper;

    public void add(Drug drug) {
        //drug.setTime(DateUtil.now());
        drugMapper.insert(drug);
    }

    public void updateById(Drug drug) {
        drugMapper.updateById(drug);
    }

    public void deleteById(Integer id) {

        drugMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            drugMapper.deleteById(id);
        }
    }

    public Drug selectById(Integer id) {
        Drug drug = drugMapper.selectById(id);
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null) {
            Collect collect = collectMapper.selectByFidAndUser(id, currentUser.getId());
            drug.setHasCollect(collect != null);
        }
        return drug;
    }

    public List<Drug> selectAll(Drug drug) {
        return drugMapper.selectAll(drug);
    }

    public PageInfo<Drug> selectPage(Drug drug, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && RoleEnum.DRUGSHOP.name().equals(currentUser.getRole())){
            drug.setShopId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Drug> list = drugMapper.selectAll(drug);
        return PageInfo.of(list);
    }

}
