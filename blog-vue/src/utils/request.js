import axios from 'axios'
import {Message, MessageBox} from 'element-ui'
import store from '@/store/store'
import router from '@/router/router'


// 创建axios实例
var prod
const service = axios.create({
  baseURL: '/api', // api的base_url
  timeout: 15000, // 请求超时时间,
  header: 'Content-Type:application/x-www-form-urlencoded'
})

// request拦截器
service.interceptors.request.use(config => {
  // alert(store.state.token) //token可以通过此方式拿到
  if (store.state.token != '') {
    config.headers['Authorization'] = store.state.token // 让每个请求携带自定义token 请根据实际情况自行修改
  }
  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非200是抛错 可结合自己业务进行修改
     * 如果非200，响应在此被拦截，具体调用代码中无需再判断是否为200
     */
    const res = response.data

    if (res.code !== 200) {

      if (res.code != 403) {
        //开发环境
        // Message({
        //   message: res.code + ' : ' + res.message,
        //   type: 'error',
        //   duration: 5 * 1000
        // })
        //生产环境
        Message({
          message: res.message,
          type: 'error',
          duration: 5 * 1000
        })
      }
      
      // 403:Token过期 或 权限不足(恶意访问/被封禁) ;
      if (res.code === 403) {
        store.commit('logout');
        MessageBox.confirm('你已被登出，点击确定返回首页', '状态异常', {
          confirmButtonText: '确 定',
          cancelButtonText: '确 定',
          type: 'warning'
        }).then(() => {
          // window.location.href = '#/'
          router.push({ //路由跳转
            path: '/'
          })
          location.reload()
        }).catch(() => {

          router.push({ //路由跳转
            path: '/'
          })
          location.reload()
        })
      }
      return Promise.reject('error')
    } else {
      return response.data
    }
  },
  error => {
    console.log('错误:' + error.message)// for debug
    var message;
    if (error.response.status == 504) {
      message = '连接超时'
    }
    else {
      message = error.message
    }
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
