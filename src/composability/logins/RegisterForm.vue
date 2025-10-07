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

const emit = defineEmits(['switchMode'])

const form = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

const errors = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

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
  }
}

const validateAll = () => {
  validateField('username')
  validateField('password')
  validateField('confirmPassword')

  return !errors.username && !errors.password && !errors.confirmPassword
}

const handleRegister = () => {
  if (!validateAll()) {
    return
  }

  console.log('注册参数：', form)
  alert('注册成功！')
  Object.assign(form, { username: '', password: '', confirmPassword: '' })
  emit('switchMode', 'login')
}
</script>
