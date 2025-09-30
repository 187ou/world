<template>
  <div class="group bg-white rounded-lg border border-gray-300 hover:border-blue-400 transition-all duration-300 overflow-hidden">
    <div class="flex flex-col md:flex-row p-2">
      <!-- 文档图标 -->
      <div class="w-12 h-12 flex-shrink-0">
        <img :src="iconSrc" class="w-full h-full object-cover" :alt="doc.name">
      </div>

      <!-- 文档信息 -->
      <div class="flex-1 md:ml-6 flex flex-col">
        <div class="flex flex-col md:flex-row md:items-center justify-between">
          <h3 class="font-medium text-gray-900 text-lg">{{ doc.name }}</h3>
        </div>
        <p class="text-sm text-gray-500 mt-1">{{ doc.path }}</p>
      </div>
      <div class="flex items-center justify-between">
        <div class="flex flex-col items-end mt-3 md:mt-0">
          <div class="flex flex-wrap gap-2">
            <a-button
              class="px-2 py-1 bg-blue-700 hover:bg-blue-800 text-white rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]"
              @click="emit('open')">
              打开
            </a-button>
            <a-button
              class="px-2 py-1 bg-gray-200 hover:bg-gray-300 text-gray-700 rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]"
              @click="emit('toggle-star')">
              {{ isStarred ? '取消收藏' : '收藏' }}
            </a-button>
            <a-button
              v-if="doc.previewUrl"
              class="px-2 py-1 bg-gray-200 hover:bg-gray-300 text-gray-700 rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]"
              @click="showPreview">
              预览
            </a-button>
          </div>
          <span class="text-xs mt-2">{{ doc.modified }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Button as AButton } from 'ant-design-vue'

interface DocumentItem {
  name: string
  path: string
  modified: string
  previewUrl?: string
}

const props = defineProps<{
  doc: DocumentItem
  isStarred?: boolean
  iconSrc?: string
}>()

const emit = defineEmits<{
  (e: 'open'): void
  (e: 'toggle-star'): void
  (e: 'preview', url: string): void
}>()

const iconSrc = computed(() => {
  if (props.iconSrc) return props.iconSrc
  return props.isStarred
    ? new URL('../../assets/home/收藏.png', import.meta.url).href
    : new URL('../../assets/home/文档.png', import.meta.url).href
})

const showPreview = () => {
  if (props.doc.previewUrl) {
    emit('preview', props.doc.previewUrl)
  }
}
</script>
