package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.mapper.CollectMapper;
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
public class CollectService {

    @Resource
    private CollectMapper collectMapper;

    public void add(Collect collect) {
        Account currentUser = TokenUtils.getCurrentUser();
        Collect dbCollect = collectMapper.selectByFidAndUser(collect.getFid(), currentUser.getId());
        if (dbCollect != null) {
            collectMapper.deleteById(dbCollect.getId());
        } else {
            collect.setUserId(currentUser.getId());
            collectMapper.insert(collect);
        }
    }

    public void updateById(Collect collect) {
        collectMapper.updateById(collect);
    }

    public void deleteById(Integer id) {

        collectMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            collectMapper.deleteById(id);
        }
    }

    public Collect selectById(Integer id) {
        return collectMapper.selectById(id);
    }

    public List<Collect> selectAll(Collect collect) {
        return collectMapper.selectAll(collect);
    }

    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())){
            collect.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(collect);
        return PageInfo.of(list);
    }

}
