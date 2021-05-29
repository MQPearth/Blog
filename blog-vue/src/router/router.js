import Vue from 'vue'
import Router from 'vue-router'

import index from '@/views/index'
import message from '@/views/message'
import announcement from '@/views/announcement'
import newBlog from '@/views/newBlog'
import account from '@/views/account'
import admins from '@/views/admins'
import forgetPwd from '@/views/forgetPwd'
import searchBlog from '@/views/searchBlog'
import blog from '@/views/blog'
import myBlog from '@/views/myBlog'
import editBlog from '@/views/editBlog'
import notfound from '@/views/notfound'

import userManage from '@/views/userManage'
import codeManage from '@/views/codeManage'
import announcementManage from '@/views/announcementManage'
import blogManage from '@/views/blogManage'
import logManage from '@/views/logManage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: index
    },
    {
      path: '/message',
      name: 'message',
      component: message
    },
    {
      path: '/announcement',
      name: 'announcement',
      component: announcement
    },
    {
      path: '/newBlog',
      name: 'newBlog',
      component: newBlog
    },
    {
      path: '/account',
      name: 'account',
      component: account
    },
    {
      path: '/admins',
      name: 'admins',
      component: admins,
      children: [  //这里就是二级路由的配置
        {
          path: 'userManage',
          name: 'userManage',
          component: userManage
        },
        {
          path: 'codeManage',
          name: 'codeManage',
          component: codeManage
        },
        {
          path: 'announcementManage',
          name: 'announcementManage',
          component: announcementManage
        },
        {
          path: 'blogManage',
          name: 'blogManage',
          component: blogManage
        },
        {
          path: 'logManage',
          name: 'logManage',
          component: logManage
        }
      ]
    },
    {
      path: '/forgetPwd',
      name: 'forgetPwd',
      component: forgetPwd
    },
    {
      path: '/searchBlog/:searchTxt',
      name: 'searchBlog',
      component: searchBlog
    },
    {
      path: '/blog/:blogId',
      name: 'blog',
      component: blog
    },
    {
      path: '/myBlog',
      name: 'myBlog',
      component: myBlog
    },
    {
      path: '/editBlog/:blogId',
      name: 'editBlog',
      component: editBlog
    },
    {
      path: '*',
      name: 'notfound',
      component: notfound
    }
  ]
})
