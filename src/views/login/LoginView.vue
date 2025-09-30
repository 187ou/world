<template>
  <div
    class="h-screen bg-cover bg-center bg-no-repeat"
    :style="{ backgroundImage: `url(${loginBg})` }"
  ></div>

  <!-- Modal -->
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
      style="min-height: 500px;"
    >
      <!-- 内容容器 -->
      <div class="relative w-full flex" style="height: 500px;">
        <!-- 表单区域 -->
        <div
          :class="[
            'absolute top-0 h-full bg-white px-6 pt-14 pb-5 transition-all duration-700 ease-in-out overflow-y-auto w-[60%]',
            mode === 'register' ? 'left-[40%]' : 'left-0'
          ]"
        >
          <transition name="fade" mode="out-in">
            <LoginForm
              v-if="mode === 'login'"
              key="login"
              @success="closeModal"
              @switchMode="switchMode"
            />
            <RegisterForm
              v-else-if="mode === 'register'"
              key="register"
              @switchMode="switchMode"
            />
            <ForgotPasswordForm
              v-else
              key="forgot"
              @switchMode="switchMode"
            />
          </transition>
        </div>

        <!-- 图片区域 -->
        <div
          :class="[
            'hidden md:block absolute top-0 h-full overflow-hidden transition-all duration-700 ease-in-out',
            mode === 'register' ? 'left-0 w-[40%]' : 'left-[60%] w-[40%]'
          ]"
        >
          <img :src="loginImg" alt="Login Image" class="w-full h-full object-cover" />
        </div>
      </div>

      <!-- 关闭按钮 -->
      <button class="absolute top-3 right-3 w-8 h-8 z-10" @click="closeModal">
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

// 子组件
import LoginForm from '@/composability/logins/LoginForm.vue'
import RegisterForm from '@/composability/logins/RegisterForm.vue'
import ForgotPasswordForm from '@/composability/logins/ForgotPasswordForm.vue'

const isOpen = ref(false)
const mode = ref<'login' | 'register' | 'forgot'>('login')

// 控制模态框
const openModal = () => {
  isOpen.value = true
  document.body.style.overflow = 'hidden'
  mode.value = 'login'
}
const closeModal = () => {
  isOpen.value = false
  document.body.style.overflow = 'auto'
  mode.value = 'login'
}
const switchMode = (target: 'login' | 'register' | 'forgot') => {
  mode.value = target
}

// 卸载时恢复滚动
onBeforeUnmount(() => {
  document.body.style.overflow = 'auto'
})
</script>

<style>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>
