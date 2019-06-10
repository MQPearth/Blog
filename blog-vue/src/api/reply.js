import request from '@/utils/request'
import qs from 'qs';

export default {
  sendReply(discussId, replyBody, rootId) {
    return request({
      url: '/reply/' + discussId,
      method: 'post',
      data: qs.stringify({'replyBody': replyBody, 'rootId': rootId})
    })
  }
}
