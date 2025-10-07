<template>
  <a-form :model="passwordForm" :label-col="{ span: 4 }" :wrapper-col="{ span: 16 }">
    <a-form-item label="旧密码">
      <a-input-password v-model:value="passwordForm.oldPassword" />
    </a-form-item>
    <a-form-item label="新密码">
      <a-input-password v-model:value="passwordForm.newPassword" />
    </a-form-item>
    <a-form-item label="确认新密码">
      <a-input-password v-model:value="passwordForm.confirmPassword" />
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
    <a-form-item label="验证码">
      <div class="flex items-center gap-3">
        <a-input
          v-model:value="passwordForm.captchaInput"
          placeholder="输入验证码"
          :maxlength="4"
        />
        <CaptchaCanvas ref="captcha" />
      </div>
    </a-form-item>
    <a-form-item :wrapper-col="{ offset: 4, span: 16 }" style="text-align: center;">
      <a-button type="primary" @click="changePassword">修改密码</a-button>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import {
  Form as AForm,
  FormItem as AFormItem,
  Input as AInput,
  InputPassword as AInputPassword,
  Button as AButton,
  message,
  Progress as AProgress
} from 'ant-design-vue'
import CaptchaCanvas from '@/components/common/CaptchaCanvas.vue'

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
  captchaInput: ''
})

const captcha = ref<InstanceType<typeof CaptchaCanvas> | null>(null)

// 密码强度计算
const passwordStrength = computed(() => {
  const password = passwordForm.value.newPassword
  let strength = 0
  let text = '弱'
  let status: 'normal' | 'active' | 'success' | 'exception' = 'exception'

  if (password.length >= 8) strength += 25
  if (/[a-z]/.test(password)) strength += 25
  if (/[A-Z]/.test(password)) strength += 25
  if (/[0-9]/.test(password)) strength += 25

  if (strength >= 75) {
    text = '强'
    status = 'success'
  } else if (strength >= 50) {
    text = '中'
    status = 'active'
  } else if (strength > 0) {
    text = '弱'
    status = 'normal'
  }

  return { percent: strength, text, status }
})

// 监听密码变化，自动验证确认密码
watch(() => passwordForm.value.newPassword, () => {
  if (passwordForm.value.confirmPassword &&
    passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    message.warning('两次输入的新密码不一致')
  }
})

const changePassword = () => {
  if (!passwordForm.value.oldPassword) {
    message.error('请输入旧密码')
    return
  }

  if (!passwordForm.value.newPassword) {
    message.error('请输入新密码')
    return
  }

  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    message.error('两次输入的新密码不一致')
    return
  }

  // 检查密码强度
  if (passwordStrength.value.percent < 50) {
    message.warning('密码强度较弱，建议使用更复杂的密码')
  }

  if (!captcha.value || !captcha.value.validate(passwordForm.value.captchaInput)) {
    message.error('验证码不正确')
    return
  }

  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
    captchaInput: ''
  }

  message.success('修改成功')
}
</script>

<style scoped>
.password-strength {
  width: 100%;
}

.strength-text {
  margin-top: 5px;
  font-size: 12px;
  color: #666;
}
</style>
