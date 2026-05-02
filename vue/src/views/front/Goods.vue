<template>
  <div style="width: 60%; margin: 10px auto;">
    <div style="margin-bottom: 20px">
      <el-select v-model="data.typeId" placeholder="请选择医用护理分类" size="large" style="width: 200px; margin-right: 10px">
        <el-option v-for="item in data.typeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
      </el-select>
      <el-input v-model="data.name" size="large" style="width: 400px; margin-right: 10px" placeholder="请输入医用护理名称搜索"></el-input>
      <el-button size="large" type="primary" @click="load">搜索</el-button>
      <el-button size="large" @click="reset">重置</el-button>
    </div>

    <div style="margin: 20px 0" v-if="data.total > 0">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in data.tableData" :key="item.id">
          <div class="card" style="padding: 0; border-radius: 5px; overflow: hidden; margin-bottom: 20px">
            <img :src="item.img" alt="" style="width: 100%; height: 200px">
            <div style="padding: 10px">
              <div style="font-size: 20px; margin-bottom: 10px; display: flex; align-items: center">
                <el-tag type="success">售卖中</el-tag>
                <span style="font-weight: bold; margin-left: 5px; flex: 1; width: 0" class="line1">{{ item.name }}</span>
              </div>
              <div style="display: flex; align-items: center; margin: 15px 0">
                <div style="font-size: 18px; color: red; flex: 1">￥{{ item.price }}</div>
                <div><el-button @click="view(item)" type="primary" plain size="small">查看详情</el-button></div>
              </div>
              <div style="display: flex; align-items: center">
                <div style="flex: 1">
                  <el-input-number v-model="item.num" :min="1" style="width: 100px" size="small"></el-input-number>
                </div>
                <div><el-button @click="addCart(item.id,item.num)" type="danger" size="small">加入购物车</el-button></div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div style="padding: 20px; text-align: center; color: #666" v-else>暂无搜索结果...</div>

    <div style="margin-top: 20px" v-if="data.total">
      <el-pagination @current-change="load" layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="医用护理详情" v-model="data.goodsVisible" width="40%" destroy-on-close>
      <div style="padding: 20px; color: #333">
        <div style="text-align: center; margin-bottom: 10px"><img style="width: 50%" :src="data.goods.img" alt=""></div>
        <div style="display: flex; align-items: center; margin-bottom: 10px">
          <el-tag type="success">售卖中</el-tag>
          <span style="font-size: 20px; font-weight: bold; margin-left: 5px">{{ data.goods.name }}</span>
        </div>
        <div style="margin: 10px 0"><span style="color: #666">商品销量：</span>{{ data.goods.sale }}</div>
        <div style="margin: 10px 0"><span style="color: #666">剩余数量：</span>{{ data.goods.store }}</div>
        <div style="margin: 10px 0"><span style="color: #666">商品类型：</span>{{ data.goods.typeName }}</div>
        <div style="margin: 10px 0; text-align: justify; line-height: 20px"><span style="color: #666">商品简介：</span>{{ data.goods.descr }}</div>
        <div style="margin: 10px 0;"><span style="color: #666">商品价格：</span><span style="font-size: 18px; color: red">￥{{ data.goods.price }}</span></div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.goodsVisible = false">关 闭</el-button>
        </span>
      </template>
    </el-dialog>
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
  typeList: [],
  goodsVisible: false,
  goods: {}
})

window.scrollTo({ top: 0 })

const addCart = (goodsId, num) => {
  request.post('/cart/add', { goodsId: goodsId, num: num}).then(res => {
    if (res.code === '200') {
      ElMessage.success('加入成功')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const view = (goods) => {
  data.goods = goods
  data.goodsVisible = true
}

request.get('/goodsType/selectAll').then(res => data.typeList = res.data)

const load = () => {
  request.get('/goods/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      typeId: data.typeId,
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
      data.tableData.forEach(item => item.num = 1)
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