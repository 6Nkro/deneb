import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import common from './assets/common.js'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import VueSweetalert2 from 'vue-sweetalert2'
import 'sweetalert2/dist/sweetalert2.min.css'

createApp(App)
  .use(store)
  .use(router)
  .use(common)
  .use(VueSweetalert2)
  .mount('#app')
