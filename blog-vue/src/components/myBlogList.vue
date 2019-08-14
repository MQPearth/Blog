<template>
  <div id="myBlogList">
    <div v-for="blog in blogList">
      <blogOverView :id="blog.id" :title="blog.title" :body="blog.body" :time="getTime(blog.time)"
                    :blogViews="blog.blogViews"
                    :discussCount="blog.discussCount" :tags="catchTagName(blog.tags)"
                    :name="blog.user.name"/>
    </div>

    <el-card class="box-card" style="margin: 20% " v-if="blogList.length <= 0">
      <div >
        还没发布过博客
      </div>
    </el-card>


    <div>
      <el-pagination
        :pager-count="5"
        :page-size="pageSize"
        background
        layout="prev, pager, next"
        :total="total"
        @current-change="currentChange"
        :current-page="currentPage"
        @prev-click="currentPage=currentPage-1"
        @next-click="currentPage=currentPage+1">
      </el-pagination>
    </div>
  </div>
</template>
<script>
  import blogOverView from '@/components/blogOverView'
  import blog from '@/api/blog'
  import date from '@/utils/date'

  export default {
    name: 'myBlogList',
    components: {blogOverView},
    data() {
      return {
        total: 0,        //数据总数
        blogList: [],   //当前页数据
        pageSize: 5,    //每页显示数量
        currentPage: 1   //当前页数
      }
    },
    created() {
      this.loadBlog();
    },
    methods: {
      getTime(time) {//将时间戳转化为几分钟前，几小时前
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
        scrollTo(0, 0);
      },
      loadBlog() { //加载数据
        blog.getMyBlog(this.currentPage, this.pageSize).then(responese => {
          this.total = responese.data.total;
          this.blogList = responese.data.rows;
        });
      }
    }
  }
</script>
<style scoped>
  #myBlogList {
    margin-top: -13px;
  }
</style>
