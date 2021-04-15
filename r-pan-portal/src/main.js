import Vue from 'vue'
import App from './App'
import router from './router'
import './plugins/element'
import './plugins/clipboard'
import 'font-awesome/css/font-awesome.min.css'

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
