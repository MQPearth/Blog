<template>  
  <div class="head">  
    <div class="menu-bar">
      <span>
        <img class="logoImg" src="../assets/qqkpilogo.png"/>
      </span>
      <ul id="navbar-nav">  
          <li><a index="1" href="/#/" @click="activeIndex = '1'">
              博客
            </a>
          </li>  
          <!-- 备忘 -->
          <li>
            <a index="2" href="/#/message" @click="activeIndex = '2'">
              留言
            </a>
          </li>  
          <li>
            <a id="bugA" index="3" href="/#/bug" @click="activeIndex = '3'">
              BUG
            </a>
          </li>  
          <li>
            <a index="4" href="/#/announcement" @click="activeIndex = '4'">
              关于
            </a>
          </li>  
          <li>
            <a index="5" href="/#/label" @click="activeIndex = '5'">
              标签
            </a>
          </li>  
      </ul>  
      <ul id="navbar-button">  
          <li id="searchLi" view="search" class="hidden-xs hidden-sm">
            <div class="search-box">
              <a class="search-btn" @click="searchSubmit">
                <i class="fa fa-search el-icon-search" aria-hidden="true"></i>
              </a>
              <input type="text" class="search-txt" placeholder="搜索" v-model="searchTxt"/>
              <div class="search-line"></div>
            </div>
          </li>  
          <li>
            <el-button plain class="loginBut" size="small"  v-if="this.$store.state.token==''" index="" @click="loginFormVisible = true">登录</el-button>
            <el-menu :router="true" :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect" style="height: 50px; border: 0;">
              <el-submenu id="one" class="hidden-xs-only" index="4" v-if="this.$store.state.token!==''" :router="true">
                <template slot="title">欢迎回来，尊敬的&nbsp;&nbsp;{{this.$store.state.name}}&nbsp;&nbsp;
                  <el-avatar id="userIconId" :src="this.$store.state.icon" @error="errorHandler">
                    <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
                  </el-avatar>
                </template>
                

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
                  <i class="el-icon-switch-button"/>退出登录
                </el-menu-item>
              </el-submenu>
            </el-menu>
            
          </li>  
          
      </ul>  
    </div>
    
    

    <el-dialog title="登录" :append-to-body="true" :visible.sync="loginFormVisible" width="21%">
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


    
  </div>  
</template>  

