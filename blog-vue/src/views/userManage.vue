<template>
  <div>
    <div style="text-align: right;margin-bottom: 5px">
      <el-tooltip placement="top" effect="light" v-if="searchFlag == false">
        <div slot="content">如果不慎封禁了管理员<br/>请去数据库修改管理员状态</div>
        <el-link :underline="false" style="margin-right: 68%;color: #E6A23C"><i class="el-icon-question"></i></el-link>
      </el-tooltip>
      <el-button style="width: 7%;text-align: center" v-if="searchFlag == true" @click="returnNormal()">返回</el-button>

      <el-input placeholder="使用用户名模糊查询用户" v-model="searchName" suffix-icon="el-icon-search"
                style="width: 30%;" @keyup.enter.native="searchSubmit"/>
    </div>
    <div v-loading="loading">
      <el-table :data="userData" style="width: 100%" :border="true">
        <el-table-column label="用户名" width="180">
          <template slot-scope="scope">
            <i class="el-icon-user"></i>
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column label="邮箱" width="240">
          <template slot-scope="scope">
            <i class="el-icon-message"></i>
            <span style="margin-left: 10px">{{ scope.row.mail }}</span>
          </template>
        </el-table-column>

        <el-table-column label="赞赏码" width="150">
          <template slot-scope="scope">
            <i class="el-icon-trophy" v-if="scope.row.reward!=null">&nbsp;&nbsp;&nbsp;&nbsp;</i>
            <a href="javascript:">
              <img v-if="scope.row.reward!=null" width="30px" height="30px" @click="showImg(scope.row.reward)"
                   :src="scope.row.reward" alt="赞赏码"/>
            </a>
            <p class="el-icon-trophy" v-if="scope.row.reward==null">&nbsp;&nbsp;&nbsp;&nbsp;暂无记录</p>
          </template>
        </el-table-column>

        <el-table-column label="最后登录时间" width="180">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px" v-if="!(typeof(scope.row.login) == 'undefined') && scope.row.login!==null">{{ getTime(scope.row.login.time) }}</span>
            <span style="margin-left: 10px" v-if="(typeof(scope.row.login) == 'undefined') ||scope.row.login===null">暂无记录</span>
          </template>
        </el-table-column>

        <el-table-column label="最后登录IP" width="180">
          <template slot-scope="scope">
            <i class="el-icon-place"></i>
            <el-link :underline="false">
              <span style="margin-left: 10px" v-if="!(typeof(scope.row.login) == 'undefined') && scope.row.login!==null"
                    @click="showIPInfo(scope.row.login===null?'':scope.row.login.ip)">{{ scope.row.login.ip }}</span>
            </el-link>

            <span style="margin-left: 10px" v-if="(typeof(scope.row.login) == 'undefined') ||scope.row.login===null"
                  @click="showIPInfo(scope.row.login===null?'':scope.row.login.ip)">暂无记录</span>

          </template>
        </el-table-column>

        <el-table-column label="状态" width="130">
          <template slot-scope="scope">
            <i class="el-icon-wind-power"></i>
            <span style="margin-left: 10px" v-if="scope.row.state == 1">正常</span>
            <span style="margin-left: 10px" v-if="scope.row.state == 0">封禁</span>
          </template>
        </el-table-column>

        <el-table-column label="操作">

          <template slot-scope="scope">
            <el-button size="mini" v-if="scope.row.state == 1" type="warning" plain
                       @click="banUser(scope.row.id,(scope.row.state+1)%2)">
              封禁
            </el-button>

            <el-button size="mini" v-if="scope.row.state == 0" type="success" plain
                       @click="banUser(scope.row.id,(scope.row.state+1)%2)">
              解封
            </el-button>
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
  import user from '@/api/user'
  import other from '@/api/other'
  import date from '@/utils/date'

  export default {
    name: 'userManage',
    data() {
      return {
        userData: [],
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
          user.getUserByName(this.searchName, this.currentPage, this.pageSize).then(res => {
            this.userData = res.data.rows;
            this.total = res.data.total;
            this.loading = false;
          })
        } else {
          user.getUser(this.currentPage, this.pageSize).then(res => {
            this.userData = res.data.rows;
            this.total = res.data.total;
            this.loading = false;
          })
        }
      },
      currentChange(currentPage) { //页码更改事件处理
        this.currentPage = currentPage;
        this.load();
        scrollTo(0, 0);
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
      banUser(id, state) {
        user.banUser(id, state).then(res => {
          if (state == 1) {
            this.$message({
              message: '解封成功',
              type: 'success'
            });
          } else {
            this.$message({
              message: '封禁成功',
              type: 'success'
            });
          }
          this.load();
        });
      },
      showIPInfo(ip) {
        if (ip === '')
          return;
        this.loading = true
        other.getIPInfo(ip).then(res => {
          this.loading = false
          this.$alert('<div>' + res.text.ip2region_location + '</div>', '用户IP来源', {
            dangerouslyUseHTMLString: true
          });
        }).catch(res => {
          this.loading = false
        })
      },
      showImg(imgPath) {
        this.$alert('<div><img src=\'' + imgPath + '\' alt=\'赞赏码\' width=\'200px\' height=\'200px\'/> </div>', '用户赞赏码', {
          dangerouslyUseHTMLString: true
        });
      }
    }
  }
</script>
