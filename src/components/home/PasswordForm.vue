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
import { ref } from 'vue'
import {
  Form as AForm,
  FormItem as AFormItem,
  Input as AInput,
  InputPassword as AInputPassword,
  Button as AButton,
  message
} from 'ant-design-vue'
import CaptchaCanvas from '@/components/logins/CaptchaCanvas.vue'

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
  captchaInput: ''
})

const captcha = ref<InstanceType<typeof CaptchaCanvas> | null>(null)

const changePassword = () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    message.error('两次输入的新密码不一致')
    return
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
