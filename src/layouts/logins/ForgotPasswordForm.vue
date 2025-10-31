<template>
  <FormContainer title="重置密码" description="请输入邮箱和新密码。">
    <FormInput
      label="邮箱"
      v-model="form.email"
      type="email"
      placeholder="Email"
    >
      <template #captcha>
        <VerificationCode
          :mode="0"
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

    <FormInput
      label="新密码"
      v-model="form.password"
      type="password"
      placeholder="New Password"
    />

    <FormInput
      label="确认密码"
      v-model="form.confirmPassword"
      type="password"
      placeholder="Password again"
    />

    <div class="flex justify-between items-center">
      <FormLink linkClass="text-sm text-gray-500" @click="$emit('switchMode','login')">
        返回登录
      </FormLink>
      <FormButton @click="handleForgotPassword">确认修改</FormButton>
    </div>
  </FormContainer>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { formFilters } from '@/filters/formFilters'
import FormContainer from '@/components/logins/FromContainer.vue'
import FormInput from '@/components/logins/FromInput.vue'
import FormButton from '@/components/logins/FromButton.vue'
import FormLink from '@/components/logins/FromLink.vue'
import VerificationCode from '@/components/logins/VerificationCode.vue'
import { message } from 'ant-design-vue'
import { resetPassword } from '@/apis'

const form = ref({
  email: '',
  password: '',
  confirmPassword: '',
  code: ''
})

const handleForgotPassword = async () => {
  if (!form.value.email) return message.error('请输入邮箱')
  if (!formFilters.validateEmail(form.value.email)) return message.error('邮箱格式不正确')

  if (!form.value.code) return message.error('请输入验证码')

  if (!form.value.password) return message.error('请输入新密码')
  const passwordValidation = formFilters.validatePassword(form.value.password)
  if (!passwordValidation.isValid) return message.error(passwordValidation.message)

  if (form.value.password !== form.value.confirmPassword) return message.error('两次输入的密码不一致')

  try {
    const res = await resetPassword({
      email: form.value.email,
      code: form.value.code,
      newPassword: form.value.password,
      confirmPassword: form.value.confirmPassword
    })

    console.log('重置密码结果：', res)
    message.success('密码修改成功！')
    form.value = { email: '', password: '', confirmPassword: '', code: '' }
    emit('switchMode', 'login')
  } catch (error) {
    console.error('重置密码失败:', error)
    message.error('密码修改失败，请重试')
  }
}

const onCodeSendSuccess = () => {
}

const onCodeSendError = (error: unknown) => {
  console.error('验证码发送失败:', error)
}
const emit = defineEmits(['switchMode'])
</script>
