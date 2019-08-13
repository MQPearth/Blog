<template>
  <div>
    <el-dialog title="发布公告" :visible.sync="sendFlag">
      <el-form style="margin-left: -5%">
        <el-form-item label="公告标题" :label-width="'120px'">
          <el-input v-model="announcementTitle" autocomplete="off" placeholder="请输入标题" maxlength="255"
                    show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="公告内容" :label-width="'120px'">
          <el-input
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 7}"
            placeholder="请输入内容 支持html标签"
            v-model="announcementBody">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sendFlag = false">取 消</el-button>
        <el-button type="primary" @click="sendAnnouncement()">确 定</el-button>
      </div>
    </el-dialog>
    <div style="text-align: right;margin-bottom: 5px">
      <el-button style="width: 12%;text-align: center" @click="sendFlag=true">发布新公告</el-button>

    </div>
    <div v-loading="loading">
      <el-table :data="announcementData" style="width: 100%" :border="true">

        <el-table-column label="公告标题" width="300">
          <template slot-scope="scope">
            <i class="el-icon-reading"></i>
            <span style="margin-left: 10px">{{ scope.row.title }}</span>
          </template>
        </el-table-column>

        <el-table-column label="公告内容" width="650">
          <template slot-scope="scope">
            <i class="el-icon-tickets"></i>
            <span style="margin-left: 10px"
                  @click="look(scope.row.title,scope.row.body)">{{substring(scope.row.body)}}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="success" plain v-if="scope.row.top==1"
                       @click="top(scope.row.id,(scope.row.top+1)%2)">置顶公告
            </el-button>

            <el-button size="mini" type="warning" plain v-if="scope.row.top==0"
                       @click="top(scope.row.id,(scope.row.top+1)%2)">取消置顶
            </el-button>

            <el-button size="mini" type="danger" plain
                       @click="deleteAnnouncement(scope.row.id)">删除
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
  import announcement from '@/api/announcement'

  export default {
    name: 'announcement',
    data() {
      return {
        announcementTitle: '',
        announcementBody: '',
        announcementData: [],
        total: 0,        //数据总数
        pageSize: 10,    //每页显示数量
        currentPage: 1,   //当前页数
        loading: true,
        sendFlag: false
      }
    },
    created() {
      this.load()
    },
    methods: {
      load() {
        announcement.getAnnouncement(this.currentPage, this.pageSize).then(res => {
          this.announcementData = res.data.rows;
          this.total = res.data.total;
          this.loading = false;
        });
      },
      currentChange(currentPage) { //页码更改事件处理
        this.currentPage = currentPage;
        this.load();
        scrollTo(0, 0);
      },
      sendAnnouncement() {
        if (this.announcementTitle.length <= 0 || this.announcementBody.length <= 0) {
          this.$message({
            message: '字段不完整',
            type: 'error'
          });
          return;
        }

        announcement.sendAnnouncement(this.announcementTitle, this.announcementBody).then(res => {
          this.$message({
            message: '发布成功',
            type: 'success'
          });
          this.announcementTitle = '';
          this.announcementBody = '';
          this.load();
          this.sendFlag = false
        })
      },
      top(id, state) {
        announcement.top(id, state).then(res => {
          if (state == 1) {
            this.$message({
              message: '取消成功',
              type: 'success'
            });
          } else {
            this.$message({
              message: '置顶成功',
              type: 'success'
            });
          }
          this.load();
        })
      },
      deleteAnnouncement(id) {
        this.$confirm('是否删除此公告?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          announcement.deleteAnnouncement(id).then(res => {
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            this.load();
          })
        }).catch(() => {
        })
      },
      substring(txt) {
        txt = txt.replace(new RegExp('<.*>', 'g'), '<标签>');

        if (txt.length > 50)
          return txt.substring(0, 50) + '...';
        else
          return txt;
      },
      look(title, body) {
        this.$alert('<div>' + body + '</div>', title, {
          dangerouslyUseHTMLString: true
        });
      }
    }
  }
</script>
