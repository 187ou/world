<template>
  <div>
    <h1 class="text-[26px] font-normal text-[#55311c]">Man</h1>
    <p class="mt-2 mb-7 text-gray-700">Create your account.</p>

    <div class="form-control mb-4">
      <label class="label text-xs font-semibold uppercase">用户名</label>
      <input type="text" placeholder="Username" class="input input-bordered w-full" />
    </div>
    <div class="form-control mb-4">
      <label class="label text-xs font-semibold uppercase">密码</label>
      <input type="password" placeholder="Password" class="input input-bordered w-full" />
    </div>
    <div class="form-control mb-4">
      <label class="label text-xs font-semibold uppercase">确认密码</label>
      <input type="password" placeholder="Confirm Password" class="input input-bordered w-full" />
    </div>

    <div class="flex justify-between items-center">
      <button class="btn text-white">注册</button>
    </div>

    <p class="mt-10 text-sm text-center">
      已有账户?
      <a href="#" class="text-[#8c7569]" @click.prevent="$emit('switchMode','login')">立即登录</a>
    </p>
  </div>
</template>

<script setup lang="ts">
defineEmits(['switchMode'])
</script>
