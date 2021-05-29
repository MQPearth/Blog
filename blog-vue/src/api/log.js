import request from '@/utils/request'
import qs from "qs";


export default {
  findNewestLog(type, left, right, size) {
    return request({
      url: '/log/findNewestLog',
      method: 'post',
      data: qs.stringify({'type': type, 'left': left, 'right': right, 'size': size})
    })
  }
}
