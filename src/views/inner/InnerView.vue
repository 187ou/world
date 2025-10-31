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
import TopMenuBar from '@/layouts/inners/TopMenuBar.vue'
import QuickAccessToolbar from '@/layouts/inners/QuickAccessToolBar.vue'
import SidePanel from '@/layouts/inners/SidePanel.vue'
import StatusBar from '@/layouts/inners/StatusBar.vue'
import { useEditorStore } from '@/stores/editorStore'
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { openBook } from '@/apis'

const editorStore = useEditorStore()
const editorRef = ref<HTMLElement | null>(null)
const route = useRoute()
const documentUrl = ref<string | null>(null)

// 用于存储待显示的文本内容
const contentQueue = ref<string[]>([])
// 当前已显示的文本
const displayedContent = ref<string>('')

const handleKeyPress = (event: KeyboardEvent) => {
  event.preventDefault()

  if (contentQueue.value.length > 0 &&
    !event.ctrlKey &&
    !event.altKey &&
    event.key !== 'Shift' &&
    event.key !== 'CapsLock' &&
    event.key !== 'Tab') {

    const nextChar = contentQueue.value.shift()
    if (nextChar !== undefined) {
      displayedContent.value += nextChar
      if (editorRef.value) {
        editorRef.value.innerHTML = displayedContent.value
        editorStore.setEditorContent(displayedContent.value)
      }
    }
  }
}

// 加载章节内容但不直接显示
const loadChapterContent = async () => {
  if (documentUrl.value) {
    try {
      const response = await openBook(documentUrl.value)
      console.log('Received Content:', response.data)
      if (response.data) {
        const content = response.data.chapterTxt || ''
        contentQueue.value = content.split('')
        displayedContent.value = ''
        editorStore.chapterName = response.data.chapterName
        editorStore.chapterTxtSize = response.data.chapterTxtSize

        if (editorRef.value) {
          editorRef.value.innerHTML = ''
          editorStore.setEditorContent('')
        }
      }
    } catch (error) {
      console.error('加载章节内容失败:', error)
    }
  }
}

onMounted(async () => {
  window.addEventListener('keydown', handleKeyPress)

  if (editorRef.value) {
    editorRef.value.innerHTML = editorStore.editorContent
  }
  if (route.query.url) {
    documentUrl.value = route.query.url as string
    console.log('Received URL:', documentUrl.value)

    await loadChapterContent()
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

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeyPress)
})
</script>
