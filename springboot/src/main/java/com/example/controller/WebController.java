package com.example.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.service.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private DrugShopService drugShopService;
    @Resource
    private UserService userService;
    @Resource
    private DrugService drugService;
    @Resource
    private ForumService forumService;
    @Resource
    private DrugOrdersService drugOrdersService;
    @Resource
    private GoodsOrdersService goodsOrdersService;
    @Resource
    private DrugTypeService drugTypeService;

    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account loginAccount = null;
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            loginAccount = adminService.login(account);
        } else if (RoleEnum.DRUGSHOP.name().equals(account.getRole())) {
            loginAccount = drugShopService.login(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            loginAccount = userService.login(account);
        }
        return Result.success(loginAccount);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (RoleEnum.DRUGSHOP.name().equals(account.getRole())) {
            DrugShop drugShop = new DrugShop();
            BeanUtil.copyProperties(account, drugShop);
            drugShopService.add(drugShop);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            User user = new User();
            BeanUtil.copyProperties(account, user);
            userService.add(user);
        }
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if (RoleEnum.DRUGSHOP.name().equals(account.getRole())) {
            drugShopService.updatePassword(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

    @GetMapping("/count")
    public Result count() {
        List<DrugShop> drugShopList = drugShopService.selectAll(null).stream().filter(ps -> "审核通过".equals(ps.getStatus())).toList();
        List<Drug> drugList = drugService.selectAll(null).stream().filter(p -> "上架".equals(p.getStatus())).toList();
        List<Forum> forumList = forumService.selectAll(null);
        BigDecimal drugOrders = drugOrdersService.selectAll(null).stream().filter(o -> !"已取消".equals(o.getStatus()))
                .map(DrugOrders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        BigDecimal goodsOrders = goodsOrdersService.selectAll(null).stream().filter(o -> !"已取消".equals(o.getStatus()))
                .map(GoodsOrders::getTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        HashMap<String, Object> map = new HashMap<>();
        map.put("drugShop", drugShopList.size());
        map.put("drug", drugList.size());
        map.put("forum", forumList.size());
        map.put("drugOrders", drugOrders);
        map.put("goodsOrders", goodsOrders);
        return Result.success(map);
    }

    @GetMapping("/line")
    public Result line() {
        // 返回横轴的数据
        Date date = new Date();
        DateTime dateTime = DateUtil.offsetDay(date, -6);
        List<DateTime> dateTimeList = DateUtil.rangeToList(dateTime, date, DateField.DAY_OF_YEAR);
        List<String> dateStrList = dateTimeList.stream().map(DateUtil::formatDate).toList();
        List<DrugOrders> drugOrdersList = drugOrdersService.selectAll(null).stream().filter(o -> !"已取消".equals(o.getStatus())).toList();
        // 返回纵轴的数据
        List<BigDecimal> totalList = new ArrayList<>();
        for (String day : dateStrList) {
            BigDecimal dayMoney = drugOrdersList.stream().filter(orders -> orders.getTime().contains(day)).map(DrugOrders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            totalList.add(dayMoney);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("dateStrList", dateStrList);
        map.put("totalList", totalList);
        return Result.success(map);
    }

    @GetMapping("/pie")
    public Result pie() {
        List<DrugType> drugTypeList = drugTypeService.selectAll(null);
        List<Drug> drugList = drugService.selectAll(null);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        for (DrugType drugType : drugTypeList) {
            map = new HashMap<>();
            map.put("name", drugType.getName());
            map.put("value", drugList.stream().filter(p -> p.getTypeId().equals(drugType.getId())).count());
            list.add(map);
        }
        return Result.success(list);
    }

    @GetMapping("/bar")
    public Result bar() {
        List<DrugShop> drugShopList = drugShopService.selectAll(null).stream().filter(ps -> "审核通过".equals(ps.getStatus())).toList();
        List<String> drugShopNameList = drugShopList.stream().map(DrugShop::getName).toList();

        List<DrugOrders> drugOrdersList = drugOrdersService.selectAll(null).stream().filter(o -> !"已取消".equals(o.getStatus())).toList();
        List<BigDecimal> totalList = new ArrayList<>();
        for (DrugShop drugShop : drugShopList) {
            BigDecimal total = drugOrdersList.stream().filter(o -> o.getShopId().equals(drugShop.getId())).map(DrugOrders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            totalList.add(total);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("drugShopNameList", drugShopNameList);
        map.put("totalList", totalList);
        return Result.success(map);
    }
}