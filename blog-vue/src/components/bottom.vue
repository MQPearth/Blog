
<template>
  <div id="bottom" class="footer">
    <div class="line"></div>
    <div class="icp">Copyright © {{year}} {{author}}  <a href="http://beian.miit.gov.cn/" target="_blank">{{ record }}</a></div>
    <!-- <p id="txt">

      <el-link :underline="false" type="info">Copyright © 2023-2024 代码学习资料&nbsp;&nbsp;</el-link>
      <span class="footer-nav-item"><a href="https://beian.miit.gov.cn/" target="_blank">蜀ICP备2021011226号-2</a></span>
      
    </p> -->

    <div class="line"/>
  </div>
</template>

<script>
  
  export default {
    name: 'bottom',
    data(){
      return{
        year: "2023-" + new Date().getFullYear(),        // 一般都是最新的一年
        author: '代码学习资料',					// 作者名/备注
        record: '蜀ICP备2021011226号-2'			// 备案号
      }
    }
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
    margin-bottom: -24px;
  }
  .footer {  
    position: fixed;  
    bottom: 0;  
    width: 100%;  
    /* 其他样式属性，如背景颜色、字体大小等 */  
}  

  .sdfsdf {
    color: #8c939d;
  }

  .contact-qq {
    width: 20px;
    height: 20px;
  }
  .icp {
    position: absolute;
    bottom: 0;
    right: 0;
    margin: 10px 0;
    width: 100%;
    height: 36px;
    font-size: small;
    white-space: pre;
    text-align: center;
    color: gray;
    z-index: 1000;
  }
  .icp > a {
      color: gray;
      text-decoration: none;
  }
  .icp > a:hover {
      color: rgb(80, 115, 117);
      text-decoration: none;
  }

</style>
