<template>
  <el-card id="forgetPwd">
    <div style="margin: auto;width: 25%">
      <el-form ref="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="userName" placeholder="你的用户名"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="newPassword" placeholder="新密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="confirmPassword" placeholder="再次输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="mail" placeholder="你的邮箱"></el-input>
        </el-form-item>
        <el-form-item label="验证码">

          <el-input v-model="mailCode" placeholder="收到的验证码"></el-input>

        </el-form-item>
      </el-form>
    </div>
    <div>
      <el-button type="text" @click="sendMail">
        发送验证码&nbsp;&nbsp;
        <i class="el-icon-coffee-cup" v-if="!sendMailFlag"/>
        <i class="el-icon-loading" v-if="sendMailFlag"/>
      </el-button>
      <el-button type="text" @click="forgetPwd">更改密码&nbsp;&nbsp;<i class="el-icon-potato-strips"></i></el-button>
    </div>
  </el-card>
</template>
<script>
  import user from '@/api/user'

  export default {
    name: 'forgetPwd',
    data() {
      return {
        userName: '',
        mail: '',
        mailCode: '',
        newPassword: '',
        confirmPassword: '',
        sendMailFlag: false
      }
    },
    methods: {
      sendMail() {//发送邮件
        var reg = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);
        if (this.userName.length <= 0) {
          this.$message({
            message: '请填写用户名',
            type: 'error'
          });
          return;
        }
        if (!reg.test(this.mail)) {//检测字符串是否符合正则表达式
          this.$message({
            message: '邮箱格式不正确',
            type: 'error'
          });
          return;
        }
        this.sendMailFlag = true;
        user.sendMail(this.mail).then(res => {
          this.$message({
            message: '发送成功',
            type: 'success'
          });
          this.sendMailFlag = false;
        }).catch(()=>{
          this.sendMailFlag = false;
        })
      },
      forgetPwd() {
        if (this.userName.length <= 0) {
          this.$message({
            message: '请填写用户名',
            type: 'error'
          });
          return;
        }
        var reg = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);
        if (!reg.test(this.mail)) {//检测字符串是否符合正则表达式
          this.$message({
            message: '邮箱格式不正确',
            type: 'error'
          });
          return;
        }
        if (this.newPassword.length < 6) {
          this.$message({
            message: '密码不能小于6位',
            type: 'error'
          });
          return;
        }
        if (this.newPassword != this.confirmPassword) {
          this.$message({
            message: '两次输入的密码不一致',
            type: 'error'
          });
          return;
        }
        if (this.mailCode.length <= 0) {
          this.$message({
            message: '请输入验证码',
            type: 'error'
          });
          return;
        }
        user.forgetPassword(this.userName, this.mailCode, this.newPassword).then(res => {

          this.$message({
            message: '更改成功',
            type: 'success'
          });
          scrollTo(0, 0);
          this.$router.push({ //路由跳转
            path: '/'
          })
        })
      }
    }
  }
</script>
<style scoped>
  #forgetPwd {
    margin: 20px 5% 6% 5%;
  }

</style>
