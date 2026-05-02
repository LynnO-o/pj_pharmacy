<template>
  <div style="width: 60%; margin: 10px auto">
    <div class="card" style="padding: 20px; display: flex; grid-gap: 20px; margin-bottom: 20px">
      <img :src="data.shop.avatar" alt="" style="width: 200px; height: 200px">
      <div style="flex: 1; display: flex; flex-direction: column; justify-content: space-around">
        <div style="font-size: 20px; font-weight: bold; margin-bottom: 10px">{{ data.shop.name }}</div>
        <div style="margin-bottom: 10px">
          <span style="color: #666">药品类型：</span>
          <span>{{ JSON.parse(data.shop.sellDrug || '[]').join('、') }}</span>
        </div>
        <div style="margin-bottom: 10px">
          <span style="color: #666">联系方式：</span>
          <span>{{ data.shop.phone }}</span>
        </div>
        <div style="margin-bottom: 10px">
          <span style="color: #666">店铺地址：</span>
          <span>{{ data.shop.address }}</span>
        </div>
        <div style="text-align: justify; line-height: 20px">
          <span style="color: #666">店铺介绍：</span>
          <span>{{ data.shop.introduce }}</span>
        </div>
      </div>
    </div>

    <div class="card" style="padding: 20px; margin-bottom: 20px">
      <div style="font-size: 24px; font-weight: bold; padding-bottom: 10px; border-bottom: 1px solid #ccc">医用护理信息</div>
      <div style="padding-top: 20px">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in data.drugList">
            <div style="cursor: pointer" @click="router.push('/front/drugDetail?id=' + item.id)">
              <img :src="item.img" alt="" style="width: 100%; height: 200px; display: block; border-radius: 5px">
              <div style="margin: 10px 0; display: flex; align-items: center">
                <span style="font-size: 20px; font-weight: bold; margin-left: 5px">{{ item.name }}</span>
              </div>
              <div style="font-size: 20px; color: red">￥{{ item.price }}</div>
            </div>
          </el-col>
        </el-row>

        <div style="margin-top: 20px" v-if="data.total">
          <el-pagination @current-change="loadDrugList" layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
        </div>
      </div>
    </div>

    <div class="card" style="padding: 20px; margin-bottom: 20px">
      <div style="font-size: 24px; font-weight: bold; padding-bottom: 10px; border-bottom: 1px solid #ccc">医用用品信息</div>
      <div style="padding-top: 20px" v-if="data.goodsTotal > 0">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in data.goodsList" :key="item.id">
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

        <div style="margin-top: 20px" v-if="data.goodsTotal">
          <el-pagination @current-change="loadGoodsList" layout="total, prev, pager, next" :page-size="data.goodsPageSize" v-model:current-page="data.goodsPageNum" :total="data.goodsTotal" />
        </div>
      </div>
      <div style="padding-top: 40px; text-align: center; color: #666" v-else>
        暂无医用用品...
      </div>
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

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  id: router.currentRoute.value.query.id,
  shop: {},
  drugList: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  goodsList: [],
  goodsPageNum: 1,
  goodsPageSize: 10,
  goodsTotal: 0,
  goodsVisible: false,
  goods: {}
})

window.scrollTo({top:0})

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

request.get('/drugShop/selectById/' + data.id).then(res => {
  data.shop = res.data
})

const loadDrugList = () => {
  // 查询药品列表
  request.get('/drug/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      shopId: data.id,
      status: '上架'
    }
  }).then(res => {
    data.drugList = res.data?.list
    data.total = res.data?.total
  })
}
loadDrugList()

const loadGoodsList = () => {
  // 查询医用护理列表
  request.get('/goods/selectPage', {
    params: {
      pageNum: data.goodsPageNum,
      pageSize: data.goodsPageSize,
      shopId: data.id,
      status: '上架'
    }
  }).then(res => {

    data.goodsList = res.data?.list
    data.goodsTotal = res.data?.total
    data.goodsList.forEach(item => { item.num = 1 })
  })
}
loadGoodsList()
</script>

<style>

</style>