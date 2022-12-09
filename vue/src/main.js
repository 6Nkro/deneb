import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import BootstrapVue3 from 'bootstrap-vue-3'
import common from './assets/common.js'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'

import VueSweetalert2 from 'vue-sweetalert2'
import 'sweetalert2/dist/sweetalert2.min.css'

const app = createApp(App)

app.config.globalProperties.$axios = axios

app.use(store)
  .use(router)
  .use(BootstrapVue3)
  .use(VueSweetalert2)
  .use(common)
  .mount('#app')
