<template>
  <div>
    <div class="front-notice"><el-icon><Bell /></el-icon>公告：{{ data.top }}</div>
    <div class="front-header">
      <div class="front-header-left" @click="router.push('/front/home')">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">优选在线药房系统设计与开发</div>
      </div>
      <div class="front-header-center">
        <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal">
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/drugshop">药店</el-menu-item>
          <el-menu-item index="/front/drug">药品信息</el-menu-item>
          <el-menu-item index="/front/goods">医用护理</el-menu-item>
          <el-menu-item index="/front/cart">购物车</el-menu-item>
          <el-menu-item index="/front/orders">我的订单</el-menu-item>
          <el-menu-item index="/front/forum">医药论坛</el-menu-item>

        </el-menu>
      </div>
      <div class="front-header-right">
        <div v-if="!data.user.id">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="cursor: pointer; height: 60px">
            <div style="display: flex; align-items: center">
              <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
              <span style="margin-left: 5px;">{{ data.user.name }}</span><el-icon><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/front/notice')">系统公告</el-dropdown-item>
                <el-dropdown-item @click="router.push('/front/myForum')">我的帖子</el-dropdown-item>
                <el-dropdown-item @click="router.push('/front/myComment')">我的评论</el-dropdown-item>
                <el-dropdown-item @click="router.push('/front/myCollect')">我的收藏</el-dropdown-item>
                <el-dropdown-item @click="router.push('/front/address')">我的地址</el-dropdown-item>
                <el-dropdown-item @click="router.push('/front/person')">个人信息</el-dropdown-item>
                <el-dropdown-item @click="router.push('/front/password')">修改密码</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div class="main-body">
      <RouterView @updateUser="updateUser" style="min-height: 50vh"/>

      <Footer />
    </div>
  </div>
</template>

<script setup>
  import router from "@/router/index.js";
  import { reactive } from "vue";
  import request from "@/utils/request.js";
  import Footer from "@/components/Footer.vue"

  const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    top: '',
    noticeData: []
  })

  const logout = () => {
    localStorage.removeItem('xm-user')
    router.push('/login')
  }

  const updateUser = () => {
    data.user =  JSON.parse(localStorage.getItem('xm-user') || '{}')
  }


</script>

<style scoped>
@import "../assets/css/front.css";
</style>