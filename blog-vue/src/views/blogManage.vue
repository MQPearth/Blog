<template>
  <div>
    <div style="text-align: right;margin-bottom: 5px">
      <el-button style="width: 7%;text-align: center" v-if="searchFlag == true" @click="returnNormal()">返回</el-button>

      <el-input placeholder="模糊查询标题" v-model="searchName" suffix-icon="el-icon-search"
                style="width: 30%;" @keyup.enter.native="searchSubmit"/>
    </div>
    <div v-loading="loading">
      <el-table :data="blogData" style="width: 100%" :border="true">
        <el-table-column label="标题" width="400">
          <template slot-scope="scope">
            <i class="el-icon-tickets"></i>
            <span style="margin-left: 10px">{{ scope.row.title }}</span>
          </template>
        </el-table-column>

        <el-table-column label="发布时间" width="220">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px">{{ getTime(scope.row.time) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="用户" width="250">
          <template slot-scope="scope">
            <i class="el-icon-user"></i>
            <span style="margin-left: 10px">{{ scope.row.user.name }}</span>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="180">
          <template slot-scope="scope">
            <i class="el-icon-wind-power"></i>
            <span style="margin-left: 10px" v-if="scope.row.state == 1">正常</span>
            <span style="margin-left: 10px;color:#F56C6C " v-if="scope.row.state == 0">已删除</span>
          </template>
        </el-table-column>

        <el-table-column label="操作">

          <template slot-scope="scope">
            <el-button size="mini" v-if="scope.row.state == 1" type="warning" plain
                       @click="deleteBlog(scope.row.id)">
              删除
            </el-button>
            <el-tooltip placement="top" effect="light" v-if="scope.row.state == 0">
              <div slot="content">删除博客同时级联删除了标签<br/>博客此时为保留状态</div>
              <el-link :underline="false" style="margin-right: 68%;color: #E6A23C"><i class="el-icon-question"></i>
              </el-link>
            </el-tooltip>

          </template>
        </el-table-column>
      </el-table>
    </div>
    <br/>
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
  </div>
</template>
<script>
  import blog from '@/api/blog'
  import date from '@/utils/date'

  export default {
    name: 'blogManage',
    data() {
      return {
        blogData: [],
        total: 0,        //数据总数
        pageSize: 10,    //每页显示数量
        currentPage: 1,   //当前页数
        loading: true,
        searchName: '',
        searchFlag: false   //搜索状态 true 显示搜索数据 false显示正常数据
      }
    },
    created() {
      this.load()
    },
    methods: {
      load() {
        if (this.searchFlag) {
          blog.adminSearchBlog(this.searchName, this.currentPage, this.pageSize).then(res => {
            this.blogData = res.data.rows;
            this.total = res.data.total;
            this.loading = false;
          })
        }
        else {
          blog.adminGetBlog(this.currentPage, this.pageSize).then(res => {
            this.blogData = res.data.rows;
            this.total = res.data.total;
            this.loading = false;
          })
        }
      },
      currentChange(currentPage) { //页码更改事件处理
        this.currentPage = currentPage;
        this.load();
      },
      searchSubmit() {
        this.currentPage = 1;
        this.loading = true;
        this.searchFlag = true;
        this.load()
      },
      returnNormal() {
        this.currentPage = 1;
        this.searchName = ''
        this.loading = true;
        this.searchFlag = false;
        this.load()
      },
      getTime(time) {
        return date.timeago(time)
      },
      deleteBlog(id) {
        this.$confirm('是否删除此博客?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          blog.adminDeleteBlog(id).then(res => {
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            this.load();
          });
        }).catch(() => {
        })

      }
    }
  }
</script>
