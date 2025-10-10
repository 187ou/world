import http from '@/utils/http'

export interface UserInfo {
  id: number
  name: string
  email: string
}

export interface LoginPayload {
  email: string
  password: string
}

export interface LoginResponse {
  token: string
}

// 登录接口
export async function login(payload: LoginPayload) {
  return http.post<{ token: string }>('/auth/login', payload)
}

// 获取用户信息接口
export async function fetchUserInfo() {
  return http.get<UserInfo>('/auth/me')
}
