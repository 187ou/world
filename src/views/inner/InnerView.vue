<template>
  <div class="h-screen flex flex-col bg-gray-100">
    <TopMenuBar />
    <QuickAccessToolbar />

    <div class="flex flex-1 overflow-hidden backdrop-blur-md bg-blue-50/40">
      <div class="flex-1 p-4 flex justify-center items-start overflow-auto h-full">
        <div class="shadow-lg rounded-lg bg-white p-8 relative"
             :style="{ width: `${800 * (editorStore.zoom / 100)}px`,
                      minHeight: `${1100 * (editorStore.zoom / 100)}px`,
                      fontFamily: editorStore.fontFamily,
                      fontSize: `${editorStore.fontSize}px`,
                      fontWeight: editorStore.isBold ? 'bold' : 'normal',
                      fontStyle: editorStore.isItalic ? 'italic' : 'normal' }"
             contenteditable="true"
             ref="editorRef"
             @input="event => editorStore.setEditorContent((event.target as HTMLElement).innerHTML)"
        >
        </div>
      </div>
      <SidePanel />
    </div>

    <StatusBar />
  </div>
</template>

<script setup lang="ts">
import TopMenuBar from '@/composability/inners/TopMenuBar.vue'
import QuickAccessToolbar from '@/composability/inners/QuickAccessToolBar.vue'
import SidePanel from '@/composability/inners/SidePanel.vue'
import StatusBar from '@/composability/inners/StatusBar.vue'
import { useEditorStore } from '@/stores/editorStore'
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { openBook } from '@/apis/api'

const editorStore = useEditorStore()
const editorRef = ref<HTMLElement | null>(null)
const route = useRoute()
const documentUrl = ref<string | null>(null)

const loadChapterContent = async () => {
  if (documentUrl.value) {
    try {
      const response = await openBook(documentUrl.value)
      console.log('Received Content:', response.data)
      if (response.data && editorRef.value) {
        editorRef.value.innerHTML = response.data.chapterTxt || ''
        editorStore.setEditorContent(response.data.chapterTxt || '')
        editorStore.chapterName = response.data.chapterName
        editorStore.chapterTxtSize = response.data.chapterTxtSize
      }
    } catch (error) {
      console.error('加载章节内容失败:', error)
    }
  }
}

onMounted(async () => {
  if (editorRef.value) {
    editorRef.value.innerHTML = editorStore.editorContent
  }
  if (route.query.url) {
    documentUrl.value = route.query.url as string
    console.log('Received URL:', documentUrl.value)

    const res = await loadChapterContent()
    console.log('Loaded Content:', res)
  }
  if (route.query.length) {
    editorStore.chapterLength = Number(route.query.length)
    console.log('Received Length:', editorStore.chapterLength)
  }
  if (route.query.chapterIndex) {
    editorStore.chapterBegin = Number(route.query.chapterIndex)
    console.log('Received Begin:', editorStore.chapterBegin)
  }
})
</script>
