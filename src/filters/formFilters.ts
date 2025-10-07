export const formFilters = {
  // 邮箱验证
  validateEmail(email: string): { isValid: boolean; message: string } {
    if (!email) {
      return { isValid: false, message: '邮箱不能为空' }
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(email)) {
      return { isValid: false, message: '邮箱格式不正确' }
    }

    return { isValid: true, message: '' }
  },

  // 用户名验证
  validateUsername(username: string): { isValid: boolean; message: string } {
    const trimmed = username.trim()

    if (trimmed.length < 3) {
      return { isValid: false, message: '用户名至少3个字符' }
    }

    if (trimmed.length > 20) {
      return { isValid: false, message: '用户名不能超过20个字符' }
    }

    // 检查是否包含非法字符
    if (!/^[a-zA-Z0-9_\u4e00-\u9fa5]+$/.test(trimmed)) {
      return { isValid: false, message: '用户名只能包含字母、数字、下划线和中文' }
    }

    return { isValid: true, message: '' }
  },

  // 密码验证
  validatePassword(password: string): { isValid: boolean; message: string } {
    if (password.length < 6) {
      return { isValid: false, message: '密码至少6位字符' }
    }

    if (password.length > 30) {
      return { isValid: false, message: '密码不能超过30个字符' }
    }

    if (!/[A-Za-z]/.test(password)) {
      return { isValid: false, message: '密码必须包含字母' }
    }

    if (!/\d/.test(password)) {
      return { isValid: false, message: '密码必须包含数字' }
    }

    return { isValid: true, message: '' }
  },

  // 确认密码验证
  validateConfirmPassword(password: string, confirmPassword: string): { isValid: boolean; message: string } {
    if (password !== confirmPassword) {
      return { isValid: false, message: '两次输入的密码不一致' }
    }

    return { isValid: true, message: '' }
  },

  // 手机号验证
  validatePhone(phone: string): { isValid: boolean; message: string } {
    if (!phone) {
      return { isValid: false, message: '手机号不能为空' }
    }

    const phoneRegex = /^1[3-9]\d{9}$/
    if (!phoneRegex.test(phone)) {
      return { isValid: false, message: '手机号格式不正确' }
    }

    return { isValid: true, message: '' }
  },

  // 密码强度验证
  calculatePasswordStrength(password: string): {
    percent: number;
    text: string;
    status: 'normal' | 'active' | 'success' | 'exception';
  } {
    let strength = 0;
    let text = '弱';
    let status: 'normal' | 'active' | 'success' | 'exception' = 'exception';

    if (!password) {
      return { percent: 0, text: '弱', status: 'exception' };
    }

    // 长度检查
    if (password.length >= 8) strength += 25;
    // 小写字母检查
    if (/[a-z]/.test(password)) strength += 25;
    // 大写字母检查
    if (/[A-Z]/.test(password)) strength += 25;
    // 数字检查
    if (/[0-9]/.test(password)) strength += 25;

    // 根据强度设置文本和状态
    if (strength >= 75) {
      text = '强';
      status = 'success';
    } else if (strength >= 50) {
      text = '中';
      status = 'active';
    } else if (strength > 0) {
      text = '弱';
      status = 'normal';
    }

    return { percent: strength, text, status };
  },
}
