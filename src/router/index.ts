import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/home/HomeView.vue'
import InnerView from '@/views/inner/InnerView.vue'
import LoginView from '@/views/login/LoginView.vue'
import NotFound from '@/views/NotFound.vue'
import TestView from '@/test/TestView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/inner',
      name: 'inner',
      component: InnerView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: NotFound
    },
    {
      path: '/test',
      name: 'test',
      component: TestView
    }
  ],
})

// router.beforeEach((to, from, next) => {
//   const isAuthenticated = !!localStorage.getItem('userToken')
//
//   if (to.meta.requiresAuth && !isAuthenticated) {
//     next('/login')
//   } else if (to.meta.requiresGuest && isAuthenticated) {
//     next('/home')
//   } else {
//     next()
//   }
// })

export default router
