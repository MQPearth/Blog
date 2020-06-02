<template>
  <div>
    <el-card id="blog">
      <el-link :underline="false" @click="back()"><i class="el-icon-back">Back</i></el-link>
      <!--为了blogId值改变事件会被watch到-->
      <p style="display: none">{{blogId = this.$route.params.blogId}}</p>

      <div id="title">
        <h2 style="text-align: center">{{title}}</h2>
      </div>
      <div style="text-align: center">
        <p>
          <span class="el-icon-time hidden-xs-only">&nbsp;{{getTime(time)}}</span>

          <span class="el-icon-view hidden-xs-only" style="margin-left: 100px">&nbsp;{{blogViews}}</span>

          <span class="el-icon-chat-line-square hidden-xs-only" style="margin-left: 100px">&nbsp;{{discussCount}}</span>
          <span class="el-icon-user-solid hidden-xs-only" style="margin-left: 150px">&nbsp;{{userName}}</span>
        </p>
        <p>
          <span>
            <span v-for="tag in catchTagName(tags)">
              <el-tag type="success" style="margin-left: 5px">{{tag}}</el-tag>
            </span>
          </span>
        </p>
      </div>


      <mavon-editor v-model="body" id="editor" :toolbarsFlag="false" :subfield="false" defaultOpen="preview"/>
      <!-- 以下是预览模式配置 -->
      <!--:toolbarsFlag="false"  :subfield="false" defaultOpen="preview"-->

      <div style="margin: 0 auto;width: 30%" class="hidden-xs-only" v-if="userReward!=undefined&&userReward!==null">
        <br/>
        <el-popover placement="bottom" width="250px" height="250px" trigger="hover">
          <img alt="打赏码" :src="userReward" width="250px" height="250px"/>
          <el-button type="text" slot="reference" icon="el-icon-trophy" round>写的不错，打赏一个</el-button>
        </el-popover>
        <el-button type="text" :class="[like?'el-tyson-good-fill':'el-tyson-good']" @click="likeThis">
          &nbsp;{{this.like == 0 ? '点赞' : '已赞'}}&nbsp;{{likeCount}}
        </el-button>
      </div>


      <el-divider/>
      <div id="discuss" class="hidden-xs-only">

        <div style="width: 50%;margin-left: 2.5%;padding-top: 2%" v-if="getStoreName()!=''">
          <el-input v-model="discussBody" placeholder="请输入评论内容" style="width: 40%" size="mini"></el-input>
          <el-button type="primary" style="width: 10%" size="mini" @click="sendDiscuss">评论</el-button>
        </div>

        <!-- 评论部分 -->
        <div v-for="discuss in discussList" id="discussList">
          <p style="margin: -5px " @mouseenter="pEnter()" @mouseleave="pLeave()">
            <el-button type="text">{{discuss.user.name}}&nbsp;&nbsp;:</el-button>
            <span style="margin-left: 10px">{{discuss.body}}</span>
            <span style="color: #909399;margin-left: 50px" class="el-icon-time">{{getTime(discuss.time)}}</span>
            <el-button type="text" style="margin-left: 5%"
                       v-if="(discuss.user.name==getStoreName()||(getStoreRoles().indexOf('ADMIN') > -1))&&replyFlag"
                       @click="deleteDiscuss(discuss.id)">删除
            </el-button>
            <el-button type="text" style="margin-left: 1%" @click="sendReply(discuss.id,null)"
                       v-if="getStoreName()!=''&&replyFlag">回复
            </el-button>

          </p>
          <!-- 评论下的回复部分 -->
          <p v-if="!(typeof(discuss.replyList) == 'undefined') && discuss.replyList.length>0"
             v-for="reply in discuss.replyList" style="margin: -5px"
             @mouseenter="pEnter()" @mouseleave="pLeave()">
            <span style="margin-left: 5%" class="el-icon-arrow-right"></span>
            <el-button type="text">{{reply.user.name}}&nbsp;&nbsp;:</el-button>

            <span v-if="reply.reply !== null">回复:</span>
            <el-button type="text" v-if="!(typeof(reply.reply) == 'undefined') && reply.reply !== null">
              {{reply.reply.user.name}}
            </el-button>

            <span style="margin-left: 10px">{{reply.body}}</span>
            <span style="color: #909399;margin-left: 50px" class="el-icon-time">{{getTime(reply.time)}}</span>

            <el-button type="text" style="margin-left: 5%"
                       v-if="(reply.user.name==getStoreName()||(getStoreRoles().indexOf('ADMIN') > -1))&&replyFlag"
                       @click="deleteReply(reply.id)">删除
            </el-button>
            <el-button type="text" style="margin-left: 1%" @click="sendReply(discuss.id,reply.id)"
                       v-if="getStoreName()!=''&&replyFlag">回复
            </el-button>
          </p>
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

    </el-card>

  </div>
