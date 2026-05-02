<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入药品名称查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd" v-if="data.user.role === 'DRUGSHOP'">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px ;overflow: auto">
      <el-table tooltip-effect="dark myTooltip" stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="shopName" label="药品店"></el-table-column>
        <el-table-column prop="typeName" label="药品类型"></el-table-column>
        <el-table-column prop="name" label="药品名称"></el-table-column>
        <el-table-column prop="img" label="药品图片">
          <template #default="scope">
            <el-image style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="药品价格"></el-table-column>
        <el-table-column prop="store" label="剩余数量"></el-table-column>
        <el-table-column prop="spec" label="药品规格" show-overflow-tooltip></el-table-column>
<!--        <el-table-column prop="expiredate" label="有效日期"></el-table-column>-->
        <el-table-column prop="drugfunction" label="功能主治" show-overflow-tooltip></el-table-column>
        <el-table-column prop="drugusage" label="药品用法" show-overflow-tooltip></el-table-column>
        <el-table-column prop="taboo" label="药品禁忌" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="药品状态">
          <template v-slot="scope">
            <el-tag type="success" v-if="scope.row.status === '上架'">上架</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '下架'">下架</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recommend" label="是否推荐">
          <template v-slot="scope">
            <el-tag type="success" v-if="scope.row.recommend === '是'">是</el-tag>
            <el-tag type="danger" v-if="scope.row.recommend === '否'">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column  label="药品详情">
          <template #default="scope">
            <el-button type="info" @click="view(scope.row.content)" style="width: 75px;">查看内容</el-button>
          </template>
        </el-table-column>
        <el-table-column label="推荐" width="150" fixed="right" v-if="data.user.role === 'ADMIN'">
          <template v-slot="scope">
            <el-button type="success" @click="recommend(scope.row, '是')" :disabled="scope.row.recommend === '是'">是</el-button>
            <el-button type="danger" @click="recommend(scope.row, '否')" :disabled="scope.row.recommend === '否'">否</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)" v-if="data.user.role === 'DRUGSHOP'"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total ,prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="药品信息" v-model="data.formVisible" width="50%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="90px" style="padding: 20px">
        <el-form-item prop="shopId" label="药店">
          <el-select  disabled style="width: 100%" v-model="data.form.shopId">
            <el-option v-for="item in data.shopList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="typeId" label="药品类型">
          <el-select  style="width: 100%" v-model="data.form.typeId">
            <el-option v-for="item in data.typeList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="name" label="药品名称">
          <el-input v-model="data.form.name" placeholder="请输入药品名称"></el-input>
        </el-form-item>
        <el-form-item prop="img" label="药品图片">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              :on-success="handleFileUpload"
              list-type="picture"
          >
            <el-button type="primary">上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="price" label="药品价格">
          <el-input-number :min="1" style="width:200px" v-model="data.form.price" placeholder="请输入药品价格"></el-input-number>
        </el-form-item>
        <el-form-item prop="store" label="剩余数量">
          <el-input-number style="width:200px" v-model="data.form.store" placeholder="请输入药品数量"></el-input-number>
        </el-form-item>
        <el-form-item prop="spec" label="药品规格">
          <el-input type="textarea" :rows="1" v-model="data.form.spec" placeholder="请输入药品规格"></el-input>
        </el-form-item>
<!--        <el-form-item prop="expiredate" label="有效日期">-->
<!--          <el-input type="textarea" :rows="1" v-model="data.form.expiredate" placeholder="请输入药品有效日期"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item prop="drugfunction" label="功能主治">
          <el-input type="textarea" :rows="1" v-model="data.form.drugfunction" placeholder="请输入药品功能主治"></el-input>
        </el-form-item>
        <el-form-item prop="drugusage" label="药品用法">
          <el-input type="textarea" :rows="1" v-model="data.form.drugusage" placeholder="请输入药品用法"></el-input>
        </el-form-item>
        <el-form-item prop="taboo" label="药品禁忌">
          <el-input type="textarea" :rows="1" v-model="data.form.taboo" placeholder="请输入药品禁忌"></el-input>
        </el-form-item>
        <el-form-item prop="status" label="药品状态">
          <el-radio-group v-model="data.form.status">
            <el-radio-button label="上架" value="上架"></el-radio-button>
            <el-radio-button label="下架" value="下架"></el-radio-button>
          </el-radio-group>
        </el-form-item>
<!--        <el-form-item prop="recommend" label="是否推荐">-->
<!--          <el-radio-group v-model="data.form.recommend">-->
<!--            <el-radio-button label="是" value="是"></el-radio-button>-->
<!--            <el-radio-button label="否" value="否"></el-radio-button>-->
<!--          </el-radio-group>-->
<!--        </el-form-item>-->
        <el-form-item label="药品详情" prop="content">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog title="药品详情" v-model="data.viewVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div class="editor-content-view" style="padding: 20px" v-html="data.content"></div>
      <template #footer>
    <span class="dialog-footer">
      <el-button @click="data.viewVisible = false">关 闭</el-button>
    </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {onBeforeUnmount, shallowRef} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'


const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  ids: [],
  shopList:[],
  typeList:[],
  rules: {
    name: [
      { required: true, message: '请输入名称', trigger: 'blur' }
    ],
    typeId: [
      { required: true, message: '请选择分类', trigger: 'change' }
    ],
    price: [
      { required: true, message: '请输入价格', trigger: 'blur' }
    ]
  },
  viewVisible: false,
  content: null,
})

const formRef = ref()


const baseUrl = import.meta.env.VITE_BASE_URL
const handleFileUpload = (res) => {
  data.form.img = res.data
}

const view = (content) => {
  data.content = content
  data.viewVisible = true
}

/* wangEditor5 初始化开始 */
const editorRef = shallowRef()  // 编辑器实例，必须用 shallowRef
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: baseUrl + '/files/wang/upload',  // 服务端图片上传接口
  fieldName: 'file'  // 服务端图片上传接口参数
}
// 组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// 记录 editor 实例，重要！
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 初始化结束 */

request.get('/drugShop/selectAll').then(res => {
  data.shopList = res.data
  let sellDrug = JSON.parse(data.user.sellDrug || '[]')
  // 筛选出当前的药店的 销售药品类型的范围
  data.typeList = data.typeList.filter(type => sellDrug.includes(type.name))
})

request.get('/drugType/selectAll').then(res => {
  data.typeList = res.data
})

const load = () => {
  request.get('/drug/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}
const handleAdd = () => {
  data.form = {status: '上架', recommend: '否', price:20,shopId: data.user.id }
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const add = () => {
  request.post('/drug/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/drug/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    }  else {
      ElMessage.error(res.msg)
    }
  })
}

const recommend = (row, recommend) => {
  let form = JSON.parse(JSON.stringify(row))
  form.recommend = recommend
  request.put('/drug/update', form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (!data.form.img) {
        ElMessage.warning('请上传图片')
        return
      }
      data.form.id ? update() : add()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/drug/delete/' + id).then(res => {
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
    request.delete("/drug/delete/batch", {data: data.ids}).then(res => {
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