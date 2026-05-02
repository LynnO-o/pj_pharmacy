<template>
  <div style="width: 60%; margin: 10px auto; min-height: 80vh">
    <div style="margin-bottom: 20px">
      <el-input v-model="data.name" size="large" style="width: 400px; margin-right: 10px" placeholder="请输入药店名称搜索"></el-input>
      <el-button size="large" type="primary" @click="load">搜索</el-button>
      <el-button size="large"  @click="reset">重置</el-button>
    </div>
    <div style="margin: 20px 0" v-if="data.total > 0">
      <div class="card" style="padding: 20px; margin-bottom: 10px; display: flex; align-items: center" v-for="item in data.tableData" :key="item.id">
        <div @click="router.push('/front/drugShopDetail?id=' + item.id)" style="flex: 1; display: flex; grid-gap: 20px; cursor: pointer;">
          <img :src="item.avatar" alt="" style="width: 100px; height: 100px">
          <div style="flex: 1; display: flex; flex-direction: column; justify-content: space-around">
            <div style="font-size: 20px; font-weight: bold">{{ item.name }}</div>
            <div style="margin: 10px 0"><span style="color: #666">售卖药品：</span>{{ JSON.parse(item.sellDrug || '[]').join('、') }}</div>
            <div><span style="color: #666">店铺地址：</span>{{ item.address }}</div>
          </div>
        </div>
        <div style="flex: 1">
          <el-row :gutter="20">
            <el-col :span="6" v-for="drug in item.drugList" :key="drug.id">
              <img @click="router.push('/front/drugDetail?id=' + drug.id)" :src="drug.img" alt="" style="width: 100px; height: 100px; border-radius: 50%; cursor: pointer">
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <div style="padding: 20px; text-align: center; color: #666" v-else>暂无搜索结果...</div>

    <div style="margin-top: 20px" v-if="data.total">
      <el-pagination @current-change="load" layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
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
  pageSize: 4,
  total: 0,
  name: null,
})

window.scrollTo({top:0})

const load = () => {
  request.get('/drugShop/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      status: '审核通过',
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
  data.name = null
  load()
}
</script>

<style scoped>

</style>