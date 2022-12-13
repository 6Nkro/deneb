import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  }
  // {
  //   path: '/account',
  //   redirect: '/',
  //   children: [
  //     {
  //       path: 'signup',
  //       name: 'signup',
  //       component: () =>
  //         import(/* webpackChunkName: "account" */ '@/views/account/AccountSignUp')
  //     }
  //   ]
  // }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.onError(error => {
  if (/loading chunk \d* failed./i.test(error.message)) {
    window.location.reload()
  }
})

export default router
