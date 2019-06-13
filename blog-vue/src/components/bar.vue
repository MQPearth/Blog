<template>
  <el-card id="bar">
    <div>
      <el-row>
        <el-menu :default-active="activeIndex" class="el-menu-demo" :router="true" mode="horizontal"
                 @select="handleSelect">


          <el-menu-item index="1" route="/">首页</el-menu-item>
          <el-menu-item index="2" style="margin-left: 2%" route="/message">留言板</el-menu-item>
          <el-menu-item index="3" style="margin-left: 2%" route="/announcement">本站公告</el-menu-item>


          <el-menu-item id="space" index="" @click="egg" class="hidden-xs-only"/>
          <!--<el-menu-item/>-->


          <div style="width: 20%;float: left;margin: 10px 2% 0px -5%" class="hidden-xs-only">
            <el-input placeholder="搜索博客" v-model="searchTxt" suffix-icon="el-icon-search"
                      @keyup.enter.native="searchSubmit"/>
          </div>


          <el-menu-item class="hidden-xs-only" v-if="this.$store.state.token==''" index="" @click="loginFormVisible = true">
            <el-button type="text">登录</el-button>
          </el-menu-item>

          <el-menu-item class="hidden-xs-only" v-if="this.$store.state.token==''" index="" @click="registerFormVisible = true">
            <el-button type="text">注册</el-button>
          </el-menu-item>

          <el-dialog title="登录" :visible.sync="loginFormVisible" width="20%">
            <el-form :model="form">
              <el-form-item :label-width="formLabelWidth">
                <el-input v-model="form.loginName" placeholder="用户名" prefixIcon="el-icon-user-solid"/>
              </el-form-item>
              <el-form-item :label-width="formLabelWidth">
                <el-input v-model="form.loginPwd" placeholder="密码" :show-password="form.loginShowPwd"
                          @keyup.enter.native="userLogin" prefixIcon="el-icon-lock"/>
              </el-form-item>
              <el-button @click="userLogin">登录&nbsp;&nbsp;<i class="el-icon-check"></i></el-button>
              <router-link to="/forgetPwd">
                <el-button @click="loginFormVisible = false">忘记密码&nbsp;&nbsp;<i class="el-icon-right"></i></el-button>
              </router-link>
            </el-form>
          </el-dialog>

          <el-dialog title="注册" :visible.sync="registerFormVisible" width="30%">

            <el-form :model="form">
              <el-form-item :label-width="formLabelWidth">
                <el-input v-model="form.registerName" placeholder="用户名" prefixIcon="el-icon-user-solid"/>
              </el-form-item>
              <el-form-item :label-width="formLabelWidth">
                <el-input v-model="form.registerPwd" placeholder="密码" :show-password="form.loginShowPwd"
                          prefixIcon="el-icon-lock"/>
              </el-form-item>

              <el-form-item :label-width="formLabelWidth">
                <el-input v-model="form.registerConfirmPwd" placeholder="确认密码" :show-password="form.loginShowPwd"
                          prefixIcon="el-icon-bell"/>
              </el-form-item>


              <el-form-item :label-width="formLabelWidth">
                <el-input v-model="form.registerInviteCode" placeholder="邀请码" prefixIcon="el-icon-s-promotion"/>
              </el-form-item>
              <el-form-item :label-width="formLabelWidth">
                <el-input v-model="form.registerMail" placeholder="邮箱" prefixIcon="el-icon-message"/>
              </el-form-item>
              <el-form-item :label-width="formLabelWidth">
                <el-input v-model="form.registerMailCode" placeholder="邮箱验证码" prefixIcon="el-icon-key"/>
              </el-form-item>
              <el-button @click="sendMail">
                发送验证码&nbsp;&nbsp;
                <i class="el-icon-coffee-cup" v-if="!sendMailFlag"/>
                <i class="el-icon-loading" v-if="sendMailFlag"/>
              </el-button>
              <el-button @click="userRegister">注册&nbsp;&nbsp;<i class="el-icon-check"></i></el-button>
            </el-form>
          </el-dialog>


          <el-submenu class="hidden-xs-only" index="" v-if="this.$store.state.token!==''" :router="true">
            <template slot="title">[&nbsp;&nbsp;{{this.$store.state.name}}&nbsp;&nbsp;]</template>

            <el-menu-item route="/newBlog" index="4-1">&nbsp;&nbsp;&nbsp;
              <i class="el-icon-edit"></i>
              写博客
            </el-menu-item>
            <el-menu-item route="/myBlog" index="4-3">&nbsp;&nbsp;&nbsp;
              <i class="el-icon-s-home"></i>
              我的博客
            </el-menu-item>
            <el-menu-item route="/account" index="4-2">&nbsp;&nbsp;&nbsp;
              <i class="el-icon-s-tools"></i>
              账号设置
            </el-menu-item>


            <el-menu-item route="/admins/userManage" index="4-4" v-if="this.$store.state.roles.indexOf('ADMIN') > -1">
              &nbsp;&nbsp;&nbsp;
              <i class="el-icon-loading"></i>管理后台
            </el-menu-item>

            <el-menu-item @click="logout">&nbsp;&nbsp;&nbsp;
              <i class="el-icon-switch-button"></i>退出登录
            </el-menu-item>
          </el-submenu>

        </el-menu>
      </el-row>

    </div>

  </el-card>
