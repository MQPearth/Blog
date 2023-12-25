import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    roles: localStorage.getItem('roles') == null ? '' : localStorage.getItem('roles'),
    token: localStorage.getItem('token') == null ? '' : localStorage.getItem('token'),
    name: localStorage.getItem('name') == null ? '' : localStorage.getItem('name'),
    icon: localStorage.getItem('icon') == null ? '' : localStorage.getItem('icon')

  }, mutations: {
    updateIcon(state, icon) {
      this.state.icon = icon;
      localStorage.setItem('icon', icon)
    },
    login(state, data) {
      // 变更状态
      this.state.token = data.token;
      localStorage.setItem('token', data.token);
      //存储用户名
      this.state.name = data.name;
      localStorage.setItem('name', data.name)
      //存储用户头像
      this.state.icon = data.icon;
      localStorage.setItem('icon', data.icon)

      this.state.roles = data.roles;
      localStorage.setItem('roles', data.roles)
    },
    logout(state) {
      localStorage.removeItem('token');
      this.state.token = '';
      localStorage.removeItem('name');
      this.state.name = '';
      localStorage.removeItem('icon');
      this.state.icon = '';
      localStorage.removeItem('roles');
      this.state.roles = '';
    },
    refresh(state, token) {  //刷新token
      this.state.token = token;
      localStorage.setItem('token', token);
    }
  }
})
