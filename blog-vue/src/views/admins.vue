<template>
  <div style="width: 99%">
    <el-container>
      <el-aside style="width: 20%; margin-bottom: -3%">
        <div id="aside">
          <el-menu :router="true" :default-active="activeIndex" class="el-menu-vertical-demo" @select="handleSelect">
            <el-menu-item class="menuItem" index="1" route="/admins/userManage">
              <i class="el-icon-user-solid"></i>
              <span slot="title">用户管理</span>
            </el-menu-item>

            <el-menu-item class="menuItem" index="2" route="/admins/codeManage">
              <i class="el-icon-menu"></i>
              <span slot="title">邀请码管理</span>
            </el-menu-item>

            <el-menu-item class="menuItem" index="3" route="/admins/announcementManage">
              <i class="el-icon-data-board"></i>
              <span slot="title">公告管理</span>
            </el-menu-item>

            <el-menu-item class="menuItem" index="4" route="/admins/blogManage">
              <i class="el-icon-document"></i>
              <span slot="title">博客管理</span>
            </el-menu-item>
          </el-menu>
        </div>
      </el-aside>
      <el-main style="width: 75%">
        <router-view/>
      </el-main>

    </el-container>

  </div>
</template>
<script>
  //vue不支持文件名为admin的文件（也许吧）
  export default {
    name: 'admins',
    data() {
      return {
        activeIndex: '1'
      }
    },
    watch: {
      // 监控当前页面path，防止刷新页面显示错误
      '$route.path': {
        deep: true,
        immediate: true,
        handler(to, from) {
          if (to === '/admins/codeManage') {
            this.activeIndex = '2'
          } else if (to === '/admins/announcementManage') {
            this.activeIndex = '3'
          } else if (to === '/admins/blogManage') {
            this.activeIndex = '4'
          }
        }
      }
    },
    methods: {
      handleSelect(key, keyPath) {
//        console.log(key, keyPath);
        this.activeIndex = key
      }
    }
  }
</script>
<style scoped>
  .menuItem {
    margin: 15% 0;
  }

  #aside {
    margin: 10px 0px 10px 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
  }
</style>
