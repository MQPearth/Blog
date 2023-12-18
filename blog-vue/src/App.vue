<template>
  <div id="app">
    <div id="bar">
      <menuBar/>
      <!-- <bar/> -->
    </div>
    <div id="view">
      <router-view/>
    </div>
    <div id="bottom">
      <bottom/>
    </div>
  </div>
</template>

<script>
  import bar from '@/components/bar'
  import menuBar from '@/components/menuBar'
  import bottom from '@/components/bottom'

  export default {
    components: {bar, bottom, menuBar},
    name: 'App'
  }
</script>

<style>
  #app {

    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
    text-align: center;
    /* margin-top: 55px; */
    /* background-color: rgba(255, 255, 255, 0.3); */

  }
  #bar {
    width: 100%;
    min-height: 50px;
    border-bottom: 1px solid #f0f0f0;
    background: #fff;
    opacity: .97;
    position: fixed;
    z-index: 99;
    top: 0;
    left: 0;
    right: 0;
    height: 50px;
    -webkit-transition: .5s;
    -o-transition: .5s;
    -moz-transition: .5s;
    -ms-transition: .5s;
    transition: transform 0.5s ease-in-out;
    box-sizing: border-box;
  }

  
  #view {
    margin-top: 50px;

  }
</style>
