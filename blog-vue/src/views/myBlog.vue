<template>
  <div id="myBlog">
    <el-container>
      <el-main style="width: 75%">
        <div>
          <myBlogList/>
        </div>
      </el-main>
      <el-aside id="aside" style="width: 20%">
        <div style="margin: 10px">
          <userNewDiscuss/>
        </div>
        <div>
          <myTag/>
        </div>
      </el-aside>
    </el-container>
  </div>
</template>
<script>
  import myBlogList from '@/components/myBlogList'
  import userNewDiscuss from '@/components/userNewDiscuss'
  import myTag from '@/components/myTag'

  export default {
    name: 'myBlog',
    components: {myBlogList,userNewDiscuss,myTag}
  }
</script>
<style scoped>
  #myBlog{
    margin: auto 1%;
    margin-bottom: 40px;
  }
</style>
