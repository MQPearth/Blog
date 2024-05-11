<template>
  <div>
    <el-container>
      <el-main style="width: 75%;margin-bottom: 40px;">
        <indexBlogBugList/>
      </el-main>
      <el-aside id="aside" style="width: 20%;margin-top: 11px;margin-bottom: 40px;" class="hidden-xs-only">
        <div style="margin: 10px" >
          <introduction/>
          <!-- <newDiscuss/> -->
          <hotBlog/>
          <statisticalBlog/>
        </div>
      </el-aside>
    </el-container>
  </div>
</template>
<script>
  import introduction from '@/components/introduction'
  import newDiscuss from '@/components/newDiscuss'
  import hotBlog from '@/components/hotBlog'
  import statisticalBlog from '@/components/statisticalBlog'
  import indexBlogBugList from '@/components/indexBlogBugList'

  import 'element-ui/lib/theme-chalk/display.css';

  export default {
    name: 'index',
    components: {introduction, newDiscuss, hotBlog, statisticalBlog, indexBlogBugList}
  }
</script>
<style scoped>

</style>
