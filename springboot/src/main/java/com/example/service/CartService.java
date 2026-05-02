package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Cart;
import com.example.mapper.CartMapper;
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
public class CartService {

    @Resource
    private CartMapper cartMapper;

    public void add(Cart cart) {
        Account currentUser = TokenUtils.getCurrentUser();
        cart.setUserId(currentUser.getId());

        // 查询当前的商品 用户有没有加入过购物车
        Cart dbcart = cartMapper.selectByUserIdAndGoodsId(currentUser.getId(), cart.getGoodsId());
        if (dbcart != null) {
            dbcart.setNum(cart.getNum() + dbcart.getNum());
            cartMapper.updateById(dbcart);
        } else {
            cartMapper.insert(cart);
        }
    }

    public void updateById(Cart cart) {
        cartMapper.updateById(cart);
    }

    public void deleteById(Integer id) {

        cartMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            cartMapper.deleteById(id);
        }
    }

    public Cart selectById(Integer id) {
        return cartMapper.selectById(id);
    }

    public List<Cart> selectAll(Cart cart) {
        return cartMapper.selectAll(cart);
    }

    public PageInfo<Cart> selectPage(Cart cart, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())){
            cart.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Cart> list = cartMapper.selectAll(cart);
        return PageInfo.of(list);
    }

}
