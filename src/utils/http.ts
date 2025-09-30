// src/utils/http.ts
import axios from 'axios'
import type { AxiosInstance, AxiosResponse } from 'axios'
import { useUserStore } from '@/stores/user.ts'

// 定义统一返回类型
export interface HttpResult<T = unknown> {
  code: number
  data?: T
  message: string
  success: boolean
}

// 创建 axios 实例
const http: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:3000/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  }
})

// 请求拦截器
http.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    if (userStore.token && config.headers) {
      config.headers.Authorization = `Bearer ${userStore.token}`
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
    const result: HttpResult = {
      code: 0,
      data: response.data,
      message: 'ok',
      success: true
    }
    response.data = result
    return response
  },
  (error) => {
    const status = error.response?.status || 500
    let message = '服务器错误'

    switch (status) {
      case 400:
        message = '请求参数错误'
        break
      case 401:
        message = '未授权或登录过期'
        // localStorage.removeItem('token')
        // window.location.href = '/login'
        break
      case 403:
        message = '没有权限访问'
        break
      case 404:
        message = '请求地址不存在'
        break
      case 500:
        message = '服务器内部错误'
        break
    }

    const result: HttpResult = {
      code: status,
      data: null,
      message,
      success: false
    }

    // 同样地，把错误也标准化成相同的格式
    return Promise.resolve({
      ...error.response,
      data: result
    } as AxiosResponse<HttpResult>)
  }
)

export default http
