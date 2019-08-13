import request from '@/utils/request'
import qs from 'qs';

export default {
  getHotBlog() {
    return request({
      url: '/blog/hotBlog',
      method: 'get'
    })
  },
  getStatisticalBlogByMonth() {
    return request({
      url: '/blog/statisticalBlogByMonth',
      method: 'get'
    })
  },
  getBlogHome(page, showCount) {
    return request({
      url: '/blog/home/' + page + '/' + showCount,
      method: 'get'
    })
  },
  getBlogById(id, isClick) {
    return request({
      url: '/blog/' + id + '/' + isClick,
      method: 'get'
    })
  },
  getMyBlog(page, showCount) {
    return request({
      url: '/blog/myblog/' + page + '/' + showCount,
      method: 'get'
    })
  },
  sendBlog(blogTitle, blogBody, tagId) {  //发布博客
    // alert(qs.stringify({'blogTitle': blogTitle, 'blogBody': blogBody,'tagId':tagId}))
    return request({
      url: '/blog',
      method: 'post',
      data: qs.stringify({'blogTitle': blogTitle, 'blogBody': blogBody, 'tagId': tagId})
    })
  },
  uploadImg(formdata) {
    return request({
      url: '/blog/uploadImg',
      method: 'post',
      data: formdata,
      headers: {'Content-Type': 'multipart/form-data'},
    })
  },
  editBlog(blogId, blogTitle, blogBody, tagId) {  //发布博客
    return request({
      url: '/blog/' + blogId,
      method: 'put',
      data: qs.stringify({'blogTitle': blogTitle, 'blogBody': blogBody, 'tagId': tagId})
    })
  },
  adminDeleteBlog(blogId) { //管理员删除博客
    return request({
      url: '/blog/admin/' + blogId,
      method: 'delete'
    })
  },
  userDeleteBlog(blogId) { //普通用户删除博客
    return request({
      url: '/blog/' + blogId,
      method: 'delete'
    })
  },
  adminGetBlog(page, showCount) {
    return request({
      url: '/blog/AllBlog/' + page + '/' + showCount,
      method: 'get'
    })
  },
  adminSearchBlog(searchTxt, page, showCount) {
    return request({
      url: '/blog/searchAllBlog/' + page + '/' + showCount + '?search=' + searchTxt,
      method: 'get'
    })
  },
  userSearchBlog(searchTxt, page, showCount) {
    return request({
      url: '/blog/searchBlog/' + page + '/' + showCount + '?search=' + searchTxt,
      method: 'get'
    })
  }
}
