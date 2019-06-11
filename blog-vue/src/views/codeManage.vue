<template>
  <div>
    <div style="text-align: right;margin-bottom: 5px">
      <el-button style="width: 12%;text-align: center" @click="generateCode()">生成新邀请码</el-button>

    </div>
    <div v-loading="loading">
      <el-table :data="codeData" style="width: 100%" :border="true">

        <el-table-column label="邀请码" width="500">
          <template slot-scope="scope">
            <i class="el-icon-menu"></i>
            <span style="margin-left: 10px">{{ scope.row.id }}</span>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="310">
          <template slot-scope="scope">
            <i class="el-icon-wind-power"></i>
            <span style="margin-left: 10px" v-if="scope.row.state == 1">已使用</span>
            <span style="margin-left: 10px;color:#67C23A" v-if="scope.row.state == 0">未使用</span>
          </template>
        </el-table-column>

        <el-table-column label="使用用户" >
          <template slot-scope="scope">
            <i class="el-icon-user"></i>
            <span style="margin-left: 10px" v-if="scope.row.user!=null">{{ scope.row.user.name }}</span>
          </template>
        </el-table-column>

      </el-table>
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
  </div>
</template>
<script>
  import code from '@/api/code'

  export default {
    name: 'codeManage',
    data() {
      return {
        codeData: [],
        total: 0,        //数据总数
        pageSize: 10,    //每页显示数量
        currentPage: 1,   //当前页数
        loading: true
      }
    },
    created() {
      this.load()
    },
    methods: {
      load() {
        code.getCode(this.currentPage, this.pageSize).then(res => {
          this.codeData = res.data.rows;
          this.total = res.data.total;
          this.loading = false;
        });
      },
      currentChange(currentPage) { //页码更改事件处理
        this.currentPage = currentPage;
        this.load();
        scrollTo(0, 0);
      },
      generateCode() {
        code.generateCode().then(res => {
          this.$message({
            message: '生成成功',
            type: 'success'
          });
          this.load();
        })
      }
    }
  }
</script>
