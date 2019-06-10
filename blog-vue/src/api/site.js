import request from '@/utils/request'

export default {
  getSite () {
    return request({
      url: '/site',
      method: 'get'
    })
  }
}
