// src/stores/user.ts
import { defineStore } from 'pinia'
// import http, { HttpResult } from '@/utils/http.ts'
import http from '@/utils/http'
import type { HttpResult } from '@/utils/http'

interface UserInfo {
  id: number
  name: string
  email: string
}

interface LoginPayload {
  email: string
  password: string
}

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: null as UserInfo | null,
  }),
  actions: {
    // 登录
    async login(payload: LoginPayload) {
      const res: HttpResult<{ token: string }> = await http.post('/auth/login', payload)
      if (res.success && res.data) {
        this.token = res.data.token
        localStorage.setItem('token', this.token)
        return true
      }
      return false
    },

    // 退出
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('token')
    },

    // 获取用户信息
    async fetchUserInfo() {
      const res: HttpResult<UserInfo> = await http.get('/auth/me')
      if (res.success && res.data) {
        this.user = res.data
        return res.data
      }
      return null
    }
  }
})