<script>
  import user from '@/api/user'

  import 'element-ui/lib/theme-chalk/display.css';

  export default {
    name: 'bar',
    data() {
      return {
        hodColor1: '',
        hodColor2: '',
        hodColor3: '',
        hodColor4: '',
        hodColor5: '',
        searchTxt: '',//搜索框
        activeIndex: '1',
        loginFormVisible: false,//登录框
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
    watch: {
      // 监控当前页面path，防止刷新页面显示错误
      '$route.path': {
        deep: true,
        immediate: true,
        handler(to, from) {
          if (to === '/myBlog') {
            this.activeIndex = '1'
          }
          // console.log(to, from);
          
          // if (String(to).startsWith('/searchBlog')) {
          //   this.activeIndex = 1
          // }
          
          // if (to === '/') {
          //   this.activeIndex = '1'
          // } else if (to === '/message') {
          //   this.activeIndex = '2'
          // } else if (to === '/announcement') {
          //   this.activeIndex = '3'
          // } else {
          //   this.activeIndex = '4'
          // }
          // this.exColor(this.activeIndex)
        }
      }
    },
    methods: {
      selectMenuItem(item){
        this.selectedMenuItem = item; 
        this.activeIndex = item;
      },
      errorHandler() {
        return true
      },
      exColor(index){
        switch (index) {
          case '1':
            this.hodColor1 = '#03a9f4'
            this.hodColor2 = '#383838'
            this.hodColor3 = '#383838'
            this.hodColor4 = '#383838'
            this.hodColor5 = '#383838'
            break;
          case '2':
            this.hodColor1 = '#383838'
            this.hodColor2 = '#03a9f4'
            this.hodColor3 = '#383838'
            this.hodColor4 = '#383838'
            this.hodColor5 = '#383838'
            break;
          case '3':
            this.hodColor1 = '#383838'
            this.hodColor2 = '#383838'
            this.hodColor3 = '#03a9f4'
            this.hodColor4 = '#383838'
            this.hodColor5 = '#383838'
            break;
          case '4':
            this.hodColor1 = '#383838'
            this.hodColor2 = '#383838'
            this.hodColor3 = '#383838'
            this.hodColor4 = '#03a9f4'
            this.hodColor5 = '#383838'
            break;
          case '5':
            this.hodColor1 = '#383838'
            this.hodColor2 = '#383838'
            this.hodColor3 = '#383838'
            this.hodColor4 = '#383838'
            this.hodColor5 = '#03a9f4'
            break;
          default:
            break;
        }
        
      },
      handleSelect(key, keyPath) {
        // console.log(key, keyPath);
        if (key != null && key !== '')
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
        user.logout().then(res => {
          this.$store.commit('logout')//清除token等信息
          this.$message({
            message: '退出成功',
            type: 'success'
          });
          this.$router.push({ //路由跳转
            path: '/'
          })
        })

      },
      sendMail() {//发送邮件

        const reg = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);
        if (!reg.test(this.form.registerMail)) {//检测字符串是否符合正则表达式
          this.$message({
            message: '邮箱格式不正确',
            type: 'error'
          });
          return;
        }
        this.sendMailFlag = true;
        const mail = this.form.registerMail;
        const _this = this;
        user.sendMail(mail).then(res => {
          //成功调用接口后设置定时器，每隔600ms查询一次邮件状态
          var intervalId = setInterval(function () {
            user.getMailSendState(mail).then(res => {
              //邮件状态发生改变时
              if (res.data !== '0') {
                //清除定时器
                clearInterval(intervalId);
                if (res.data === '1') {
                  _this.$message.success('发送成功');
                } else {
                  _this.$message.error('发送失败');
                }
                _this.sendMailFlag = false;
              }
            });
          }, 600);

        }).catch(() => {
          this.sendMailFlag = false;
          this.$message.error('发送失败');
        })
      },
      // userRegister() {//用户注册
      //   //判断是否输入字段
      //   if (this.form.registerName.length <= 0 || this.form.registerPwd.length <= 0 || this.form.registerConfirmPwd.length <= 0 ||
      //     this.form.registerMail.length <= 0 || this.form.registerMailCode.length <= 0 || this.form.registerInviteCode.length <= 0) {
      //     this.$message({
      //       message: '字段不符合规范',
      //       type: 'error'
      //     });
      //     return;
      //   }
      //   if (this.form.registerPwd !== this.form.registerConfirmPwd) {
      //     this.$message({
      //       message: '两次密码不一致',
      //       type: 'error'
      //     });
      //     return;
      //   }
      //   if (this.form.registerPwd.length < 6) {
      //     this.$message({
      //       message: '密码长度需大于6位',
      //       type: 'error'
      //     });
      //     return;
      //   }
      //   var reg = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);

      //   if (!reg.test(this.form.registerMail)) {//检测字符串是否符合正则表达式
      //     this.$message({
      //       message: '邮箱格式不正确',
      //       type: 'error'
      //     });
      //     return;
      //   }

      //   user.register(this.form.registerName, this.form.registerPwd, this.form.registerMail, this.form.registerMailCode, this.form.registerInviteCode).then(res => {
      //     this.$message({
      //       message: '注册成功',
      //       type: 'success'
      //     })
      //     this.registerFormVisible = false
      //   })

      // },
      searchSubmit() {
        console.log(this.searchTxt.length);
        if (this.searchTxt.length < 0){
          return;
        }
        if(this.searchTxt.length == 0){
          console.log("t");
          this.activeIndex = '1'
          this.$router.push({ //路由跳转
            path: '/'
          })
        }else {
          this.activeIndex = '1'
          this.$router.push({ //路由跳转
            path: '/searchBlog/' + this.searchTxt
          })
          this.searchTxt = '';//清空搜索框
          this.$message({
            message: '搜索成功',
            type: 'success',
            duration: 800
          })
        }
        
      }
    }
  }
</script>
<style>  
  .menu-bar {  
    background-color: #fff;
    /* color: #fff;   */
  }  
  .menu-bar span {  
    margin-top: -5px;
    margin-left: 13%;
    float: left;
  }  
  #navbar-nav {  
    float: left;
    margin: 0;
    box-sizing: border-box;
    
  }
    
  #navbar-nav ul {  
    list-style-type: none;  
    margin: 0;  
    padding: 0; 
    margin-left: 1%; 
    margin-top: -0.55%; 
    float: left;
    display: flex; 
  }  
    
  #navbar-nav li {  
    display: inline-block;  
    margin: 0;
    margin-bottom: 30px;
    margin-top: 6px;
    float: left;
  }  
  .logoImg{  
    height: 32px; 
    margin-top: 15px;
  } 
 
    
  #navbar-nav a {  
    color: #383838;
    font-size: 15px;
    padding: 10px 15px; 
    display: block;
    text-decoration: none;
    /* margin-top: 15px;  */
    /* display: block;  
    padding: 10px 15px;  
    text-decoration: none;   */
  }  
  #navbar-nav a:hover, a:focus, a:active {
    color: #03a9f4;
    text-decoration: none;
  }
  #navbar-button {
    /* display: absolute;  */
    /* margin-left: 70%; 
    margin-bottom: 150px; */
    margin: 0;
    float: right;
    box-sizing: border-box;
    padding: 0;
    margin-right: 200px;
  }
  #navbar-button ul {  
    list-style-type: none;  
    margin: 0;  
    padding: 0; 
    margin-left: 1%; 
    margin-top: -0.55%; 
    float: left;
    display: flex; 
  }  
    
  #navbar-button li {  
    display: inline-block;  
    margin: 0;
    /* margin-bottom: 30px;
    margin-top: 6px; */
    float: left;
  }  
  #navbar-button span {  
    margin-top: 0;
    /* margin-bottom: 30px;
    margin-top: 6px; */
    /* float: left; */
  }  
    
  
  /* #navbar-nav a:hover {  
    color: #03a9f4;
  }   */
  .menu-bar .loginBut {  
    margin-top: 12px;
  }

  .search-line {
    position: absolute;
    left: 40px;
    bottom: 10px;
    width: 0px;
    height: 2px;
    /* background-color: rgb(251, 121, 0); */
    transition: 1024s;
  }
  .search-box {
      position: relative;
      bottom: -6px;
      left: -30px;
      background-color: white;
      /* box-shadow: 0 2px 25px 0 rgba(0, 0, 0, 0.1); */
      height: 20px;
      padding: 10px;
      border-radius: 40px;
      display: flex;
  }
  .search-txt {
      border: none;
      background: none;
      outline: none;
      padding: 0;
      color: #222;
      font-size: 16px;
      line-height: 40px;
      width: 0;
      transition: 0.4s;
  }
  .search-btn {
      color: #888888;
      font-size: 24px;
      width: 20px;
      height: 20px;
      margin-top: 0px;
      border-radius: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      transition: 0.4s;
  }
  .search-box:hover .search-txt {
      width: 150px;
      padding: 0 12px;
  }
  .search-box:hover .search-btn {
      background-color: #fff;
      animation: rotate 0.4s linear;
  }
  .search-txt:focus {
      width: 150px;
      padding: 0 12px;
  }

  .search-txt:focus + .search-line {
      width: 150px;
  }

  @keyframes rotate {
      0% {
          transform: rotate(0deg);
      }
      100% {
          transform: rotate(360deg);
      }
  }
  .el-submenu__title {
    padding: 0;
    border: 0px;
    border-bottom: none;
    
  }
  #searchLi {
    height: 50px;
    top: 40px;
  }
  .el-menu--horizontal>.el-submenu.is-active .el-submenu__title {
    border-bottom: none;
    color: #303133;
  }
  #bugA {
    margin-top: 3px;
  }
  #one .el-icon-arrow-down {
    display: none;
  }
  #userIconId {
    position: absolute;
    top: 6px;
    background-position-y: 10px;
    /* float: right; */
    margin-left: 0px;
    
    
  }

  .default-a {  
    color: #383838;
  }  
    
  .selected-a {  
    color: #03a9f4; 
  }  

  
</style>