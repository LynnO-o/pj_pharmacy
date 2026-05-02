package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Address;
import com.example.entity.Drug;
import com.example.entity.DrugOrders;
import com.example.exception.CustomException;
import com.example.mapper.DrugOrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 业务层方法
 */
@Service
public class DrugOrdersService {

    @Resource
    private DrugOrdersMapper drugOrdersMapper;
    @Resource
    private DrugService drugService;
    @Resource
    private AddressService addressService;
    @Resource
    private UserService userService;

    @Transactional
    public void add(DrugOrders drugOrders) {
        // 判断库存是否足够
        Integer drugId = drugOrders.getDrugId();
        Drug drug = drugService.selectById(drugId);
        if (drug.getStore() < 1) {  // 库存不足
            throw new CustomException("500", "药品剩余数量不足");
        }
        Account currentUser = TokenUtils.getCurrentUser();
        // 判断账户余额是否充足
        if (currentUser.getAccount() == null || currentUser.getAccount().compareTo(drug.getPrice()) < 0) {
            throw new CustomException("500", "账户余额不足");
        }
        // 扣减库存
        drug.setStore(drug.getStore() - 1);
        drugService.updateById(drug);
        String orderNo = DateUtil.format(new Date(), "yyyyMMdd") + String.valueOf(System.currentTimeMillis()).substring(8);
        drugOrders.setOrderNo(orderNo);

        drugOrders.setUserId(currentUser.getId());
        drugOrders.setDrugName(drug.getName());
        drugOrders.setDrugImg(drug.getImg());
        drugOrders.setPrice(drug.getPrice());
        if (drugOrders.getAddressId() != null) {
            Address address = addressService.selectById(drugOrders.getAddressId());
            drugOrders.setPickupUser(address.getName());
            drugOrders.setPickupAddress(address.getAddress());
            drugOrders.setPickupPhone(address.getPhone());
        }
//        else {
//            drugOrders.setPickupUser(drugOrders.getPickupUser());
//            drugOrders.setPickupAddress(drugOrders.getPickupAddress());
//            drugOrders.setPickupPhone(drugOrders.getPickupPhone());
//        }
        // 设置收货地址信息

        drugOrders.setStatus("待支付");
        drugOrders.setTime(DateUtil.now());
        drugOrders.setId(null);  //这么做是为了防止再次购买的时候出现了id，导致数据插入异常
        drugOrdersMapper.insert(drugOrders);
    }

    @Transactional
    public void updateById(DrugOrders drugOrders) {
        Account currentUser = TokenUtils.getCurrentUser();
        Integer drugId = drugOrders.getDrugId();
        Drug drug = drugService.selectById(drugId);
        // 用户取消订单
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            if ("已取消".equals(drugOrders.getStatus())) {
                drug.setStore(drug.getStore() + 1);
                // 退还库存
                drugService.updateById(drug);
            } else if ("待发货".equals(drugOrders.getStatus())) {
                userService.updateAccountById(-drugOrders.getPrice().doubleValue(), currentUser.getId());
            }
        }
        drugOrdersMapper.updateById(drugOrders);
    }

    public void deleteById(Integer id) {

        drugOrdersMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            drugOrdersMapper.deleteById(id);
        }
    }

    public DrugOrders selectById(Integer id) {
        return drugOrdersMapper.selectById(id);
    }

    public List<DrugOrders> selectAll(DrugOrders drugOrders) {
        return drugOrdersMapper.selectAll(drugOrders);
    }

    public PageInfo<DrugOrders> selectPage(DrugOrders drugOrders, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())){
            drugOrders.setUserId(currentUser.getId());
        }
        if (RoleEnum.DRUGSHOP.name().equals(currentUser.getRole())){
            drugOrders.setShopId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<DrugOrders> list = drugOrdersMapper.selectAll(drugOrders);
        return PageInfo.of(list);
    }

}
