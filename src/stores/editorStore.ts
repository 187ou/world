import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useEditorStore = defineStore('editor', () => {
  const zoom = ref(100)
  const fontFamily = ref('宋体')
  const fontSize = ref(18)
  const isBold = ref(false)
  const isItalic = ref(false)
  const editorContent = ref('<p>使用说明书</p>') // 初始化内容

  function toggleBold() {
    isBold.value = !isBold.value
  }

  function toggleItalic() {
    isItalic.value = !isItalic.value
  }

  function setEditorContent(content: string) {
    editorContent.value = content
  }

  return { zoom, fontFamily, fontSize, isBold, isItalic, editorContent, toggleBold, toggleItalic, setEditorContent }
})
