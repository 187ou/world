import axios from 'axios'
import type { AxiosInstance, AxiosResponse } from 'axios'
// import { useUserStore } from '@/stores/user.ts'

const http: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  }
})

// 请求拦截器
http.interceptors.request.use(
  // (config) => {
  //   const userStore = useUserStore()
  //   if (userStore.token && config.headers) {
  //     config.headers.Authorization = `Bearer ${userStore.token}`
  //   }
  //   return config
  // },
  // (error) => {
  //   return Promise.reject(error)
  // }
)

// 响应拦截器
http.interceptors.response.use(
  (response: AxiosResponse) => {
    return response.data
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

    return Promise.reject(new Error(message))
  }
)

export default http
