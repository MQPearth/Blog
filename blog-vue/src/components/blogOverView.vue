<template>
  <el-card id="blogOverView">

    <div style="position: absolute;right: 25%;width: 10%" @mouseenter="pEnter()" @mouseleave="pLeave()">
      <span style="width: 5%" v-if="show&&(getStoreName()==name)" @click="editBlog(id)" title="编辑">
          <el-link class="el-icon-edit-outline" :underline="false"></el-link>
        </span>
      <span style="width: 5%;margin-left: 1%;"
            v-if="show&&(getStoreName()==name||getStoreRoles().indexOf('ADMIN')>-1)"
            title="删除" @click="deleteBlog(id)">
          <el-link class="el-icon-delete" :underline="false"></el-link>
        </span>
    </div>

    <div @mouseenter="pEnter()" @mouseleave="pLeave()">
      <el-link :underline="false" @click="router(id)">
        <h3 id="title">{{title}}</h3>
      </el-link>
    </div>
    <div id="body" @mouseenter="pEnter()" @mouseleave="pLeave()">
      {{bodyTran(body)}}
    </div>
    <div @mouseenter="pEnter()" @mouseleave="pLeave()">
      <p style="overflow: hidden;text-overflow:ellipsis; white-space: nowrap;">
        <span class="el-icon-time" style="width: 20%">&nbsp;{{time}}</span>

        <span class="el-icon-view" style="width: 15%">&nbsp;{{blogViews}}</span>

        <span class="el-icon-chat-line-square" style="width: 10%">&nbsp;{{discussCount}}</span>

        <span class="el-icon-user-solid" style="width: 10%;text-align: center;">
          &nbsp;{{name}}
        </span>

        <span>
          <span v-for="tag in tags">
            <el-tag size="small" type="success" style="margin-left: 5px">{{tag}}</el-tag>
          </span>
        </span>

      </p>
    </div>
  </el-card>
</template>
<script>

  import blog from '@/api/blog'

  export default {
    name: 'blogOverView',
    props: ['id', 'title', 'body', 'time', 'blogViews', 'discussCount', 'tags', 'name'],
    data() {
      return {
        show: false
      }
    },
    methods: {
      router(id) {
        scrollTo(0, 0);
        this.$router.push({ //路由跳转
          path: '/blog/' + id
        });
      },
      pEnter() {
        this.show = true;
      },
      pLeave() {
        this.show = false;
      },
      getStoreName() { //获取store中存储的name
        return this.$store.state.name;
      },
      getStoreRoles() { //获取store中存储的roles
        return this.$store.state.roles;
      },
      deleteBlog(id) {

        this.$confirm('是否删除此博客?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if (this.$store.state.roles.indexOf('ADMIN') > -1) {
            //管理员
            blog.adminDeleteBlog(id).then(res => {
              this.$message({
                type: 'success',
                message: '删除成功'
              });
              window.location.reload()
            })
          } else {
            //普通用户
            blog.userDeleteBlog(id).then(res => {
              this.$message({
                type: 'success',
                message: '删除成功'
              });
              window.location.reload()
            })
          }
        }).catch(() => {
        });

      },
      bodyTran(body) { //将数据库中带标签的博文转换为纯文本
        var dd = body.replace(new RegExp('#', 'g'), "");
        dd = dd.replace(new RegExp('!\\[.*\\]\\(.*\\)', 'g'), "[图片]");
        dd = dd.replace(new RegExp('\\[.*\\]\\(.*\\)', 'g'), "[链接]");
        dd = dd.replace(new RegExp('-', 'g'), "");
        dd = dd.replace(new RegExp('>', 'g'), "");
        dd = dd.replace(new RegExp('`', 'g'), "");
        dd = dd.substring(0, 150)
        return dd;
      },
      editBlog(id) {
        this.$router.push({ //路由跳转
          path: '/editBlog/' + id
        })
      }

    }
  }
</script>
<style scoped>
  #blogOverView {
    text-align: left;
    /*-moz-box-shadow: 0px 6px 0px #333333;*/
    /*-webkit-box-shadow: 0px 6px 0px #333333;*/
    /*box-shadow: 0px 3px 10px #333333;*/
    margin: 25px 0px;
    padding: 5px 10px;
  }

  #title {
    color: black;
    transition: all 0.2s linear;
  }

  #title:hover {
    color: #409EFF;
    transition: all 0.2s linear;
  }

  #body {
    font-size: 14px
  }

  a {
    text-decoration: none;
  }
</style>
