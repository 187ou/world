import http from '@/utils/http'

export interface UserInfo {
  id: number
  name: string
  email: string
  level: number
  money: number
  nickname: string
  sex: number
  phone: string
}

export interface LoginPayload {
  username: string
  password: string
}

export interface LogoutPayload {
  refreshToken: string
}

// 登录接口
export async function login(payload: LoginPayload) {
  return http.post('/api/auth/login', payload)
}

// 退出登录接口
export async function logout(payload: LogoutPayload) {
  return http.post('/api/auth/logout', payload)
}
