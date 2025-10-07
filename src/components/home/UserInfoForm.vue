<template>
  <a-form :model="userInfo" :label-col="{ span: 4 }" :wrapper-col="{ span: 16 }">
    <a-form-item label="用户名">
      <a-input
        v-model:value="userInfo.username"
        @blur="validateUsername"
      />
      <div v-if="errors.username" class="text-red-500 text-sm">{{ errors.username }}</div>
    </a-form-item>

    <a-form-item label="昵称">
      <a-input v-model:value="userInfo.nickname" />
    </a-form-item>

    <a-form-item label="邮箱">
      <a-input
        v-model:value="userInfo.email"
        @blur="validateEmail"
      />
      <div v-if="errors.email" class="text-red-500 text-sm">{{ errors.email }}</div>
    </a-form-item>

    <a-form-item label="性别">
      <a-select v-model:value="userInfo.gender" placeholder="请选择性别">
        <a-select-option value="male">男</a-select-option>
        <a-select-option value="female">女</a-select-option>
        <a-select-option value="other">其他</a-select-option>
      </a-select>
    </a-form-item>

    <a-form-item label="生日">
      <a-date-picker
        v-model:value="userInfo.birthday"
        style="width: 100%"
        placeholder="请选择生日"
      />
    </a-form-item>

    <a-form-item label="手机号">
      <a-input
        v-model:value="userInfo.phone"
        @blur="validatePhone"
      />
      <div v-if="errors.phone" class="text-red-500 text-sm">{{ errors.phone }}</div>
    </a-form-item>

    <a-form-item label="个人简介">
      <a-textarea
        v-model:value="userInfo.bio"
        :rows="4"
        placeholder="请输入个人简介"
      />
    </a-form-item>

    <a-form-item :wrapper-col="{ offset: 4, span: 16 }" style="text-align: center;">
      <a-button type="primary" @click="updateUserInfo">保存</a-button>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import {
  Form as AForm,
  FormItem as AFormItem,
  Input as AInput,
  Button as AButton,
  Select as ASelect,
  SelectOption as ASelectOption,
  DatePicker as ADatePicker,
  Textarea as ATextarea,
  message
} from 'ant-design-vue'
import dayjs from 'dayjs'
import { formFilters } from '@/filters/formFilters.ts'

const userInfo = ref({
  username: 'User',
  nickname: 'Nickname',
  email: 'user@example.com',
  password: 'man123',
  gender: 'male',
  birthday: dayjs('1990-01-01'),
  phone: '13800138000',
  bio: 'Man',
})

const errors = reactive({
  username: '',
  email: '',
  phone: ''
})

const validateUsername = () => {
  const result = formFilters.validateUsername(userInfo.value.username)
  errors.username = result.message
  return result.isValid
}

const validateEmail = () => {
  const result = formFilters.validateEmail(userInfo.value.email)
  errors.email = result.message
  return result.isValid
}

const validatePhone = () => {
  const result = formFilters.validatePhone(userInfo.value.phone)
  errors.phone = result.message
  return result.isValid
}

const updateUserInfo = () => {
  // 验证所有必填字段
  const isUsernameValid = validateUsername()
  const isEmailValid = validateEmail()
  const isPhoneValid = validatePhone()

  if (!isUsernameValid || !isEmailValid || !isPhoneValid) {
    message.error('请检查表单填写内容')
    return
  }

  alert('更新用户信息成功')
  console.log('更新用户信息', userInfo.value)
}
</script>

