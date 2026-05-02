package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Drug;
import com.example.entity.DrugShop;
import com.example.exception.CustomException;
import com.example.mapper.DrugOrdersMapper;
import com.example.mapper.DrugShopMapper;
import com.example.mapper.GoodsOrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * 业务层方法
 */
@Service
public class DrugShopService {

    @Resource
    private DrugShopMapper drugShopMapper;
    @Resource
    private DrugService drugService;
    @Resource
    private DrugOrdersMapper drugOrdersMapper;
    @Resource
    private GoodsOrdersMapper goodsOrdersMapper;

    public void add(DrugShop drugShop) {
        DrugShop dbDrugShop = drugShopMapper.selectByUsername(drugShop.getUsername());
        if (ObjectUtil.isNotNull(dbDrugShop)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(drugShop.getPassword())) {
            drugShop.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(drugShop.getName())) {
            drugShop.setName(drugShop.getUsername());
        }
        drugShop.setRole(RoleEnum.DRUGSHOP.name());
        drugShop.setStatus("待提交");
        //drugShop.setTime(DateUtil.now());
        drugShopMapper.insert(drugShop);
    }

    public void updateById(DrugShop drugShop) {

        drugShopMapper.updateById(drugShop);
    }

    public void deleteById(Integer id) {

        drugShopMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            drugShopMapper.deleteById(id);
        }
    }

    public DrugShop selectById(Integer id) {
        return drugShopMapper.selectById(id);
    }

    public List<DrugShop> selectAll(DrugShop drugShop) {
        return drugShopMapper.selectAll(drugShop);
    }

    public List<DrugShop> selectRank() {
        DrugShop params = new DrugShop();
        params.setStatus("审核通过");
        List<DrugShop> drugShopList = drugShopMapper.selectAll(params);
        for (DrugShop drugShop : drugShopList) {
            Double drugSum = drugOrdersMapper.selectByShopId(drugShop.getId());
            Double goodsSum = goodsOrdersMapper.selectByShopId(drugShop.getId());
            Double sum = drugSum + goodsSum;
            drugShop.setSum(sum);
        }
        return drugShopList.stream().sorted(Comparator.comparing(DrugShop::getSum, Comparator.reverseOrder())).limit(10).toList();
    }

    public PageInfo<DrugShop> selectPage(DrugShop drugShop, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DrugShop> list = drugShopMapper.selectAll(drugShop);
        for (DrugShop shop : list) {
            Drug drug = new Drug();
            drug.setShopId(shop.getId());
            // 根据店铺id  查出这个店里面所有的宠物信息
            List<Drug> drugList = drugService.selectAll(drug);
            drugList = drugList.stream().limit(4).toList();
            shop.setDrugList(drugList);
        }
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public DrugShop login(Account account) {
        DrugShop dbDrugShop = drugShopMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbDrugShop)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!dbDrugShop.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String token = TokenUtils.createToken(dbDrugShop.getId() + "-" + dbDrugShop.getRole(), dbDrugShop.getPassword());
        dbDrugShop.setToken(token);
        return dbDrugShop;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        DrugShop dbDrugShop = drugShopMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbDrugShop)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbDrugShop.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbDrugShop.setPassword(account.getNewPassword());
        drugShopMapper.updateById(dbDrugShop);
    }

    public void identify(DrugShop drugShop) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.DRUGSHOP.name().equals(currentUser.getRole())){
            drugShop.setStatus("待审核");
        }
        drugShopMapper.updateById(drugShop);
    }
}
