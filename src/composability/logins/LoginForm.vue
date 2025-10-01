<template>
  <div>
    <h1 class="text-[26px] font-normal text-[#55311c]">Man!</h1>
    <p class="mt-2 mb-7 text-gray-700">What can I say. &nbsp;See you again.</p>

    <div class="form-control mb-4">
      <label class="label text-xs font-semibold uppercase">邮箱</label>
      <input v-model="form.email" type="email" placeholder="Email" class="input input-bordered w-full" />
    </div>

    <div class="form-control mb-4">
      <label class="label text-xs font-semibold uppercase">密码</label>
      <div class="relative">
        <input
          v-model="form.password"
          :type="passwordVisible ? 'text' : 'password'"
          placeholder="Password"
          class="input input-bordered w-full pr-10"
        />
        <button
          type="button"
          @click="togglePasswordVisibility"
          class="absolute inset-y-0 right-0 pr-3 flex items-center"
        >
          {{ passwordVisible ? '🙈' : '🐵' }}
        </button>
      </div>
    </div>

    <div class="form-control mb-4">
      <label class="label text-xs font-semibold uppercase">是真人吗？</label>
      <div class="flex items-center gap-3">
        <input
          v-model="form.captchaInput"
          type="text"
          placeholder="输入验证码"
          class="input input-bordered w-full"
          maxlength="4"
        />
        <CaptchaCanvas ref="captcha" />
      </div>
    </div>

    <div class="flex justify-between items-center">
      <a href="#" class="text-sm text-gray-500" @click.prevent="$emit('switchMode','forgot')">忘记密码?</a>
      <button class="btn text-white" @click="handleLogin">登录</button>
    </div>

    <p class="mt-10 text-sm text-center">
      没有账户?
      <a href="#" class="text-[#8c7569]" @click.prevent="$emit('switchMode','register')">现在注册</a>
    </p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import CaptchaCanvas from '@/components/common/CaptchaCanvas.vue'

const form = ref({
  email: '',
  password: '',
  captchaInput: ''
})

const passwordVisible = ref(false)

const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value
}

const handleLogin = () => {
  console.log('登录参数：', form.value)
  alert('登录成功！')
  form.value = { email: '', password: '', captchaInput: '' }
  emit('success')
}

const emit = defineEmits(['success','switchMode'])
</script>
