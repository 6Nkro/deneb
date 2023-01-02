import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/library',
    name: 'library',
    component: () =>
      import(/* webpackChunkName: "library" */ '@/views/LibraryView')
  },
  {
    path: '/share',
    name: 'share',
    component: () =>
      import(/* webpackChunkName: "share" */ '@/views/ShareView'),
    redirect: '/share/list/1',
    children: [
      {
        path: 'list/:page',
        name: 'shareList',
        component: () =>
          import(/* webpackChunkName: "share" */ '@/views/ShareView')
      }
    ]
  }

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
