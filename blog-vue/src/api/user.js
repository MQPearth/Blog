import request from '@/utils/request'
import qs from 'qs';

export default {
  login(name, password) {
    return request({
      url: '/user/login',
      method: 'post',
      // header: 'Content-Type:application/x-www-form-urlencoded',
      data: qs.stringify({'name': name, 'password': password})
    })
  },
  sendMail(mail) {
    return request({
      url: '/user/sendMail',
      method: 'post',
      // header: 'Content-Type:application/x-www-form-urlencoded',
      data: qs.stringify({'mail': mail})
    })
  },
  register(name, password, mail, mailCode, inviteCode) {
    return request({
      url: '/user/register',
      method: 'post',
      // header: 'Content-Type:application/x-www-form-urlencoded',
      data: qs.stringify({
        'name': name, 'password': password,
        'mail': mail, 'mailCode': mailCode,
        'inviteCode': inviteCode
      })
    })
  },
  forgetPassword(userName, mailCode, newPassword) {
    return request({
      url: '/user/forgetPassword',
      method: 'post',
      data: qs.stringify({'userName': userName, 'mailCode': mailCode, 'newPassword': newPassword})
    })
  },
  getUserMail() {
    return request({
      url: '/user/mail',
      method: 'get',
    })
  },
  updateReward(filePath) {
    return request({
      url: '/user/updateReward',
      method: 'put',
      data: qs.stringify({'imgPath': filePath})
    })
  },
  getUserReward() {
    return request({
      url: '/user/getReward',
      method: 'get',
    })
  },
  updatePassword(oldPassword, newPassword, mailCode) {
    return request({
      url: '/user/updatePassword',
      method: 'post',
      data: qs.stringify({'oldPassword': oldPassword, 'newPassword': newPassword, 'code': mailCode})
    })
  },
  updateMail(newMail, oldMailCode, newMailCode) {
    return request({
      url: '/user/updateMail',
      method: 'post',
      data: qs.stringify({'newMail': newMail, 'oldMailCode': oldMailCode, 'newMailCode': newMailCode})
    })
  },
  getUser(page, showCount) { //管理员分页查询用户数据
    return request({
      url: '/user/' + page + '/' + showCount,
      method: 'get',
    })
  },
  getUserByName(searchName, page, showCount) {   //管理层分页模糊查询用户名
    return request({
      url: '/user/search/' + page + '/' + showCount + '?userName=' + searchName,
      method: 'get',
    })
  },
  banUser(userId, userState) {
    return request({
      url: '/user/ban/' + userId + '/' + userState,
      method: 'get',
    })
  },
  logout(){
    return request({
      url: '/user/logout',
      method: 'get',
    })
  },
  getMailSendState(mail) {
    return request({
      url: '/user/getMailSendState',
      method: 'post',
      data: qs.stringify({'mail': mail})
    })
  }


}
