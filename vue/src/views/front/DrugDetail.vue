<template>
  <div style="width: 60%; margin: 10px auto">
    <div class="card" style="padding: 20px; display: flex; grid-gap: 20px; margin-bottom: 20px">
      <img :src="data.drug.img" alt="" style="width: 400px; height: 350px">
      <div style="flex: 1; display: flex; flex-direction: column; justify-content: space-around">
        <div style="font-size: 20px; font-weight: bold; margin-bottom: 10px">{{ data.drug.name }}</div>
        <div style="margin-bottom: 10px; display: flex; align-items: center;">
          <span style="color: #666">药店：</span>
          <div style="display: flex; align-items: center; cursor: pointer;" @click="router.push('/front/drugShopDetail?id=' + data.drug.shopId)">
            <img style="width: 20px; height: 20px; border-radius: 50%" :src="data.drug.shopAvatar" alt="">
            <span>{{ data.drug.shopName }}</span>
          </div>
        </div>
        <div style="margin-bottom: 10px">
          <span style="color: #666">药品类型：</span>
          <span>{{ data.drug.typeName }}</span>
        </div>
        <div style="margin-bottom: 10px">
          <span style="color: #666">药品状态：</span>
          <el-tag type="success">售卖中</el-tag>
        </div>
        <div style="margin-bottom: 10px">
          <span style="color: #666">药店价格：</span>
          <span style="color: #1d4ad7; font-size: 16px">￥{{ data.drug.price }}</span>
        </div>
        <div style="margin-bottom: 10px">
          <span style="color: #666">剩余数量：</span>
          <span >{{ data.drug.store }}</span>
        </div>
        <div class="line3" style="text-align: justify; line-height: 20px; margin-bottom: 10px">
          <span style="color: #666">功能主治：</span>
          <el-tooltip effect="dark myTooltip" :content="data.drug.drugfunction" placement="top" v-if="data.drug.drugfunction?.length >= 114">
            <span>{{ data.drug.drugfunction }}</span>
          </el-tooltip>
          <span>{{ data.drug.drugfunction }}</span>
        </div>
        <div class="line3" style="text-align: justify; line-height: 20px; height: 60px; margin-bottom: 10px">
          <span style="color: #ff0000">药品禁忌：</span>
          <el-tooltip effect="dark myTooltip" :content="data.drug.taboo" placement="top" v-if="data.drug.taboo?.length >= 114">
            <span>{{ data.drug.taboo }}</span>
          </el-tooltip>
          <span>{{ data.drug.taboo }}</span>
        </div>

        <div style="display: flex; grid-gap: 20px">
          <div>
            <el-button type="warning" v-if="data.drug.hasCollect" @click="addCollect(data.drug.id)">取消收藏</el-button>
            <el-button type="warning" v-else @click="addCollect(data.drug.id)">收藏药品</el-button>
          </div>
          <el-button type="danger" @click="handleAdd" :disabled="data.drug.store < 1">立即购买</el-button>
        </div>
      </div>
    </div>

    <div class="card" style="padding: 20px">
      <div style="font-size: 24px; font-weight: bold; padding-bottom: 10px; border-bottom: 1px solid #ddd">药品详情</div>
      <div style="padding: 20px 0" v-html="data.drug.content"></div>
    </div>

    <el-dialog title="收货地址" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" label-width="90px" style="padding: 20px">
        <el-form-item label="收货地址">
          <el-select  style="width: 100%" v-model="data.form.addressId">
            <el-option v-for="item in data.addressList" :key="item.id" :value="item.id" :label="item.name + ' - ' + item.address + ' - ' + item.phone"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="addOrder">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  id: router.currentRoute.value.query.id,
  drug: {},
  addresslist:[],
  form:{},
  formVisible:false
})

const addCollect = (drugId) => {
  request.post('/collect/add', { fid: drugId }).then(res => {
    if (res.code === '200') {
      ElMessage.success("操作成功")
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

window.scrollTo({top:0})

const handleAdd = () => {
  data.form = { drugId:data.id ,shopId:data.drug.shopId }
  data.formVisible = true
}

const addOrder = () => {
  if (!data.form.addressId) {
    ElMessage.warning('请选择收货地址')
    return
  }
  request.post('/drugOrders/add',data.form).then(res =>{
    if (res.code === '200') {
      ElMessage.success('下单成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

if (data.user.id) {
  request.get('/address/selectAll', {
    params: {
      userId: data.user.id
    }
  }).then(res => {
    data.addressList = res.data
  })
}
request.get('/address/selectAll',{
  params:{
    userId:data.user.id
  }
}).then(res => {
  data.addressList = res.data
})

const load = () => {
  request.get('/drug/selectById/' + data.id).then(res => {
    data.drug = res.data
  })
}
load()

</script>

<style>
.myTooltip{
  width: 30%;
}
</style>