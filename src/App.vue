<script setup lang="ts">
import { onMounted, onBeforeUnmount } from 'vue'
import { useUserStore } from '@/stores/user.ts'

const handleBeforeUnload = () => {
  const userStore = useUserStore()
  userStore.clearToken()
}

onMounted(() => {
  window.addEventListener('beforeunload', handleBeforeUnload)
})

onBeforeUnmount(() => {
  window.removeEventListener('beforeunload', handleBeforeUnload)
})
</script>

<template>
  <transition mode="out-in" name="route-transition">
    <router-view />
  </transition>
</template>

<style scoped lang="scss">
:deep(.route-transition-enter-from) {
  transform: translateX(50px);
  opacity: 0;
}
:deep(.route-transition-leave-to) {
  transform: translateX(-50px);
  opacity: 0;
}
:deep(.route-transition-enter-active),
:deep(.route-transition-leave-active) {
  transition: all 0.5s cubic-bezier(0.25, 0.8, 0.25, 1);
}
</style>
