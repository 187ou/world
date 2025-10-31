<template>
  <a-form :model="passwordForm" :label-col="{ span: 4 }" :wrapper-col="{ span: 16 }">
    <a-form-item label="旧密码">
      <a-input-password v-model:value="passwordForm.oldPassword" />
    </a-form-item>

    <a-form-item label="新密码">
      <a-input-password
        v-model:value="passwordForm.newPassword"
        @blur="validateNewPassword"
      />
      <div v-if="errors.newPassword" class="text-red-500 text-sm">{{ errors.newPassword }}</div>
    </a-form-item>

    <a-form-item label="确认新密码">
      <a-input-password
        v-model:value="passwordForm.confirmPassword"
        @blur="validateConfirmPassword"
      />
      <div v-if="errors.confirmPassword" class="text-red-500 text-sm">{{ errors.confirmPassword }}</div>
    </a-form-item>

    <a-form-item label="密码强度">
      <div class="password-strength">
        <a-progress
          :percent="passwordStrength.percent"
          :status="passwordStrength.status"
          :show-info="false"
        />
        <div class="strength-text">{{ passwordStrength.text }}</div>
      </div>
    </a-form-item>

    <a-form-item label="邮箱">
      <FormInput
        v-model="passwordForm.email"
        type="email"
        placeholder="Email"
      >
        <template #captcha>
          <VerificationCode
            :mode="2"
            :email="passwordForm.email"
          />
        </template>
      </FormInput>
    </a-form-item>

    <a-form-item label="验证码">
      <FormInput
        v-model="passwordForm.code"
        type="text"
        placeholder="Code"
      />
    </a-form-item>

    <a-form-item :wrapper-col="{ offset: 4, span: 16 }" style="text-align: center;">
      <a-button type="primary" @click="changePassword">修改密码</a-button>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { ref, computed, watch, reactive } from 'vue'
import {
  Form as AForm,
  FormItem as AFormItem,
  InputPassword as AInputPassword,
  Button as AButton,
  message,
  Progress as AProgress
} from 'ant-design-vue'
import CaptchaCanvas from '@/components/common/CaptchaCanvas.vue'
import { formFilters } from '@/filters/formFilters'
import FormInput from '@/components/logins/FromInput.vue'
import VerificationCode from '@/components/logins/VerificationCode.vue'
import { updatePassword } from '@/apis'
import type { ResetPasswordDto } from '@/types/resetPasswordDto.ts'

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
  captchaInput: '',
  email: '',
  code: ''
})

const errors = reactive({
  newPassword: '',
  confirmPassword: '',
  email: ''
})

const captcha = ref<InstanceType<typeof CaptchaCanvas> | null>(null)

const passwordStrength = computed(() => {
  return formFilters.calculatePasswordStrength(passwordForm.value.newPassword)
})

const validateNewPassword = () => {
  const result = formFilters.validatePassword(passwordForm.value.newPassword)
  errors.newPassword = result.message
  return result.isValid
}

const validateConfirmPassword = () => {
  const result = formFilters.validateConfirmPassword(
    passwordForm.value.newPassword,
    passwordForm.value.confirmPassword
  )
  errors.confirmPassword = result.message
  return result.isValid
}

// 添加邮箱验证函数
const validateEmail = () => {
  const result = formFilters.validateEmail(passwordForm.value.email)
  errors.email = result.message
  return result.isValid
}

// 修改 changePassword
const changePassword = async () => {
  if (!passwordForm.value.oldPassword) {
    message.error('请输入旧密码')
    return
  }

  // 验证邮箱
  const isEmailValid = validateEmail()

  // 验证新密码
  const isNewPasswordValid = validateNewPassword()
  const isConfirmPasswordValid = validateConfirmPassword()

  if (!isEmailValid || !isNewPasswordValid || !isConfirmPasswordValid) {
    message.error('请检查表单填写内容')
    return
  }

  // 检查密码强度
  if (passwordStrength.value.percent < 10) {
    message.warning('密码强度较弱，建议使用更复杂的密码')
  }

  // 准备重置密码的数据
  const resetData: ResetPasswordDto = {
    email: passwordForm.value.email,
    code: passwordForm.value.code,
    newPassword: passwordForm.value.newPassword,
    confirmPassword: passwordForm.value.confirmPassword,
    oldPassword: passwordForm.value.oldPassword
  }

  try {
    const response = await updatePassword(resetData)
    if (response) {
      message.success('密码修改成功')
      passwordForm.value = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
        captchaInput: '',
        email: '',
        code: ''
      }
    } else {
      message.error('密码修改失败')
    }
  } catch (error) {
    console.error(error)
    message.error('密码修改失败，请稍后重试')
  }
}

watch(() => passwordForm.value.newPassword, () => {
  validateNewPassword()
  validateConfirmPassword()
})
</script>

<style scoped lang="scss">
.password-strength {
  width: 100%;
}

.strength-text {
  margin-top: 5px;
  font-size: 12px;
  color: #666;
}
</style>
