<template>
  <a-form :model="userInfo" :label-col="{ span: 4 }" :wrapper-col="{ span: 16 }">
    <a-form-item label="用户名">
      <a-input v-model:value="userInfo.username" />
    </a-form-item>
    <a-form-item label="昵称">
      <a-input v-model:value="userInfo.nickname" />
    </a-form-item>
    <a-form-item label="邮箱">
      <a-input v-model:value="userInfo.email" disabled />
    </a-form-item>
    <a-form-item label="密码">
      <a-input-password v-model:value="userInfo.password" disabled />
    </a-form-item>
    <a-form-item :wrapper-col="{ offset: 4, span: 16 }" style="text-align: center;">
      <a-button type="primary" @click="updateUserInfo">保存</a-button>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Form as AForm, FormItem as AFormItem, Input as AInput, InputPassword as AInputPassword, Button as AButton } from 'ant-design-vue'

const userInfo = ref({
  username: 'User',
  nickname: 'Nickname',
  email: 'user@example.com',
  password: 'man123'
})

const updateUserInfo = () => {
  alert('更新用户信息成功')
  console.log('更新用户信息', userInfo.value)
}
</script>
