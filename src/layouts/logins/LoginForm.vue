<template>
  <FormContainer title="Man!" description="What can I say. &nbsp;See you again.">
    <FormInput
      label="邮箱"
      v-model="form.email"
      type="email"
      placeholder="Email"
    />

    <FormInput
      label="密码"
      v-model="form.password"
      type="password"
      placeholder="Password"
      showPasswordToggle
    />

    <FormInput
      label="是真人吗？"
      v-model="form.captchaInput"
      type="captcha"
      placeholder="Man"
      :maxlength="4"
    >
      <template #captcha>
        <CaptchaCanvas ref="captcha" />
      </template>
    </FormInput>

    <div class="flex justify-between items-center">
      <FormLink linkClass="text-sm text-gray-500" @click="$emit('switchMode','forgot')">
        忘记密码?
      </FormLink>
      <FormButton @click="handleLogin">登录</FormButton>
    </div>

    <p class="mt-10 text-sm text-center">
      没有账户?
      <FormLink linkClass="text-[#8c7569]" @click="$emit('switchMode','register')">
        现在注册
      </FormLink>
    </p>
  </FormContainer>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import CaptchaCanvas from '@/components/common/CaptchaCanvas.vue'
import { formFilters } from '@/filters/formFilters'
import FormContainer from '@/components/logins/FromContainer.vue'
import FormInput from '@/components/logins/FromInput.vue'
import FormButton from '@/components/logins/FromButton.vue'
import FormLink from '@/components/logins/FromLink.vue'

const form = ref({
  email: '',
  password: '',
  captchaInput: ''
})

const captcha = ref<InstanceType<typeof CaptchaCanvas> | null>(null)

const handleLogin = () => {
  // 邮箱验证
  if (!form.value.email) return alert('请输入邮箱')
  if (!formFilters.validateEmail(form.value.email)) return alert('邮箱格式不正确')

  // 密码验证
  if (!form.value.password) return alert('请输入密码')

  // 验证码验证
  if (!form.value.captchaInput) return alert('请输入验证码')
  if (!captcha.value || !captcha.value.validate(form.value.captchaInput)) {
    alert('验证码不正确')
    // 刷新验证码
    if (captcha.value) {
      captcha.value.refreshCaptcha()
    }
    return
  }

  console.log('登录参数：', form.value)
  alert('登录成功！')

  // 清空表单并刷新验证码
  form.value = { email: '', password: '', captchaInput: '' }
  if (captcha.value) {
    captcha.value.refreshCaptcha()
  }

  emit('success')
}

const emit = defineEmits(['success','switchMode'])
</script>
