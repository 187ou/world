import { defineStore } from 'pinia'
import { login, fetchUserInfo } from '@/apis/auth'
import type { UserInfo, LoginPayload } from '@/apis/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: null as UserInfo | null,
  }),
  actions: {
    // 登录
    async login(payload: LoginPayload) {
      try {
        const res = await login(payload)
        if (res && res?.data) {
          this.token = res.data.token
          localStorage.setItem('token', this.token)
          return true
        }
        return false
      } catch (error) {
        console.error('登录失败:', error)
        return false
      }
    },

    // 退出
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('token')
    },

    // 获取用户信息
    async fetchUserInfo() {
      try {
        const res = await fetchUserInfo()
        if (res && res?.data) {
          this.user = res.data
          return res.data
        }
        return null
      } catch (error) {
        console.error('获取用户信息失败:', error)
        return null
      }
    }
  }
})
