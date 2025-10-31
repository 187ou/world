import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/home/HomeView.vue'
import InnerView from '@/views/inner/InnerView.vue'
import LoginView from '@/views/login/LoginView.vue'
import NotFound from '@/views/NotFound.vue'
import TestView from '@/test/TestView.vue'
import { useUserStore } from '@/stores/user.ts'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true }
    },
    {
      path: '/inner',
      name: 'inner',
      component: InnerView,
      meta: { requiresAuth: true }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { requiresGuest: true }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: NotFound
    },
    {
      path: '/test',
      name: 'test',
      component: TestView,
      meta: { requiresAuth: false }
    }
  ],
})

router.beforeEach((to, from, next) => {
  // 判断是否需要登录（结合 LocalStorage 和 SessionStorage ）
  const useStore = useUserStore()
  const userStore = useStore.getUser()
  const token = useStore.getToken()
  const isAuthenticated = !!token && !!userStore

  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
  } else if (to.meta.requiresGuest && isAuthenticated) {
    next('/home')
  } else {
    next()
  }
})

export default router
