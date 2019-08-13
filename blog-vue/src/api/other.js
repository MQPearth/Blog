import request from '@/utils/request'
import qs from 'qs';

export default {
  getIPInfo(ip) {
    return request({
      url: '/ip',
      method: 'post',
      data: qs.stringify({'ip': ip})
    })
  }
}
