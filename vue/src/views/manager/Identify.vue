<template>
  <div style="width: 50%" class="card">
    <div style="margin-bottom: 10px; color: red">一旦认证信息修改，药店的认证状态就会变为待审批状态，将无法使用药店功能</div>
    <el-form ref="formRef" :model="data.from" label-width="70px" style="padding: 20px">
      <el-form-item prop="practiceLicense" label="认证证件">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :headers="{ 'token': data.user.token }"
            :on-success="handleFileUpload"
            :show-file-list="false"
            class="avatar-uploader"
        >
          <img v-if="data.from.practiceLicense" :src="data.from.practiceLicense" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item prop="principalName" label="真实姓名">
        <el-input v-model="data.from.principalName" placeholder="请输入真实姓名"></el-input>
      </el-form-item>
      <el-form-item prop="principalNo" label="身份证号">
        <el-input v-model="data.from.principalNo" placeholder="请输入真实身份证号码"></el-input>
      </el-form-item>
      <el-form-item prop="principalNo" label="审核状态">
        <el-tag type="info" v-if="data.from.status === '待提交'">待提交</el-tag>
        <el-tag type="warning" v-if="data.from.status === '待审核'">待审核</el-tag>
        <el-tag type="danger" v-if="data.from.status === '审核拒绝'">审核拒绝</el-tag>
        <el-tag type="success" v-if="data.from.status === '审核通过'">审核通过</el-tag>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="update">提 交</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  from: {},
})

// 拷贝 药店主用户的 信息
// data.from = JSON.parse(JSON.stringify(data.user))
const load = () => {
  request.get('/drugShop/selectById/' + data.user.id).then(res => {
    data.from = res.data
  })
}
load()

const handleFileUpload = (res) => {
  data.from.practiceLicense = res.data
}

const update = () => {
  ElMessageBox.confirm('您确认提交审核吗？', '提交确认', { type: 'warning' }).then(res => {
    request.put('/drugShop/identify', data.from).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('提交成功，等待管理员审核')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}
</script>

<style>
.avatar-uploader {
  height: 120px;
}
.avatar-uploader .avatar {
  width: 180px;
  height: 120px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 120px;
  text-align: center;
}
</style>