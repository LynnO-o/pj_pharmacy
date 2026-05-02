<template>
  <div class="main-content">
    <div>
      <el-carousel height="400px">
        <el-carousel-item v-for="item in data.slideshowList" :key="item">
          <img @click="goTo('/front/drugDetail?id=' + item.drugId)" :src="item.img" alt="" style="width: 100%; height: 400px; cursor: pointer">
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="margin: 30px 0; display: flex; grid-gap: 20px">
      <div style="flex: 1">
        <div style="display: flex; align-items: center; margin-bottom: 20px">
          <div style="flex: 1; font-size: 24px; font-weight: bold">推荐药品</div>
          <div style="cursor: pointer" @click="goTo('/front/drug')">查看所有药品 <el-icon style="top: 2px"><ArrowRight /></el-icon></div>
        </div>
        <div>
          <el-row :gutter="10">
            <el-col :span="12" v-for="item in data.recommendDrugList" :key="item.id">
              <div @click="goTo('/front/drugDetail?id=' + item.id)" class="card" style="padding: 0; margin-bottom: 10px; cursor: pointer">
                <img :src="item.img" alt="" style="width: 100%; height: 220px; display: block">
                <div style="padding: 10px">
                  <div style="font-size: 20px; font-weight: bold; margin-bottom: 10px;"  class="line1">{{ item.name }}</div>
                  <div style="font-size: 20px; color: red">￥{{ item.price }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div style="flex: 1">
        <div style="display: flex; align-items: center; margin-bottom: 20px">
          <div style="flex: 1; font-size: 24px; font-weight: bold">热门药店</div>
          <div style="cursor: pointer" @click="goTo('/front/drugShop')">查看所有药店 <el-icon style="top: 2px"><ArrowRight /></el-icon></div>
        </div>
        <div class="card" style="padding: 10px">
          <div @click="goTo('/front/drugShopDetail?id=' + item.id)" class="drugShop-item" v-for="item in data.drugShopList" :key="item.id" style="display: flex; align-items: center; grid-gap: 40px;
            margin-bottom: 10px; border-bottom: 1px solid #ddd; padding: 10px 0; cursor: pointer">
            <div style="width: 100px; text-align: center">
              <img :src="item.avatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
              <div style="font-size: 16px">{{ item.name }}</div>
            </div>
            <div>
              <div style="margin-bottom: 20px"><span style="color: #666">药店地址：</span>{{ item.address }}</div>
              <div style=" white-space: nowrap;   overflow: hidden; text-overflow: ellipsis;width: 300px;"><span style="color: #666" >售卖药品：</span>{{ JSON.parse(item.sellDrug || '[]').join('、') }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div style="margin: 20px 0">
      <div style="display: flex; align-items: center; margin-bottom: 20px">
        <div style="flex: 1; font-size: 24px; font-weight: bold">热销医用护理</div>
        <div style="cursor: pointer" @click="goTo('/front/goods')">查看所有护理用品 <el-icon style="top: 2px"><ArrowRight /></el-icon></div>
      </div>
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
  slideshowList: [],
  recommendDrugList:[],
  drugShopList:[],
  goodsList:[],
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

request.get('/slideshow/selectAll').then(res => {
  data.slideshowList = res.data
})

request.get('/drug/selectAll',{
  params:{
    recommend:'是',
    status:'上架'
  }
}).then(res => {
  data.recommendDrugList = res.data.splice(0,4)
})

request.get('/goods/selectAll',{
  params:{
    status:'上架',
    orderBy:'sale'
  }
}).then(res => {
  data.goodsList = res.data.splice(0,8)
  data.goodsList.forEach(item => item.num =1)
})

request.get('/drugShop/selectRank',{
  params:{
    status:'审核通过'
  }
}).then(res => {
  // const sortedList = res.data.sort((a, b) => {
  //   // 按id正序（数字排序），如果想按名称正序可替换为：a.name.localeCompare(b.name, 'zh-CN')
  //   return a.id - b.id;
  // });
  data.drugShopList = res.data.splice(0,6)
})

const goTo = (path) => {
  router.push(path)
}
</script>

<style>
.drugShop-item:last-child {
  border: none !important;
  padding-bottom: 0 !important;
}
</style>