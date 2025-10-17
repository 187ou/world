<template>
  <a-modal :open="isVisible" @cancel="handleCancel" title="小说热度排行榜" :footer="null">
    <div class="rank-list">
      <div v-for="(item, index) in rankData" :key="index" class="rank-item" @click="handleNovelClick(item)">
        <span class="rank-number">{{ index + 1 }}</span>
        <span class="novel-title">{{ item.title }}</span>
        <span class="heat-value">{{ item.heat }}次</span>
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
