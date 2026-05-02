<template>
  <div style="width: 60%; margin: 10px auto">
    <div class="card" style="padding: 20px">
      <div style="margin-bottom: 20px; font-size: 24px; font-weight: bold">我收藏的药品</div>
      <div style="margin: 20px 0" v-if="data.total > 0">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in data.tableData" :key="item.id">
            <div @click="router.push('/front/drugDetail?id=' + item.fid)" style="padding: 0; margin-bottom: 20px;
                cursor: pointer; border-radius: 5px; overflow: hidden" >
              <img :src="item.drugImg" alt="" style="width: 100%; height: 220px; display: block">
              <div style="margin-top: 10px">
                <div style="font-size: 20px; font-weight: bold; margin-bottom: 10px" class="line1">{{ item.drugName }}</div>
                <div style="font-size: 20px; color: red">￥{{ item.drugPrice }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div style="padding: 20px; text-align: center; color: #666" v-else>暂无收藏...</div>

      <div style="margin-top: 20px" v-if="data.total">
        <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
      </div>
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
  pageSize: 10,
  total: 0,
})

const load = () => {
  request.get('/collect/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
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
</script>

<style scoped>

</style>