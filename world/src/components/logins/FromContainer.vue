<template>
  <div>
    <h1 class="text-[26px] font-normal text-[#55311c]">{{ title }}</h1>
    <p v-if="description" class="mt-2 mb-7 text-gray-700">{{ description }}</p>

    <slot></slot>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title: string
  description?: string
}>()
</script>
