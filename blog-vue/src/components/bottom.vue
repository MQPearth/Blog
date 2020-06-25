<template>
  <div id="bottom">
    <div class="line"></div>
    <p id="txt">
      <el-link :underline="false" type="info">© 2020 博客客客客&nbsp;&nbsp;</el-link>
    </p>

    <div class="line"/>
  </div>
</template>

<script>
  export default {
    name: 'bottom'
  }
</script>
<style scoped>
  .line {
    width: 100%;
    height: 4px;
  }

  #bottom {
    color: #303133;
    background: #F2F6FC;
    margin-top: 90px;
  }

  .sdfsdf {
    color: #8c939d;
  }

  .contact-qq {
    width: 20px;
    height: 20px;
  }
</style>
