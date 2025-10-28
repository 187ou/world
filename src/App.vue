<script setup lang="ts">
import { RouterView } from 'vue-router'
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
