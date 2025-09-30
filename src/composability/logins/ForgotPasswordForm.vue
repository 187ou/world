<template>
  <div>
    <h1 class="text-[26px] font-normal text-[#55311c]">重置密码</h1>
    <p class="mt-2 mb-7 text-gray-700">请输入邮箱和新密码。</p>

    <div class="form-control mb-4">
      <label class="label text-xs font-semibold uppercase">邮箱</label>
      <input v-model="form.email" type="email" placeholder="请输入邮箱" class="input input-bordered w-full" />
    </div>

    <div class="form-control mb-4">
      <label class="label text-xs font-semibold uppercase">新密码</label>
      <input v-model="form.password" type="password" placeholder="请输入新密码" class="input input-bordered w-full" />
    </div>

    <div class="form-control mb-4">
      <label class="label text-xs font-semibold uppercase">确认密码</label>
      <input v-model="form.confirmPassword" type="password" placeholder="请再次输入新密码" class="input input-bordered w-full" />
    </div>

    <div class="flex justify-between items-center">
      <a href="#" class="text-sm text-gray-500" @click.prevent="$emit('switchMode','login')">返回登录</a>
      <button class="btn text-white" @click="handleForgotPassword">确认修改</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const form = ref({
  email: '',
  password: '',
  confirmPassword: ''
})

const handleForgotPassword = () => {
  if (!form.value.email) return alert('请输入邮箱')
  if (!form.value.password) return alert('请输入新密码')
  if (form.value.password !== form.value.confirmPassword) return alert('两次输入的密码不一致')

  console.log('重置密码参数：', form.value)
  alert('密码修改成功！')
  form.value = { email: '', password: '', confirmPassword: '' }
  emit('switchMode','login')
}

const emit = defineEmits(['switchMode'])
</script>
