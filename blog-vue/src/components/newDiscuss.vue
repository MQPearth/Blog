<template>
  <el-card id="newDiscuss">
    <!--<hr />-->
    <p>
      <span style="color:#67C23A" class="el-icon-chat-line-square">最新评论</span>
    </p>
    <hr/>
    <div v-for="discuss in discussList">
      <el-link type="info" :underline="false" style="margin: 5px 0" @click="router(discuss.blog.id)">
        {{discuss.user.name}}&nbsp;:&nbsp;{{discuss.body}}《{{discuss.blog.title}}》
      </el-link>
    </div>
    <br/>
  </el-card>
</template>

<script>
  import discuss from '@/api/discuss'

  export default {
    name: 'introduction',
    data() {
      return {
        discussList: ''
      }
    },
    created() {
      discuss.getNewDiscuss().then(responese => {
        this.discussList = responese.data;
      });
    },
    methods: {
      router(id) {
        scrollTo(0, 0);
        this.$router.push({ //路由跳转
          path: '/blog/'+id
        })
      }
    }

  }
</script>
<style scoped>
  #newDiscuss {
    /*-moz-box-shadow: 0px 6px 0px #333333;*/
    /*-webkit-box-shadow: 0px 6px 0px #333333;*/
    /*box-shadow: 0px 3px 10px #333333;*/
    text-align: center;

    margin: 20px 0;
  }
</style>
