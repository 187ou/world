<template>
  <FormContainer title="重置密码" description="请输入邮箱和新密码。">
    <FormInput
      label="邮箱"
      v-model="form.email"
      type="email"
      placeholder="Email"
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

const form = ref({
  email: '',
  password: '',
  confirmPassword: ''
})

const handleForgotPassword = () => {
  if (!form.value.email) return alert('请输入邮箱')
  if (!formFilters.validateEmail(form.value.email)) return alert('邮箱格式不正确')

  if (!form.value.password) return alert('请输入新密码')
  const passwordValidation = formFilters.validatePassword(form.value.password)
  if (!passwordValidation.isValid) return alert(passwordValidation.message)

  if (form.value.password !== form.value.confirmPassword) return alert('两次输入的密码不一致')

  console.log('重置密码参数：', form.value)
  alert('密码修改成功！')
  form.value = { email: '', password: '', confirmPassword: '' }
  emit('switchMode','login')
}

const emit = defineEmits(['switchMode'])
</script>
