<template>
  <div>
    <div style="margin-bottom: 10px; float: left">
      <el-date-picker
        v-model="value1"
        type="datetimerange"
        :picker-options="pickerOptions"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        align="right">
      </el-date-picker>
      <el-select v-model="type">
        <el-option key="1" label="控制层" value="1"/>
        <el-option key="2" label="SQL" value="2"/>
      </el-select>
      <el-select v-model="size">
        <el-option key="1" label="100条" value="100"/>
        <el-option key="2" label="200条" value="200"/>
      </el-select>
      <el-button type="plain" icon="el-icon-search" @click="search"/>
      <el-button type="plain" @click="switchState">{{ enable ? '关闭' : '开启' }}自动刷新</el-button>
    </div>
    <div v-loading="loading">
      <el-input id="textarea_id"
                type="textarea" :readonly="true"
                :autosize="{ minRows: 23, maxRows: 23}"
                v-model="log">
      </el-input>
    </div>
  </div>
</template>
<script>
import api from '@/api/log'
import date from '@/utils/date'

export default {
  name: 'logManage',
  data() {
    return {
      codeData: [],
      log: '',
      total: 0,        //数据总数
      pageSize: 10,    //每页显示数量
      currentPage: 1,   //当前页数
      loading: true,
      enable: false,
      type: '1',
      size: '100',
      timer: 0,
      lastDate: '',
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一天',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }]
      },
      value1: [new Date(new Date() - 3600 * 1000 * 24), new Date()],
    }
  },
  created() {
    this.load(date.format(this.value1[0]), date.format(this.value1[1]))
  },
  methods: {
    timeLoad() {
      const _this = this;
      this.timer = setInterval(function () {
        _this.load(_this.lastDate, '');
      }, 500)
    },
    load(left, right) {
      const _this = this;
      api.findNewestLog(this.type, left, right, this.size).then(res => {
        _this.loading = false;
        const textarea = document.getElementById('textarea_id');
        if (this.type === '1') {
          res.data.reverse().forEach(item => {
            let logInfo = date.format(new Date(item.date)) + "\r\nURL:[" + item.url + "]," +
              "RequestMethod:[" + item.requestMethod + "]," +
              "Args:" + item.args + "," +
              "ReturnValue:[" + item.returnValue + "]," +
              "Time:[" + item.time + "ms]," +
              "MethodName:[" + item.methodName + "]";
            this.log = this.log.concat(logInfo).concat("\r\n\r\n")
            textarea.scrollTop = textarea.scrollHeight;

          })
        } else {
          res.data.reverse().forEach(item => {
            let logInfo = date.format(new Date(item.date)) + "\r\n" +
              "sql:[" + item.sql + "]," +
              "Time:[" + item.time + "ms]";
            this.log = this.log.concat(logInfo).concat("\r\n\r\n")
            textarea.scrollTop = textarea.scrollHeight;
          })
        }
        if (res.data.length > 0) {
          _this.lastDate = date.format(new Date(res.data[res.data.length - 1].date))
        }
      })
    },
    switchState() {
      this.enable = !this.enable
      if (this.enable) {
        this.timeLoad()
      } else {
        clearInterval(this.timer)
      }
    },
    search() {
      this.enable = false
      this.log = ''
      this.load(date.format(this.value1[0]), date.format(this.value1[1]))
    }

  }
}
</script>
