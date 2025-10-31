<template>
  <a-form :model="userInfo" :label-col="{ span: 4 }" :wrapper-col="{ span: 16 }">
    <a-form-item label="用户名">
      <a-input
        v-model:value="userInfo.username"
        disabled
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
        disabled
      />
      <div v-if="errors.email" class="text-red-500 text-sm">{{ errors.email }}</div>
    </a-form-item>

    <a-form-item label="性别">
      <a-select v-model:value="userInfo.gender" placeholder="请选择性别">
        <a-select-option value="0">其他</a-select-option>
        <a-select-option value="1">男</a-select-option>
        <a-select-option value="2">女</a-select-option>
      </a-select>
    </a-form-item>

    <a-form-item label="手机号">
      <a-input
        v-model:value="userInfo.phone"
        @blur="validatePhone"
      />
      <div v-if="errors.phone" class="text-red-500 text-sm">{{ errors.phone }}</div>
    </a-form-item>

    <a-form-item label="个人留言">
      <a-textarea
        v-model:value="userInfo.bio"
        :rows="4"
        placeholder="随便写点什么吧...反正下次再来不会更新"
      />
    </a-form-item>

    <a-form-item :wrapper-col="{ offset: 4, span: 16 }" style="text-align: center;">
      <a-button type="primary" @click="updateUserInfo" style="margin-right: 16px;">保存</a-button>
      <a-button @click="handleLogout">退出登录</a-button>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import {
  Form as AForm,
  FormItem as AFormItem,
  Input as AInput,
  Button as AButton,
  Select as ASelect,
  SelectOption as ASelectOption,
  Textarea as ATextarea,
  message
} from 'ant-design-vue'
import { formFilters } from '@/filters/formFilters.ts'
import { useUserStore } from '@/stores/user.ts'
import { getCurrentUserInfo, updateUserInfo as updateUserInfoApi } from '@/apis'
import type { UserUpdateDto } from '@/types/userUpdateDto.ts'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
const userInfo = ref({
  username: 'User',
  nickname: 'Nickname',
  email: 'user@example.com',
  gender: '0',
  phone: '13800138000',
  bio: '',
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

const validatePhone = () => {
  const result = formFilters.validatePhone(userInfo.value.phone)
  errors.phone = result.message
  return result.isValid
}

const updateUserInfo = async () => {
  const isUsernameValid = validateUsername()
  const isPhoneValid = validatePhone()

  if (!isUsernameValid || !isPhoneValid) {
    message.error('请检查表单填写内容')
    return
  }

  try {
    const updateData: UserUpdateDto = {
      nickName: userInfo.value.nickname,
      phone: userInfo.value.phone,
      sex: parseInt(userInfo.value.gender),
    }

    const response = await updateUserInfoApi(updateData)

    if (response?.data) {
      message.success('用户信息更新成功')
      await fetchCurrentUserInfo()
    } else {
      message.error('更新失败')
    }
  } catch (error) {
    console.error('更新用户信息失败:', error)
    message.error('更新用户信息时发生错误')
  }
}

const fetchCurrentUserInfo = async () => {
  try {
    const response = await getCurrentUserInfo()
    if (response?.data) {
      userInfo.value = {
        username: response.data.userName || '',
        nickname: response.data.nickName || '',
        email: response.data.email || '',
        gender: response.data.sex?.toString() || '0',
        phone: response.data.phone || '',
        bio: '',
      }
      message.success('获取用户信息成功')
    } else {
      message.error('获取用户信息失败')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    message.error('获取用户信息时发生错误')
  }
}

const handleLogout = async () => {
  try {
    await userStore.logout()
    message.success('已退出登录')
  } catch (error) {
    console.error('退出登录失败:', error)
  } finally {
    await router.push('/login')
  }
}


onMounted(() => {
  fetchCurrentUserInfo()
})
</script>

