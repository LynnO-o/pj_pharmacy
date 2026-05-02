<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入药店店主名称查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table tooltip-effect="dark myTooltip" stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="username" label="账号"></el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="password" label="密码"></el-table-column>
        <el-table-column prop="avatar" label="头像">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 40px; border-radius: 50%; display: block" v-if="scope.row.avatar"
                      :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column prop="sellDrug" label="销售药品">
          <template #default="scope">
            <el-tag style="margin-bottom: 5px" type="primary" v-for="item in JSON.parse(scope.row.sellDrug || '[]')">{{ item }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="地址" show-overflow-tooltip></el-table-column>
        <el-table-column prop="introduce" label="介绍" show-overflow-tooltip></el-table-column>
        <el-table-column prop="email" label="邮件"></el-table-column>
        <el-table-column prop="role" label="角色"></el-table-column>
        <el-table-column prop="practiceLicense" label="认证图片">
          <template v-slot="scope">
            <el-image style="width: 50px; height: 30px; display: block" v-if="scope.row.practiceLicense"
                      :src="scope.row.practiceLicense" :preview-src-list="[scope.row.practiceLicense]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="principalName" label="负责人名称"></el-table-column>
        <el-table-column prop="principalNo" label="负责人身份证号" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="认证状态">
          <template v-slot="scope">
            <el-tag type="warning" v-if="scope.row.status === '待审核'">待审核</el-tag>
            <el-tag type="success" v-if="scope.row.status === '审核通过'">审核通过</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '审核拒绝'">审核拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template v-slot="scope">
            <el-button v-if="scope.row.status === '待审核'" type="success" plain @click="audit(scope.row, '审核通过')">通过</el-button>
            <el-button v-if="scope.row.status === '待审核'" type="danger" plain @click="audit(scope.row, '审核拒绝')">拒绝</el-button>
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="药店店主信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="username" label="账号">
          <el-input v-model="data.form.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="名称">
          <el-input v-model="data.form.name" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item prop="avatar" label="头像">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleFileUpload"
              list-type="picture"
          >
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="phone" label="手机号">
          <el-input v-model="data.form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item prop="sellDrug" label="销售药品">
          <el-select multiple style="width: 100%" v-model="data.sellDrug">
            <el-option v-for="item in data.drugTypeList" :key="item.id" :value="item.name" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="address" label="地址">
          <el-input type="textarea" :rows="3" v-model="data.form.address" placeholder="请输入地址"></el-input>
        </el-form-item>
        <el-form-item prop="introduce" label="介绍">
          <el-input type="textarea" :rows="3" v-model="data.form.introduce" placeholder="请输入介绍"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="data.form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="role" label="角色">
          <el-input readonly v-model="data.form.role" placeholder="请输入角色"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";


const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  ids: [],
  sellDrug: [],
  drugTypeList: []
})

request.get('/drugType/selectAll').then(res => {
  data.drugTypeList = res.data
})

const load = () => {
  request.get('/drugShop/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
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

const handleAdd = () => {
  data.form = { role: 'DRUGSHOP' }
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.sellDrug = JSON.parse(data.form.sellDrug)
  data.formVisible = true
}

const add = () => {
  request.post('/drugShop/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const audit = (row, status) => {
  ElMessageBox.confirm('您确认' + status + '吗？', '审核确认', { type: 'warning' }).then(res => {
    let form = JSON.parse(JSON.stringify(row))
    form.status = status
    request.put('/drugShop/identify', form).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()

}

const update = () => {
  request.put('/drugShop/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  // 将json数组转换成 字符串 存储到数据库
  data.form.sellDrug = JSON.stringify(data.sellDrug)
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/drugShop/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}
const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete("/drugShop/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const reset = () => {
  data.name = null
  load()
}

load()
</script>

<style>
.myTooltip {
  max-width: 40% !important;
}
</style>