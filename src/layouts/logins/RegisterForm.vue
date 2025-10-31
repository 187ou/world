<template>
  <FormContainer title="Man" description="Create your account.">
    <FormInput
      label="用户名"
      v-model="form.username"
      type="text"
      placeholder="Username"
      :error="errors.username"
      @blur="validateField('username')"
    />

    <FormInput
      label="密码"
      v-model="form.password"
      type="password"
      placeholder="Password"
      :error="errors.password"
      @blur="validateField('password')"
    />

    <FormInput
      label="确认密码"
      v-model="form.confirmPassword"
      type="password"
      placeholder="Confirm Password"
      :error="errors.confirmPassword"
      @blur="validateField('confirmPassword')"
    />

    <FormInput
      label="邮箱"
      v-model="form.email"
      type="captcha"
      placeholder="Email"
      :error="errors.email"
      @blur="validateField('email')"
    >
      <template #captcha>
        <VerificationCode
          :mode="1"
          :email="form.email"
          @send-success="onCodeSendSuccess"
          @send-error="onCodeSendError"
        />
      </template>
    </FormInput>

    <FormInput
      label="验证码"
      v-model="form.code"
      type="text"
      placeholder="Code"
    />

    <div class="flex justify-between items-center">
      <FormButton @click="handleRegister">注册</FormButton>
    </div>

    <p class="mt-10 text-sm text-center">
      已有账户?
      <FormLink linkClass="text-[#8c7569]" @click="$emit('switchMode','login')">
        立即登录
      </FormLink>
    </p>
  </FormContainer>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { formFilters } from '@/filters/formFilters'
import FormContainer from '@/components/logins/FromContainer.vue'
import FormInput from '@/components/logins/FromInput.vue'
import FormButton from '@/components/logins/FromButton.vue'
import FormLink from '@/components/logins/FromLink.vue'
import VerificationCode from '@/components/logins/VerificationCode.vue'
import { register } from '@/apis'
import type { UserRegisterDto } from '@/types/userRegisterDto.ts'
import { message } from 'ant-design-vue'

const emit = defineEmits(['switchMode'])

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: '',
  code: ''
})

const errors = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: '',
  code: ''
})

// 移除了 isSendingCode，因为现在由 VerificationCode 组件管理

const validateField = (field: keyof typeof form) => {
  switch (field) {
    case 'username':
      const usernameResult = formFilters.validateUsername(form.username)
      errors.username = usernameResult.message
      break
    case 'password':
      const passwordResult = formFilters.validatePassword(form.password)
      errors.password = passwordResult.message
      break
    case 'confirmPassword':
      const confirmResult = formFilters.validateConfirmPassword(form.password, form.confirmPassword)
      errors.confirmPassword = confirmResult.message
      break
    case 'email':
      const emailResult = formFilters.validateEmail(form.email)
      errors.email = emailResult.message
      break
    case 'phone':
      const phoneResult = formFilters.validatePhone(form.phone)
      errors.phone = phoneResult.message
      break
  }
}

const validateAll = () => {
  validateField('username')
  validateField('password')
  validateField('confirmPassword')
  validateField('email')

  return !errors.username && !errors.password && !errors.confirmPassword && !errors.email
}

const onCodeSendSuccess = () => {
}

const onCodeSendError = (error: unknown) => {
  console.error('验证码发送失败:', error)
}

const handleRegister = async () => {
  if (!validateAll()) {
    return
  }

  try {
    const registerData: UserRegisterDto = {
      userName: form.username,
      password: form.password,
      confirmPassword: form.confirmPassword,
      code: form.code,
      email: form.email,
      nickName: '',
      phone: '',
      sex: 0
    }

    const result = await register(registerData)
    console.log('注册结果：', result)
    message.success('注册成功！')
    Object.assign(form, { username: '', password: '', confirmPassword: '', email: '', code: '' })
    emit('switchMode', 'login')
  } catch (error) {
    console.error('注册失败：', error)
    message.error('注册失败，请重试')
  }
}
</script>
