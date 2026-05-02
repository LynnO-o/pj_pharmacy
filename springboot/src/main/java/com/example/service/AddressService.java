package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Address;
import com.example.mapper.AddressMapper;
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
public class AddressService {

    @Resource
    private AddressMapper addressMapper;

    public void add(Address address) {
        Account currentUser = TokenUtils.getCurrentUser();
        address.setUserId(currentUser.getId());
        //address.setTime(DateUtil.now());
        addressMapper.insert(address);
    }

    public void updateById(Address address) {
        addressMapper.updateById(address);
    }

    public void deleteById(Integer id) {

        addressMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            addressMapper.deleteById(id);
        }
    }

    public Address selectById(Integer id) {
        return addressMapper.selectById(id);
    }

    public List<Address> selectAll(Address address) {
        return addressMapper.selectAll(address);
    }

    public PageInfo<Address> selectPage(Address address, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            address.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Address> list = addressMapper.selectAll(address);
        return PageInfo.of(list);
    }

}
