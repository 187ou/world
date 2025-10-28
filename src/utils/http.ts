import axios from 'axios'
import type { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import { useUserStore } from '@/stores/user'

const http: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  }
})

const NO_TOKEN_PATHS = [
  '/api/auth/login',
  '/api/auth/register',
  '/api/auth/send-register-code',
  '/api/auth/send-verification-code',
  '/api/auth/reset-password'
]

// 请求拦截器
http.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const requiresToken = !NO_TOKEN_PATHS.some(path => config.url?.includes(path))

    if (requiresToken) {
      const userStore = useUserStore()
      if (userStore.token && config.headers) {
        config.headers.Authorization = `Bearer ${userStore.token}`
      }
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
http.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, msg } = response.data

    if (![1, 0, 20000].includes(code)) {
      return Promise.reject(new Error(msg || '请求失败'))
    }

    return response.data
  },
  (error) => {
    const status = error.response?.status || 500

    const statusMessages: Record<number, string> = {
      400: '请求参数错误',
      401: '未授权或登录过期',
      403: '没有权限访问',
      404: '请求地址不存在',
      500: '服务器内部错误'
    }

    return Promise.reject(new Error(statusMessages[status] || `未知错误 (${status})`))
  }
)

export default http
