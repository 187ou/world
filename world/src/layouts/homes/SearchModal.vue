<template>
  <a-modal
    :open="isVisible"
    title="搜索模板"
    @cancel="handleCancel"
    :footer="null"
    width="1000px"
  >
    <!-- 搜索框 -->
    <a-input-search
      v-model:value="searchText"
      placeholder="输入模板名称进行搜索"
      enter-button=""
      @search="onSearch"
      size="large"
      class="mb-4 sticky-top"
    />

    <!-- 搜索结果 -->
    <a-spin :spinning="searching" tip="孩子们，等我一下..." size="large">
      <div class="scroll-content">
        <template v-if="searchResults.length > 0">
          <TemplateList
            :templates="searchResults"
            :expanded-items="expandedItems"
            :loading-set="loadingSet"
            @toggle-expand="toggleExpand"
            @open-preview="openPreview"
          />
        </template>
        <a-empty v-else description="暂无搜索结果" />
      </div>
    </a-spin>

    <!-- 预览弹窗 -->
    <PreviewModal
      :visible="previewVisible"
      @update:visible="previewVisible = $event"
      :url="previewUrl"
      :title="previewTitle"
      class="preview-modal"
      :mask-style="{ backgroundColor: 'rgba(255, 255, 255, 0.7)' }"
    />
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { InputSearch as AInputSearch, Empty as AEmpty, Spin as ASpin } from 'ant-design-vue'
import TemplateList from '@/components/home/TemplateList.vue'
import PreviewModal from '@/components/common/PreviewModal.vue'
import { searchBooks, searchChapters } from '@/apis/api'
import type { SearchBookVo } from '@/types/book'
import type { Chapter } from '@/types/chapter'

interface NewsHotspot {
  title: string
  url: string
}

interface Template {
  name: string
  description: string
  previewUrl: string
  newsHotspots?: NewsHotspot[]
}

const props = defineProps<{
  isVisible: boolean
  templates: Array<Template>
}>()

const emit = defineEmits(['update:isVisible'])

const searchText = ref('')
const searchResults = ref<Template[]>([])
const expandedItems = ref<string[]>([])
const previewVisible = ref(false)
const previewUrl = ref('')
const previewTitle = ref('')

const chapterCache = new Map<string, NewsHotspot[]>()
const loadingSet = ref<Set<string>>(new Set())
const searching = ref(false)

// 弹窗打开时重置
watch(() => props.isVisible, (val) => {
  if (val) {
    searchText.value = ''
    searchResults.value = props.templates
    expandedItems.value = []
  }
})

// 懒加载章节
const toggleExpand = async (item: Template) => {
  const index = expandedItems.value.indexOf(item.name)
  if (index === -1) {
    expandedItems.value.push(item.name)
    if (chapterCache.has(item.name)) return

    loadingSet.value.add(item.name)
    try {
      const chapterRes = await searchChapters({
        bookName: item.name,
        bookLink: item.previewUrl
      })

      const hotspots =
        chapterRes?.data?.list?.map((chapter: Chapter) => ({
          title: chapter.chapterName || '未知章节',
          url: chapter.chapterLink || ''
        })) || []

      chapterCache.set(item.name, hotspots)

      const target = searchResults.value.find((t) => t.name === item.name)
      if (target) target.newsHotspots = hotspots
    } catch (err) {
      console.error(`加载章节失败: ${item.name}`, err)
    } finally {
      loadingSet.value.delete(item.name)
    }
  } else {
    expandedItems.value.splice(index, 1)
  }
}

// 搜索
const onSearch = async (searchValue: string) => {
  if (!searchValue?.trim()) {
    searchResults.value = []
    return
  }

  searching.value = true
  try {
    console.log('搜索书名:', searchValue.trim())
    const res = await searchBooks(searchValue.trim())
    if (res?.data?.list && Array.isArray(res.data.list)) {
      searchResults.value = res.data.list
        .filter((book: SearchBookVo): book is SearchBookVo => !!book?.bookName && !!book?.bookLink)
        .map((book: SearchBookVo) => ({
          name: book.bookName,
          description: book.bookName,
          previewUrl: book.bookLink,
          newsHotspots: []
        }))
    } else {
      searchResults.value = []
    }
  } catch (error) {
    console.error('搜索失败:', error)
    searchResults.value = []
  } finally {
    searching.value = false
  }
}

// 关闭模态框
const handleCancel = () => {
  searchText.value = ''
  searchResults.value = props.templates
  expandedItems.value = []
  previewVisible.value = false
  emit('update:isVisible', false)
}

// 打开预览
const openPreview = (hotspot: NewsHotspot) => {
  previewTitle.value = hotspot.title
  previewUrl.value = hotspot.url
  previewVisible.value = true
}
</script>

<style scoped lang="scss">
.mb-4 {
  margin-bottom: 16px;
}

.scroll-content {
  max-height: 65vh;
  overflow-y: auto;
  padding-right: 8px;
}

.sticky-top {
  position: sticky;
  top: 0;
  z-index: 10;
  background: #fff;
  padding-bottom: 8px;
}
</style>

