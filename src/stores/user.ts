import { defineStore } from 'pinia'
import { login, logout } from '@/apis/auth'
import type { UserInfo, LoginPayload, LogoutPayload } from '@/apis/auth'

interface UserState {
  token: string
  user: UserInfo | null
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: localStorage.getItem('token') || '',
    user: null,
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    userId: (state) => state.user?.id,
  },

  actions: {
    // 登录
    async login(payload: LoginPayload) {
      try {
        const res = await login(payload)
        console.log('登录成功:', res)
        if (res.data?.tokens?.accessToken) {
          this.token = res.data.tokens.accessToken
          localStorage.setItem('token', this.token)

          this.user = {
            id: res.data.user.userId,
            name: res.data.user.userName,
            email: res.data.user.email,
            level: res.data.user.level,
            money: res.data.user.money,
            nickname: res.data.user.nickName || '',
            sex: res.data.user.sex,
            phone: res.data.user.phone || '',
          }

          return true
        }
        return false
      } catch (error) {
        console.error('登录失败:', error)
        return false
      }
    },

    // 退出
    async logout() {
      try {
        if (this.token) {
          const payload: LogoutPayload = { refreshToken: this.token }
          await logout(payload)
        }
      } catch (error) {
        console.error('退出登录失败:', error)
      } finally {
        this.token = ''
        this.user = null
        localStorage.removeItem('token')
      }
    },

    // 获取用户信息
    // async fetchUserInfo() {
    //   try {
    //     const res = await fetchUserInfo()
    //     if (res.data) {
    //       this.user = res.data
    //       return res.data
    //     }
    //     return null
    //   } catch (error) {
    //     console.error('获取用户信息失败:', error)
    //     return null
    //   }
    // },

    // 初始化
    // async init() {
    //   if (this.token) {
    //     const userInfo = await this.fetchUserInfo()
    //     if (!userInfo) {
    //       await this.logout()
    //     }
    //   }
    // }
  }
})
