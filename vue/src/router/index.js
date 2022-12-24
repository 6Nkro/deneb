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
    path: '/community',
    name: 'community',
    component: () =>
      import(/* webpackChunkName: "community" */ '@/views/CommunityView')
    // redirect: ''
    // children: []
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
