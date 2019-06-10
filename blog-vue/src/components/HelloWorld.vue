<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <h2>Essential Links</h2>
    <el-button>按钮</el-button>
  </div>
</template>

<script>
  import site from '@/api/site'

  export default {
    name: 'HelloWorld',
    data() {
      return {
        msg: ''
      }
    },
    created() {
      site.getSite().then(responese => {
        this.msg = responese.code;
      });
    }

  }
</script>
