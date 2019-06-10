<template>
  <el-card id="message" v-loading="loading">

    <div v-if="loading" style="margin: 35% 0"></div>
    <div v-for="message in messageList" style="text-align: left;padding-left: 2%">
      <p style="color:#409EFF" class="el-icon-paperclip">&nbsp;{{message.name}}</p>
      <p style="color: #303133">
        {{message.body}}
      </p>

      <el-divider content-position="right">
        <el-link :underline="false" class="el-icon-delete" v-if="getStoreRoles().indexOf('ADMIN') > -1"
                 @click="deleteMessage(message.id)"/>
      </el-divider>
    </div>


    <div style="padding-bottom: 4%">
      <el-pagination
        :page-size="pageSize"
        background
        layout="prev, pager, next"
        :total="total"
        @current-change="currentChange"
        :current-page="currentPage"
        @prev-click="currentPage=currentPage-1"
        @next-click="currentPage=currentPage+1"
        :hide-on-single-page="true">
      </el-pagination>
    </div>

    <div style="width: 50%;margin-left: -100px;padding-top: 2%">
      <el-input v-model="messageBody" placeholder="请输入留言内容" style="width: 40%" size="small"></el-input>
      <el-button type="primary" style="width: 10%" size="small" @click="sendMessage">留言</el-button>
    </div>
  </el-card>
</template>
<script>
  import message from '@/api/message'

  export default {
    name: 'message',
    data() {
      return {
        total: 0,        //数据总数
        messageList: [],   //当前页数据 防止空页面的突兀
        pageSize: 5,    //每页显示数量
        currentPage: 1,   //当前页数
        messageBody: '',
        loading: true //是否加载中
      }
    },
    created() {
      this.loadMessage();
    },
    methods: {
      loadMessage() {
        message.getMessage(this.currentPage, this.pageSize).then(res => {
          this.total = res.data.total;
          this.messageList = res.data.rows;
          this.loading = false
        })
      },
      currentChange(currentPage) { //页码更改事件处理
        this.currentPage = currentPage;
        this.loadMessage();
      },
      sendMessage() {
        message.sendMessage(this.messageBody).then(res => {
          this.$message({
            type: 'success',
            message: '留言成功'
          });
          this.loadMessage();
        })
      },
      deleteMessage(id) {
        this.$confirm('是否删除此留言?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          message.deleteMessage(id).then(res => {
            this.$message({
              type: 'success',
              message: '删除成功'
            });
            this.loadMessage();
          })
        }).catch(() => {
        });
      },
      getStoreRoles() { //获取store中存储的roles
        return this.$store.state.roles;
      },
    },
  }
</script>
<style scoped>
  #message {
    margin: 10px 5% 0 5%;
  }
</style>