</template>
<script>
  import blog from '@/api/blog'
  import discuss from '@/api/discuss'
  import reply from '@/api/reply'
  import date from '@/utils/date'
  import userLike from '@/api/userLike'

  import 'element-ui/lib/theme-chalk/display.css';

  export default {
    name: 'blog',
    data() {
      return {
        blogId: -1,//博文id
        title: '',//博文标题
        body: '',//博文内容
        discussCount: 0,//评论数
        blogViews: 0,//浏览数
        time: 0, //发布事件
        userName: '',//博客用户名
        tags: [],  //博文标签
        userReward: '',//博主打赏码

        total: 0,        //数据总数
        discussList: [],   //当前页数据
        pageSize: 5,    //每页显示数量
        currentPage: 1,   //当前页数

        discussBody: '',//评论内容
        replyFlag: false,  // 是否显示回复按钮
        replyBody: '',   //回复内容
        like: 0,   //未点赞：0，点赞：1
        likeCount: 0,  //点赞次数
      }
    },
    watch: {
      blogId() {//在此调用接口
        this.loadBlog();
        var w = document.documentElement.offsetWidth || document.body.offsetWidth;
        if (w < 768) {  //对应xs
          document.getElementById('editor').style.margin = '0% -4.5%';
          document.getElementById('blog').style.margin = '20px 0% 0 0%';
          document.getElementById('blog').style.padding = '0';
        }
      }
    },
    methods: {
      getTime(time) {//将时间戳转化为几分钟前，几小时前的格式
        return date.timeago(time);
      },
      catchTagName(tag) { //从tag对象数组中拿到tag.Name属性
        var tagNames = [];
        for (var i = 0; i < tag.length; i++) {
          tagNames.push(tag[i].name)
        }
        return tagNames;
      },
      currentChange(currentPage) { //页码更改事件处理
        this.currentPage = currentPage;
        this.loadBlog();
      },
      loadBlog() { //加载数据
        var cookies = this.$cookies.get('history');
        var isClick = null;
        //存在此cookies key
        if (this.$cookies.isKey('history')) {
          //此cookies key 对应的 value 中有此 博客id
          if (cookies.indexOf(this.blogId) > -1) {
            //已点击查看
            isClick = true;
          } else {
            isClick = false;
          }
        } else {
          isClick = false;
        }

        userLike.getBlogLikeCount(this.blogId).then(res => {
          this.likeCount = res.data;
        });

        userLike.isUserLike(this.blogId).then(res => {
          this.like = res.data;
        });

        blog.getBlogById(this.blogId, isClick).then(res => {
            this.title = res.data.title;
            this.body = res.data.body;
            this.discussCount = res.data.discussCount;
            this.blogViews = res.data.blogViews;
            this.time = res.data.time;
            this.userName = res.data.user.name;
            this.tags = res.data.tags;
            this.userReward = res.data.user.reward;

            //设置cookies
            // 是否存在history此key
            if (this.$cookies.isKey('history')) {
              var cookies = this.$cookies.get('history');

              //不包含此博客的id  追加id
              if (cookies.indexOf(this.blogId) <= -1) {
                cookies = cookies + ',' + res.data.id;
                this.$cookies.set('history', cookies);
              }

            } else {
              var cookies = res.data.id;
              this.$cookies.set('history', cookies);
            }
          }
        );

        discuss.getDiscussByBlogId(this.blogId, this.currentPage, this.pageSize).then(responese => {
          this.total = responese.data.total;
          this.discussList = responese.data.rows;
        });

      },
      getStoreName() { //获取store中存储的name
        return this.$store.state.name;
      }
      ,
      getStoreRoles() { //获取store中存储的roles
        return this.$store.state.roles;
      }
      ,
      pEnter() {
        this.replyFlag = true
      }
      ,
      pLeave() {
        this.replyFlag = false
      }
      ,
      sendReply(discussId, replyId) {  //发送回复
        this.$prompt('请输入回复内容', '提示', {
          confirmButtonText: '回复',
          cancelButtonText: '取消'
        }).then(({value}) => {
          if (value == null || value.length <= 0) {
            this.$message({
              type: 'error',
              message: '字段不完整'
            });
            return;
          }
          reply.sendReply(discussId, value, replyId).then(res => {
            this.$message({
              type: 'success',
              message: '回复成功'
            });
            this.loadBlog();
          })
        }).catch(() => {
        });
      }
      ,
      sendDiscuss() {  //发送评论
        if (this.discussBody.length <= 0) {
          this.$message({
            type: 'error',
            message: '字段不完整'
          });
          return;
        }

        discuss.sendDiscuss(this.blogId, this.discussBody).then(res => {
          this.$message({
            type: 'success',
            message: '评论成功'
          });
          this.discussBody = ''
          this.loadBlog();
        })
      }
      ,
      deleteDiscuss(discussId) {  //删除评论  判断是用户还是管理员 走不一样的api
        this.$confirm('是否删除此评论?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if (this.$store.state.roles.indexOf('ADMIN') > -1) {
            //管理员
            discuss.adminDeleteDiscuss(discussId).then(res => {
              this.$message({
                type: 'success',
                message: '删除成功'
              });
              this.loadBlog();
            })
          } else {
            //普通用户
            discuss.userDeleteDiscuss(discussId).then(res => {
              this.$message({
                type: 'success',
                message: '删除成功'
              });
              this.loadBlog();
            })
          }


        }).catch(() => {
        });
      }
      ,
      deleteReply(replyId) {  //删除回复  判断是用户还是管理员 走不一样的api
        this.$confirm('是否删除此回复?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if (this.$store.state.roles.indexOf('ADMIN') > -1) {
            //管理员
            discuss.adminDeleteReply(replyId).then(res => {
              this.$message({
                type: 'success',
                message: '删除成功'
              });
            })
          } else {
            //普通用户
            discuss.userDeleteReply(replyId).then(res => {
              this.$message({
                type: 'success',
                message: '删除成功'
              });
            })
          }
          this.loadBlog();
        }).catch(() => {
        });
      }
      ,
      back() {
        history.back()
      }
      ,
      likeThis() {
        userLike.saveUserLike(this.blogId, this.like == 0 ? 1 : 0).then(res => {
          this.like = this.like == 0 ? 1 : 0;
          userLike.getBlogLikeCount(this.blogId).then(res => {
            this.likeCount = res.data;
          });
        });
      }
    }
  }
</script>
<style scoped>
  #blog {
    margin: 20px 5% 0 5%;
    padding: 20px;
    text-align: left;
  }

  #editor {
    margin: 2% 2%;
    height: 100%;
  }

  #discuss {
    margin: 15px 5% 0 5%;
  }

  #discussList {
    text-align: left;
    padding: 2% 1% 1% 3%;
  }

  /*第三方icon，点赞&取消点赞*/
  [class^="el-tyson-good"], [class*="el-tyson-good"] {
    font-family:"iconfont" !important;
    font-size:16px;
    font-style:normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }

  [class^="el-tyson-good-fill"], [class*="el-tyson-good-fill"] {
    font-family:"iconfont" !important;
    font-size:16px;
    font-style:normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }
</style>
