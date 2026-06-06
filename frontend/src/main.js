import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/global.css'
import axios from 'axios'
import moment from 'moment'

Vue.prototype.$axios = axios
Vue.prototype.$moment = moment
axios.defaults.baseURL = '/api'
axios.defaults.timeout = 10000

axios.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return Promise.reject(error)
  }
)

Vue.filter('formatDate', function(value, format = 'YYYY-MM-DD HH:mm:ss') {
  if (value) {
    return moment(String(value)).format(format)
  }
})

Vue.use(ElementUI)
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
