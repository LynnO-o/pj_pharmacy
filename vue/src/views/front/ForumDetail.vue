<template>
  <div style="width: 60%; margin: 10px auto">
    <div class="card" style="padding: 30px 20px; margin-bottom: 10px">
      <div style="text-align: center; margin-bottom: 20px; font-size: 24px; font-weight: bold">{{ data.forum.title }}</div>
      <div style="text-align: center; color: #666">
        <span>发布时间：{{ data.forum.time }}</span>
        <span style="margin-left: 30px">发布人：{{ data.forum.userName }}</span>
        <span  style="margin-left: 30px">浏览量：{{ data.forum.viewCount }}</span>
      </div>

      <div style="margin: 30px" v-html="data.forum.content"></div>
    </div>

    <div class="card" style="padding: 20px">
      <div style="font-size: 24px; font-weight: bold; margin-bottom: 10px">评论 {{ data.total }} 条</div>
      <div style="margin-bottom: 5px">
        <el-input type="textarea" :rows="3" v-model="data.form.content" placeholder="请输入评论内容"></el-input>
      </div>
      <div style="text-align: right">
        <el-button size="large" type="primary" @click="add(null)">发表评论</el-button>
      </div>

      <div style="margin: 20px 0">
        <div v-for="item in data.commentList" :key="item.id" >
          <div style="display: flex; grid-gap: 20px; margin-bottom: 20px">
            <img :src="item.userAvatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
            <div style="flex: 1">
              <div style="margin-bottom: 10px; color: #666">{{ item.userName }}</div>
              <div style="margin-bottom: 10px; font-size: 16px">{{ item.content }}</div>
              <div style="color: #666; margin-bottom: 10px">
                <span>时间：{{ item.time }}</span>
                <span @click="reply(item)" style="margin-left: 30px; cursor: pointer">回复</span>
                <span v-if="item.userId === data.user.id" style="margin-left: 10px; cursor: pointer" @click="del(item.id)">删除</span>
              </div>
              <div v-if="item.showReply">
                <el-input type="textarea" :rows="2" v-model="item.replyContent" placeholder="请输入回复内容"></el-input>
                <div style="margin-top: 5px; text-align: right">
                  <el-button type="primary" @click="add(item)">回复</el-button>
                </div>
              </div>
            </div>
          </div>
          <!-- 回复开始 -->
          <div style="padding-left: 70px">
            <div v-for="sub in item.children" :key="sub.id" >
              <div style="display: flex; grid-gap: 20px; margin-bottom: 20px">
                <img :src="sub.userAvatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
                <div style="flex: 1">
                  <div style="margin-bottom: 10px; color: #666">{{ sub.userName }} <span v-if="sub.parentUserName !== item.userName" style="color: #000">@{{ sub.parentUserName }}</span></div>
                  <div style="margin-bottom: 10px; font-size: 16px">{{ sub.content }}</div>
                  <div style="color: #666; margin-bottom: 10px">
                    <span>时间：{{ sub.time }}</span>
                    <span style="margin-left: 30px; cursor: pointer" @click="reply(sub)">回复</span>
                    <span v-if="sub.userId === data.user.id" style="margin-left: 10px; cursor: pointer" @click="del(sub.id)">删除</span>
                  </div>
                  <div v-if="sub.showReply">
                    <el-input type="textarea" :rows="2" v-model="sub.replyContent" placeholder="请输入回复内容"></el-input>
                    <div style="margin-top: 5px; text-align: right">
                      <el-button type="primary" @click="add(sub)">回复</el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 回复结束 -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  id: router.currentRoute.value.query.id,
  forum: {},
  total: 0,
  form: {},
  commentList: []
})

const reply = (comment) => {
  comment.showReply = !comment.showReply
  comment.replyContent = ''
}

const loadTree = () => {
  request.get('/comment/selectTree/' + data.id).then(res => {
    data.commentList = res.data
  })

  request.get('/comment/selectCount/' + data.id).then(res => {
    data.total = res.data
  })
}
loadTree()

const add = (parentComment) => {
  if (parentComment) {  // 回复
    data.form.content = parentComment.replyContent
    data.form.pid = parentComment.id
    data.form.rootId = parentComment.rootId

    parentComment.replyContent = ''
  }
  if (!data.form.content) {
    ElMessage.warning('请输入评论内容')
    return
  }
  data.form.fid = data.id
  request.post('/comment/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.form = {}
      loadTree()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('您确定删除该评论及其子评论吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/comment/deepDelete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        loadTree()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

request.put('/forum/updateViewCount/' + data.id).then(res => {
  request.get('/forum/selectById/' + data.id).then(res => {
    data.forum = res.data
  })
})
</script>

<style>

</style>