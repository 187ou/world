import http from '@/utils/http.ts';
import type { UserRegisterDto } from '@/types/userRegisterDto.ts'
import type { ResetPasswordDto } from '@/types/resetPasswordDto.ts'
import type { UserUpdateDto } from '@/types/userUpdateDto.ts'
import type { SetPasswordDto } from '@/types/setPasswordDto'

/**
 * 用户注册
 * @param userRegisterDto 用户注册信息对象
 * @returns 注册结果数据（包含code、message、data，其中data是RegisterVo结构）
 */
export const register = async (userRegisterDto: UserRegisterDto) => {
  return await http.post('/api/auth/register', userRegisterDto);
}

/**
 * 重置密码
 * @param setPasswordDto 重置密码信息对象，包含邮箱、验证码、新密码和确认密码
 * @returns 重置结果数据（包含code、message、data）
 */
export const resetPassword = async (setPasswordDto: SetPasswordDto) => {
  return await http.post('/api/auth/reset-password', setPasswordDto);
};

/**
 * 发送注册验证码
 * @param email
 * @returns 发送结果数据（包含code、message、data，其中data是{message}结构）
 */
export const registerCode = async (email: string) => {
  return await http.post('/api/auth/send-register-code', { email });
}

/**
 * 发送重置密码验证码
 * @param sendCodeDto 包含邮箱信息的对象
 * @returns 发送结果数据（包含code、message、data，其中data是{email, expiresIn}结构）
 */
export const sendVerificationCode = async (sendCodeDto: { email: string }) => {
  return await http.post('/api/auth/send-verification-code', sendCodeDto);
};

/**
 * 发送邮箱换绑验证码
 * @param emailBindCodeDto 包含新邮箱和用户ID的对象
 * @returns 发送结果数据（包含code、message、data，其中data是{email, expiresIn}结构）
 */
export const sendEmailBindCode = async (emailBindCodeDto: { newEmail: string; userId: number }) => {
  return await http.post('/api/auth/send-email-bind-code', emailBindCodeDto);
};

/**
 * 更新用户信息
 * @param userUpdateDto 用户更新信息对象，包含邮箱、验证码、昵称、手机号、性别和用户ID
 * @returns 更新后的用户信息数据（包含code、message、data，其中data是UserVo结构）
 */
export const updateUserInfo = async (userUpdateDto: UserUpdateDto) => {
  return await http.put('/api/auth/update-info', userUpdateDto);
};

/**
 * 更新用户邮箱
 * @param updateEmailDto 包含新邮箱和验证码的对象
 * @returns 更新结果数据
 */
export const updateEmail = async (updateEmailDto: { newEmail: string; code: string }) => {
  return await http.put('/api/auth/update-email', updateEmailDto);
};

/**
 * 获取当前登录用户的信息
 * @returns 当前用户信息数据（包含code、message、data，其中data是UserVo结构）
 */
export const getCurrentUserInfo = async () => {
  return await http.get('/api/auth/info');
};

/**
 * 发送修改密码验证码（登录状态）
 * @param sendCodeDto 包含邮箱信息的对象
 * @returns 发送结果数据
 */
export const sendUpdatePwdCode = async (sendCodeDto: { email: string }) => {
  return await http.post('/api/auth/send-verify-code', sendCodeDto);
};

/**
 * 登录状态下修改密码
 * @param updatePasswordDto 包含邮箱、旧密码、新密码、确认密码和验证码的对象
 * @returns 修改结果数据
 */
export const updatePassword = async (updatePasswordDto: ResetPasswordDto) => {
  return await http.post('/api/auth/update-password', updatePasswordDto);
};
