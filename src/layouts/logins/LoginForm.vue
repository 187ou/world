<template>
  <FormContainer title="Man!" description="What can I say. &nbsp;See you again.">
    <FormInput
      label="用户名"
      v-model="form.username"
      type="text"
      placeholder="Username"
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
import { useUserStore } from '@/stores/user'
import CaptchaCanvas from '@/components/common/CaptchaCanvas.vue'
import FormContainer from '@/components/logins/FromContainer.vue'
import FormInput from '@/components/logins/FromInput.vue'
import FormButton from '@/components/logins/FromButton.vue'
import FormLink from '@/components/logins/FromLink.vue'
import { message } from 'ant-design-vue'
import userRouter from '@/router'

const form = ref({
  // email: '',
  password: '',
  captchaInput: '',
  username: ''
})

const captcha = ref<InstanceType<typeof CaptchaCanvas> | null>(null)
const userStore = useUserStore()
const router = userRouter

const handleLogin = async () => {
  // 邮箱验证
  // if (!form.value.email) return alert('请输入邮箱')
  // if (!/\S+@\S+\.\S+/.test(form.value.email)) return alert('邮箱格式不正确')

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

  try {
    const success = await userStore.login({
      username: form.value.username,
      password: form.value.password
    })

    if (success) {
      console.log(userStore.token)
      console.log(userStore.user)
      message.success('登录成功！')

      form.value = { username: '', password: '', captchaInput: '' }
      if (captcha.value) {
        captcha.value.refreshCaptcha()
      }
      router.push('/home')
      emit('success')
    } else {
      alert('登录失败，请检查用户名和密码')
    }
  } catch (error) {
    console.error('登录错误:', error)
    alert('登录过程中发生错误')
  }
}

const emit = defineEmits(['success','switchMode'])
</script>