</template>
<script>
  import user from '@/api/user'

  import 'element-ui/lib/theme-chalk/display.css';

  export default {
    name: 'bar',
    data() {
      return {
        searchTxt: '',//搜索框
        activeIndex: '1',//默认选中第一项->首页
        loginFormVisible: false,//登录框
        registerFormVisible: false,//注册框
        form: { //表单
          loginName: '',
          loginPwd: '',
          loginShowPwd: true, //登录是否显示密码
          registerName: '',//注册
          registerPwd: '',
          registerConfirmPwd: '',
          registerMail: '',
          registerMailCode: '',
          registerInviteCode: ''
        },
        formLabelWidth: '0px',
        sendMailFlag: false
      }
    },
    methods: {
      handleSelect(key, keyPath) {
//        console.log(key, keyPath);
        if (key != null && key != '')
          this.activeIndex = key
      },
      egg() {
        this.$notify({
          title: '彩蛋',
          message: '耐心等待 十分钟后获得一枚邀请码',
          type: 'success',
          duration: 120
        });
      },
      userLogin() { //登录方法
        if (this.form.loginName.length <= 0 || this.form.loginPwd.length <= 0) {
          this.$message({
            message: '字段不完整',
            type: 'error'
          });
          return;
        }
        user.login(this.form.loginName, this.form.loginPwd).then(response => {
          this.form.loginPwd = ''
          this.$store.commit('login', response.data)//存储token
          this.$message({
            message: '登录成功',
            type: 'success'
          });
          this.loginFormVisible = false
        })
      },
      logout() {  //退出登录
        this.$store.commit('logout')//清除token等信息
        this.$message({
          message: '退出成功',
          type: 'success'
        });
        this.$router.push({ //路由跳转
          path: '/'
        })
      },
      sendMail() {//发送邮件

        var reg = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);
        if (!reg.test(this.form.registerMail)) {//检测字符串是否符合正则表达式
          this.$message({
            message: '邮箱格式不正确',
            type: 'error'
          });
          return;
        }
        this.sendMailFlag = true;
        user.sendMail(this.form.registerMail).then(res => {
          this.$message({
            message: '发送成功',
            type: 'success'
          });
          this.sendMailFlag = false;
        }).catch(() => {
          this.sendMailFlag = false;
        })
      },
      userRegister() {//用户注册
        //判断是否输入字段
        if (this.form.registerName.length <= 0 || this.form.registerPwd.length <= 0 || this.form.registerConfirmPwd.length <= 0 ||
          this.form.registerMail.length <= 0 || this.form.registerMailCode.length <= 0 || this.form.registerInviteCode.length <= 0) {
          this.$message({
            message: '字段不符合规范',
            type: 'error'
          });
          return;
        }
        if (this.form.registerPwd != this.form.registerConfirmPwd) {
          this.$message({
            message: '两次密码不一致',
            type: 'error'
          });
          return;
        }
        if (this.form.registerPwd.length < 6) {
          this.$message({
            message: '密码长度需大于6位',
            type: 'error'
          });
          return;
        }
        var reg = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);

        if (!reg.test(this.form.registerMail)) {//检测字符串是否符合正则表达式
          this.$message({
            message: '邮箱格式不正确',
            type: 'error'
          });
          return;
        }

        user.register(this.form.registerName, this.form.registerPwd, this.form.registerMail, this.form.registerMailCode, this.form.registerInviteCode).then(res => {
          this.$message({
            message: '注册成功',
            type: 'success'
          })
          this.registerFormVisible = false
        })

      },
      searchSubmit() {
        if (this.searchTxt.length <= 0)
          return;
        this.$router.push({ //路由跳转
          path: '/searchBlog/' + this.searchTxt
        })
        this.searchTxt = '';//清空搜索框
      }
    }
  }
</script>
<style>
  #bar {
    margin-top: -50px;
    margin-bottom: 0px;
  }

  #space {
    margin: 0 21% 0 20%;
  }
</style>
