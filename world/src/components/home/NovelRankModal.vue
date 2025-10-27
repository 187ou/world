<template>
  <a-modal :open="isVisible" @cancel="handleCancel" title="小说热度排行榜" :footer="null">
    <div class="p-5 max-h-[400px] overflow-y-auto">
      <div
        v-for="(item, index) in rankData"
        :key="index"
        class="flex justify-between items-center p-3 my-1 border-b border-gray-100 cursor-pointer hover:bg-blue-50 transition-colors duration-300"
        @click="handleNovelClick(item)"
      >
        <span class="w-8 font-bold text-lg text-blue-500">{{ index + 1 }}</span>
        <span class="flex-1 mx-4 text-base text-gray-700">{{ item.title }}</span>
        <span class="font-bold text-red-500">{{ item.heat }}次</span>
      </div>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { Modal as AModal } from 'ant-design-vue'
import type { RankItem } from '@/types/rank'

defineProps<{
  isVisible: boolean
}>()

const emit = defineEmits(['update:isVisible', 'select-novel'])

const handleCancel = () => {
  emit('update:isVisible', false)
}

const handleNovelClick = (novel: RankItem) => {
  emit('select-novel', novel)
  emit('update:isVisible', false)
}

const rankData: RankItem[] = [
  { title: '斗破苍穹', url: 'https://www.biquge.com/book/1', heat: 987 },
  { title: '斗罗大陆', url: 'https://www.biquge.com/book/1', heat: 876 },
  { title: '遮天', url: 'https://www.biquge.com/book/1', heat: 765 },
  { title: '完美世界', url: 'https://www.biquge.com/book/1', heat: 654 },
  { title: '圣墟', url: 'https://www.biquge.com/book/1', heat: 543 }
]
</script>

<style scoped>
</style>
