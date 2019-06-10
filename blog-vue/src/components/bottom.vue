<template>
  <div id="bottom">
    <div class="line"></div>
    <p id="txt">Blog Copyright@2019</p>
    <p>有bug请发邮件至 <el-link :underline="false" href="mailto:1970432392@qq.com">1970432392@qq.com</el-link></p>
    <div class="line"></div>
  </div>
</template>

<script>
  export default {
    name: 'bottom'
  }
</script>
<style scoped>
  .line {
    width:100%;
    height: 4px;
  }
  #bottom{
    color: #303133;
    background: #F2F6FC;
    margin-top: 90px;
  }
</style>
