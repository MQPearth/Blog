<template>
  <div id="searchBlogList" v-loading="loading">
    <p style="display: none">{{searchTxt = this.$route.params.searchTxt}}</p>
    <el-card shadow="hover" v-if="blogList.length<=0" style="margin: 20% auto">暂无符合条件的内容</el-card>
    <div>
      <blogOverView v-for="blog in blogList" :key="blog.id" :id="blog.id" :title="blog.title"
                    :body="blog.body" :time="getTime(blog.time)"
                    :blogViews="blog.blogViews"
                    :discussCount="blog.discussCount" :tags="catchTagName(blog.tags)"
                    :name="blog.user.name"/>
    </div>

    <div v-if="loading" style="margin: 35% 0">

    </div>

    <div>
      <el-pagination
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
    name: 'searchBlog',
    components: {blogOverView},
    data() {
      return {
        searchTxt: '',
        total: 0,        //数据总数
        blogList: [],   //当前页数据
        pageSize: 5,    //每页显示数量
        currentPage: 1,   //当前页数
        loading: true
      }
    },
    watch: {
      searchTxt() { //在此调用接口
        this.loadBlog();
      }
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
      },
      loadBlog() { //加载数据
        blog.userSearchBlog(this.searchTxt, this.currentPage, this.pageSize).then(responese => {
          this.total = responese.data.total;
          this.blogList = responese.data.rows;
          this.loading = false;
        });
      }
    }
  }
</script>
<style scoped>
  #searchBlogList {
    text-align: center;
    margin: -13px 5% 0 5%;
  }
</style>
