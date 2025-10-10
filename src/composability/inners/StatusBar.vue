<template>
  <div class="flex justify-between items-center p-2 bg-gray-200 text-sm">
    <div>就绪</div>
    <div>第 {{ editorStore.chapterBegin + 1 }} 页，共 {{ editorStore.chapterLength }} 页</div>
    <div>字数: {{ wordCount }}</div>
    <div>中文(中国)</div>

    <a-tooltip title="布局视图">
      <a-button-group>
        <a-button>打印</a-button>
        <a-button>Web</a-button>
        <a-button>大纲</a-button>
      </a-button-group>
    </a-tooltip>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useEditorStore } from '@/stores/editorStore'

const editorStore = useEditorStore()

const wordCount = computed(() => {
  const text = editorStore.editorContent.replace(/<[^>]*>/g, '')
  return text.length
})
</script>
