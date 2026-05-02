<template>
  <div class="main-content">
    <div style="padding: 20px; border: 1px solid #ddd; border-radius: 5px">
      <div style="display: flex; align-items: center; grid-gap: 20px">
        <div style="font-size: 18px">已选商品（{{ data.goodsNum }}）</div>
        <div style="flex: 1">
          <el-select style="width: 100%" v-model="data.addressId">
            <el-option v-for="item in data.addressList" :key="item.id" :value="item.id" :label="item.name + ' - ' + item.address + ' - ' + item.phone"></el-option>
          </el-select>
        </div>
        <div style="width: 260px; display: flex; align-items: center; justify-content: flex-end">
          商品总价格
          <span style="font-size: 20px; font-weight: bold; color: red; margin-left: 5px; display: inline-block; width: 100px">￥{{ data.money }}</span>
          <el-button @click="addOrder" type="danger" style="margin-left: 10px; border-radius: 20px">下单</el-button>
        </div>
      </div>

      <div style="margin-top: 30px">
        <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="goodsImg" label="商品图片">
            <template #default="scope">
              <el-image style="width: 80px; height: 80px; border-radius: 5px" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]" preview-teleported></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="goodsName" label="商品名称"></el-table-column>
          <el-table-column prop="goodsPrice" label="商品单价">
            <template #default="scope">
              <span style="color: red; font-size: 16px; font-weight: bold">￥{{ scope.row.goodsPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="num" label="商品数量">
            <template #default="scope">
              <el-input-number @change="calc" style="width: 150px" :min="1" v-model="scope.row.num"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="130" fixed="right">
            <template v-slot="scope">
              <el-button type="danger" plain @click="del(scope.row.id)">移除购物车</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  addressId: null,
  tableData: [],
  addressList: [],
  money: 0,
  rows: [],
  goodsNum: 0
})

const addOrder = () => {
  if (data.rows?.length === 0) {
    ElMessage.warning('请选择商品')
    return
  }
  if (!data.addressId) {
    ElMessage.warning('请选择收货地址')
    return
  }
  let arr = []
  data.rows.forEach(item => {
    //  item 表示购物车的某一行
    let form = { addressId: data.addressId, price: item.goodsPrice, goodsId: item.goodsId, num: item.num }
    arr.push(form)
  })
  request.post('/goodsOrders/add', arr).then(res => {
    if (res.code === '200') {
      ElMessage.success("下单成功")
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const calc = () => {
  // 计算商品的总数量
  data.goodsNum = 0
  data.rows.forEach(item => data.goodsNum += item.num)
  // 计算商品的总价格
  data.money = 0
  data.rows.forEach(item => {
    data.money += item.goodsPrice * item.num
  })
  data.money = data.money.toFixed(2)
}

request.get('/address/selectAll', {
  params: {
    userId: data.user.id
  }
}).then(res => {
  data.addressList = res.data
  if (data.addressList.length > 0) {
    data.addressId = data.addressList[0].id
  }
})

const handleSelectionChange = (rows) => {
  data.rows = rows
  calc()
}

const load = () => {
  request.get('/cart/selectAll', {
    params: {
      userId: data.user.id
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data || []
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const add = () => {
  request.post('/cart/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const del = (id) => {
  request.delete('/cart/delete/' + id).then(res => {
    if (res.code === '200') {
      ElMessage.success("移除成功")
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}


</script>