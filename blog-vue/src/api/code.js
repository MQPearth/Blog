import request from '@/utils/request'


export default {
  getCode(page, showCount) {
    return request({
      url: '/code/' + page + '/' + showCount,
      method: 'get'
    })
  },
  generateCode() {
    return request({
      url: '/code',
      method: 'post'
    })
  }
}
