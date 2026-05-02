<template>
  <div class="main-content">
    <div style="margin-bottom: 20px">
      <el-input v-model="data.title" size="large" style="width: 400px; margin-right: 10px" placeholder="请输入标题搜索"></el-input>
      <el-button size="large" type="primary" @click="load">搜索</el-button>
      <el-button size="large" @click="reset">重置</el-button>
    </div>
    <div style="margin: 20px 0">
      <div @click="router.push('/front/forumDetail?id=' + item.id)" class="card" v-for="(item, index) in data.tableData" :key="item.id"
           style="padding: 20px; margin-bottom: 10px; cursor: pointer">
        <div style="display: flex; grid-gap: 20px" v-if="index % 2 === 0">
          <img :src="item.img" alt="" style="width: 150px; height: 150px">
          <div style="flex: 1">
            <div style="margin-bottom: 10px; font-size: 20px">{{ item.title }}</div>
            <div style="margin-bottom: 20px; color: #666; line-height: 20px; height: 60px" class="line3">{{ item.descr }}</div>
            <div style="display: flex; align-items: center; color: #666">
              <img :src="item.userAvatar" alt="" style="width: 25px; height: 25px; border-radius: 50%">
              <span>{{ item.userName }}</span>
              <el-icon color="#666" style="margin-left: 30px; margin-right: 5px"><View /></el-icon> <span>{{ item.viewCount }}</span>
              <el-icon color="#666" style="margin-left: 30px; margin-right: 5px"><Clock /></el-icon> <span>{{ item.time }}</span>
            </div>
          </div>
        </div>
        <div style="display: flex; grid-gap: 20px" v-else>
          <div style="flex: 1">
            <div style="margin-bottom: 10px; font-size: 20px">{{ item.title }}</div>
            <div style="margin-bottom: 20px; color: #666; line-height: 20px; height: 60px" class="line3">{{ item.descr }}</div>
            <div style="display: flex; align-items: center; color: #666">
              <img :src="item.userAvatar" alt="" style="width: 25px; height: 25px; border-radius: 50%">
              <span>{{ item.userName }}</span>
              <el-icon color="#666" style="margin-left: 30px; margin-right: 5px"><View /></el-icon> <span>{{ item.viewCount }}</span>
              <el-icon color="#666" style="margin-left: 30px; margin-right: 5px"><Clock /></el-icon> <span>{{ item.time }}</span>
            </div>
          </div>
          <img :src="item.img" alt="" style="width: 150px; height: 150px">
        </div>
      </div>
    </div>

    <div style="margin: 20px 0" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  title: null,
})

const load = () => {
  request.get('/forum/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
      status: '通过',
      flag: 'front'
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const reset = () => {
  data.title = null
  load()
}

</script>

<style scoped>

</style>