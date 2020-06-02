import request from '@/utils/request'
import qs from 'qs';

export default {
  saveUserLike(blogId, status) {
    return request({
        url: '/userLike/saveUserLike',
        method: 'post',
        header: 'Content-Type:application/json',
        data: qs.stringify({"blog.id": blogId, "status": status})
    })
  },
  getBlogLikeCount(blogId) {
    return request({
        url: '/blog/getBlogLikeCount'+ '/' + blogId,
        method: 'get'
    })
  },
  isUserLike(blogId) {
    return request({
        url: '/userLike/isUserLike'+ '/' + blogId,
        method: 'get'
    })
  },
}
