<template>
  <div style="width: 60%; margin: 10px auto; min-height: 60vh">
    <div style="margin-bottom: 20px">
      <el-select v-model="data.typeId" placeholder="请选择药品分类" size="large" style="width: 200px; margin-right: 10px">
        <el-option v-for="item in data.typeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
      </el-select>
      <el-input v-model="data.name" size="large" style="width: 400px; margin-right: 10px" placeholder="请输入药品名称搜索"></el-input>
      <el-button size="large" type="primary" @click="load">搜索</el-button>
      <el-button size="large" @click="reset">重置</el-button>
    </div>

    <div style="margin: 20px 0" v-if="data.total > 0">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in data.tableData" :key="item.id">
          <div @click="router.push('/front/drugDetail?id=' + item.id)" class="card" style="padding: 0; margin-bottom: 20px;
                cursor: pointer; border-radius: 5px; overflow: hidden" >
            <img :src="item.img" alt="" style="width: 100%; height: 220px; display: block">
            <div style="padding: 10px">
              <div style="font-size: 20px; font-weight: bold; margin-bottom: 10px" class="line1">{{ item.name }}</div>
              <div style="font-size: 20px; color: red">￥{{ item.price }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
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
  pageSize: 8,
  total: 0,
  name: null,
  typeId: null,
  typeList: []
})

request.get('/drugType/selectAll').then(res =>{
  data.typeList = res.data
})

const load = () => {
  request.get('/drug/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      typeId: data.typeId
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
  data.typeId = null
  load()
}
</script>

<style scoped>

</style>