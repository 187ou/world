<template>
  <div
    class="h-screen bg-cover bg-center bg-no-repeat"
    :style="{ backgroundImage: `url(${loginBg})` }"
  ></div>

  <!-- Modal：保留原模态框逻辑，仅依赖按钮触发 -->
  <div
    :class="[
      'fixed left-0 bottom-0 w-full flex flex-col items-center justify-center transition-all duration-500',
      isOpen ? 'h-full bg-[rgba(51,51,51,0.85)]' : 'h-[60px] bg-[rgba(51,51,51,0.5)]'
    ]"
  >
    <div
      :class="[
        'flex w-full max-w-[720px] rounded-xl overflow-hidden bg-white absolute transition-all duration-500',
        isOpen ? 'opacity-100 pointer-events-auto scale-100 translate-y-0' : 'opacity-0 pointer-events-none scale-75 translate-y-10'
      ]"
    >
      <!-- 左侧内容 -->
      <div
        :class="[
          'flex-[3] bg-white px-6 pt-14 pb-5 transition-all duration-500',
          isOpen ? 'opacity-100 translate-y-0 delay-100' : 'opacity-0 translate-y-20'
        ]"
      >
        <h1 class="text-[26px] font-normal text-[#55311c]">Man!</h1>
        <p class="mt-2 mb-7 text-gray-700">What&nbsp; can&nbsp; I&nbsp; say. &nbsp;&nbsp;&nbsp;See&nbsp;
          you&nbsp; again.</p>

        <!-- 输入框 -->
        <div class="form-control mb-4">
          <label class="label text-xs font-semibold uppercase">User</label>
          <input type="email" placeholder="Email" class="input input-bordered w-full" />
        </div>

        <div class="form-control mb-4">
          <label class="label text-xs font-semibold uppercase">Password</label>
          <input type="password" placeholder="Password" class="input input-bordered w-full" />
        </div>

        <!-- 按钮 -->
        <div class="flex justify-between items-center">
          <a href="#" class="text-sm text-gray-500">忘记密码?</a>
          <button class="btn text-white">登录</button>
        </div>

        <p class="mt-10 text-sm text-center">
          没有账户? <a href="#" class="text-[#8c7569]">现在注册</a>
        </p>
      </div>

      <!-- 右侧图片 -->
      <div class="hidden md:block flex-[2] overflow-hidden">
        <img
          :src="loginImg"
          alt="Login Image"
          :class="['w-full h-full object-cover transition-transform duration-1000', isOpen ? 'scale-100' : 'scale-125']"
        />
      </div>

      <!-- 关闭按钮 -->
      <button class="absolute top-3 right-3 w-8 h-8" @click="closeModal">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50">
          <path
            d="M 25 3 C 12.86158 3 3 12.86158 3 25 C 3 37.13842 12.86158 47 25 47 C 37.13842 47 47 37.13842 47 25 C 47 12.86158 37.13842 3 25 3 z M 25 5 C 36.05754 5 45 13.94246 45 25 C 45 36.05754 36.05754 45 25 45 C 13.94246 45 5 36.05754 5 25 C 5 13.94246 13.94246 5 25 5 z M 16.990234 15.990234 A 1.0001 1.0001 0 0 0 16.292969 17.707031 L 23.585938 25 L 16.292969 32.292969 A 1.0001 1.0001 0 1 0 17.707031 33.707031 L 25 26.414062 L 32.292969 33.707031 A 1.0001 1.0001 0 1 0 33.707031 32.292969 L 26.414062 25 L 33.707031 17.707031 A 1.0001 1.0001 0 0 0 32.980469 15.990234 A 1.0001 1.0001 0 0 0 32.292969 16.292969 L 25 23.585938 L 17.707031 16.292969 A 1.0001 1.0001 0 0 0 16.990234 15.990234 z"
          />
        </svg>
      </button>
    </div>

    <!-- 打开按钮 -->
    <button
      v-if="!isOpen"
      class="btn px-10 py-2 rounded-full bg-white shadow-lg text-lg text-[rgb(125,105,94)]"
      @click="openModal"
    >
      点击登录
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, onBeforeUnmount } from 'vue'
import loginBg from '@/assets/login/login.png'
import loginImg from '@/assets/login/right.avif'

const isOpen = ref(false)

// 模态框打开/关闭逻辑（保留按钮触发，删除滚动触发）
const openModal = () => {
  isOpen.value = true
  document.body.style.overflow = 'hidden'
}

const closeModal = () => {
  isOpen.value = false
  document.body.style.overflow = 'auto'
}

// 组件卸载前确保恢复页面滚动（防止异常场景下滚动被锁定）
onBeforeUnmount(() => {
  document.body.style.overflow = 'auto'
})
</script>
