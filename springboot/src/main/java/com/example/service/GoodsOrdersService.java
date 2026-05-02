package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.CartMapper;
import com.example.mapper.GoodsOrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 业务层方法
 */
@Service
public class GoodsOrdersService {

    @Resource
    private GoodsOrdersMapper goodsOrdersMapper;
    @Resource
    private GoodsService goodsService;
    @Resource
    private AddressService addressService;
    @Resource
    private UserService userService;
    @Autowired
    private CartMapper cartMapper;

    @Transactional
    public void add(List<GoodsOrders> goodsOrdersList) {
        Account currentUser = TokenUtils.getCurrentUser();
        BigDecimal allTotal = goodsOrdersList.stream().map(goodsOrders -> goodsOrders.getPrice().multiply(BigDecimal.valueOf(goodsOrders.getNum())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        // 判断账户余额是否充足
        if (currentUser.getAccount() == null || currentUser.getAccount().compareTo(allTotal) < 0) {
            throw new CustomException("500", "账户余额不足");
        }
        for (GoodsOrders goodsOrders : goodsOrdersList) {
            // 判断库存是否足够
            Integer goodsId = goodsOrders.getGoodsId();
            Goods goods = goodsService.selectById(goodsId);
            if (goods.getStore() < goodsOrders.getNum()) {  // 库存不足
                throw new CustomException("500", "医护用品剩余数量不足");
            }

            // 扣减库存
            goods.setStore(goods.getStore() - goodsOrders.getNum());
            // 增加销量
            goods.setSale(goods.getSale() + goodsOrders.getNum());
            goodsService.updateById(goods);
            String orderNo = DateUtil.format(new Date(), "yyyyMMdd") + String.valueOf(System.currentTimeMillis()).substring(8);
            goodsOrders.setOrderNo(orderNo);
            goodsOrders.setShopId(goods.getShopId());
            goodsOrders.setUserId(currentUser.getId());
            goodsOrders.setGoodsName(goods.getName());
            goodsOrders.setGoodsImg(goods.getImg());
            goodsOrders.setPrice(goods.getPrice());
            BigDecimal total = goods.getPrice().multiply(BigDecimal.valueOf(goodsOrders.getNum()));
            goodsOrders.setTotal(total);
            // 设置收货地址信息
            if (goodsOrders.getAddressId() != null) {
                Address address = addressService.selectById(goodsOrders.getAddressId());
                goodsOrders.setPickupUser(address.getName());
                goodsOrders.setPickupAddress(address.getAddress());
                goodsOrders.setPickupPhone(address.getPhone());
            }
            goodsOrders.setStatus("待支付");
            goodsOrders.setTime(DateUtil.now());
            goodsOrders.setId(null);  // 这么做是为了防止再次购买的时候出现了id，导出数据库插入异常
            goodsOrdersMapper.insert(goodsOrders);
            // 清除购物车里面对应的商品
            cartMapper.deleteByUserIdAndGoodsId(currentUser.getId(), goodsId);
        }
    }

    @Transactional
    public void updateById(GoodsOrders goodsOrders) {
        Account currentUser = TokenUtils.getCurrentUser();
        Integer goodsId = goodsOrders.getGoodsId();
        Goods goods = goodsService.selectById(goodsId);
        // 用户取消订单
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            if ("已取消".equals(goodsOrders.getStatus())) {
                goods.setStore(goods.getStore() + goodsOrders.getNum());
                goods.setSale(goods.getSale() - goodsOrders.getNum());
                // 退还库存
                goodsService.updateById(goods);
            } else if ("待发货".equals(goodsOrders.getStatus())) {
                userService.updateAccountById(-goodsOrders.getTotal().doubleValue(), currentUser.getId());
            }
        }
        goodsOrdersMapper.updateById(goodsOrders);
    }

    public void deleteById(Integer id) {
        goodsOrdersMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            goodsOrdersMapper.deleteById(id);
        }
    }

    public GoodsOrders selectById(Integer id) {
        return goodsOrdersMapper.selectById(id);
    }

    public List<GoodsOrders> selectAll(GoodsOrders goodsOrders) {
        return goodsOrdersMapper.selectAll(goodsOrders);
    }

    public PageInfo<GoodsOrders> selectPage(GoodsOrders goodsOrders, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            goodsOrders.setUserId(currentUser.getId());
        }
        if (RoleEnum.DRUGSHOP.name().equals(currentUser.getRole())){
            goodsOrders.setShopId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsOrders> list = goodsOrdersMapper.selectAll(goodsOrders);
        return PageInfo.of(list);
    }

}