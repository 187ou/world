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

const editorStore = useEditorStore()
const editorRef = ref<HTMLElement | null>(null)

onMounted(() => {
  if (editorRef.value) {
    editorRef.value.innerHTML = editorStore.editorContent
  }
})
</script>
