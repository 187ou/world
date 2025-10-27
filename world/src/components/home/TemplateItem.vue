<template>
  <div
    class="group bg-white rounded-lg overflow-hidden border border-gray-200 shadow-sm hover:shadow-md hover:border-blue-300 transition-all duration-300 transform hover:-translate-y-1 cursor-pointer">
    <!-- 模板预览图 -->
    <div class="aspect-[3/2] bg-gray-100 relative overflow-hidden">
      <img
        :src="template.previewUrl"
        :alt="template.name"
        class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
      >
      <div
        class="absolute inset-0 bg-gradient-to-t from-black/50 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-end">
        <div class="p-4 w-full">
          <a-button
            class="w-full bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-md text-sm font-medium transition-colors transform translate-y-4 group-hover:translate-y-0 opacity-0 group-hover:opacity-100 transition-all duration-300"
            @click="emit('create')">
            新建
          </a-button>
        </div>
      </div>
    </div>

    <!-- 模板信息 -->
    <div class="p-4">
      <h3 class="font-medium text-gray-900">{{ template.name }}</h3>
      <p class="text-sm text-gray-500 mt-1">{{ template.description }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Button as AButton } from 'ant-design-vue'

interface Template {
  name: string
  description: string
  previewUrl: string
}

defineProps<{
  template: Template
}>()

const emit = defineEmits<{
  (e: 'create'): void
}>()
</script>
