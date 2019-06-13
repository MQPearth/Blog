<template>
  <div>
    <el-container>
      <el-main style="width: 75%">
        <indexBlogList/>
      </el-main>
      <el-aside id="aside" style="width: 20%" class="hidden-xs-only">
        <div style="margin: 10px" >
          <introduction/>
          <newDiscuss/>
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
  import indexBlogList from '@/components/indexBlogList'

  import 'element-ui/lib/theme-chalk/display.css';

  export default {
    name: 'index',
    components: {introduction, newDiscuss, hotBlog, statisticalBlog, indexBlogList}
  }
</script>
<style scoped>

</style>
