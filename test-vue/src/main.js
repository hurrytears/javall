// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import qs from 'qs'

Vue.config.productionTip = false

// 设置反向代理，前端请求默认发送到 https://localhost:8082/
var axios = require('axios')
axios.defaults.baseURL = 'http://localhost:8082/'
axios.defaults.headers = {
  'content-type': 'application/x-www-form-urlencoded',
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Methods': 'GET, POST, PATCH, PUT, DELETE, OPTIONS',
  'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token'
}
// 全局注册，之后可在其他组件中通过 this.$axios 发送数据
Vue.prototype.$axios = axios
Vue.prototype.$qs = qs
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
