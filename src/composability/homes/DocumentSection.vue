<template>
  <section class="h-[calc(100vh-300px)]">
    <a-tabs v-model:activeKey="activeTabKey" class="mb-6">
      <template #rightExtra>
        <a-input-search
          v-model:value="searchQuery"
          placeholder="搜索文档"
          class="w-80 shadow-md rounded-lg"
          @search="onSearch"
        />
      </template>
      <a-tab-pane key="recent" tab="最近文档">
        <!-- 最近文档列表 -->
        <div class="space-y-3">
          <template v-for="(doc, index) in paginatedRecentDocuments" :key="index">
            <DocumentItem
              :doc="doc"
              :is-starred="false"
              @open="handleOpenDocument(doc)"
              @toggle-star="handleToggleStar(doc)"
              @preview="showPreviewModal"
            />
          </template>
        </div>
        <div class="flex justify-center mt-4 p-4">
          <a-pagination
            v-model:current="currentRecentPage"
            :total="recentDocuments.length"
            :page-size="pageSize"
            show-less-items
            @change="onRecentPageChange"
          />
        </div>
      </a-tab-pane>
      <a-tab-pane key="starred" tab="我的收藏">
        <!-- 我的收藏内容 -->
        <div class="space-y-3">
          <template v-for="(doc, index) in paginatedStarredDocuments" :key="index">
            <DocumentItem
              :doc="doc"
              :is-starred="true"
              @open="handleOpenDocument(doc)"
              @toggle-star="handleToggleStar(doc)"
              @preview="showPreviewModal"
            />
          </template>
        </div>
        <div class="flex justify-center mt-4 p-4">
          <a-pagination
            v-model:current="currentStarredPage"
            :total="starredDocuments.length"
            :page-size="pageSize"
            show-less-items
            @change="onStarredPageChange"
          />
        </div>
      </a-tab-pane>
    </a-tabs>

    <!-- 预览模态框 -->
    <a-modal
      v-model:open="previewModalVisible"
      title="请勿点击内部！仅供预览！"
      :footer="null"
      width="80%"
      wrap-class-name="full-modal"
      @cancel="handlePreviewCancel"
    >
      <div class="w-full h-[70vh]">
        <iframe :src="currentPreviewUrl" frameborder="0" class="w-full h-full" sandbox="allow-scripts allow-same-origin allow-popups allow-forms allow-modals"></iframe>
      </div>
    </a-modal>
  </section>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import {
  Tabs as ATabs,
  TabPane as ATabPane,
  InputSearch as AInputSearch,
  Modal as AModal,
  Pagination as APagination
} from 'ant-design-vue'
import DocumentItem from '@/components/home/DocumentList.vue'

const activeTabKey = ref('recent')
const searchQuery = ref('')
const previewModalVisible = ref(false)
const currentPreviewUrl = ref('')

const onSearch = (searchValue: string) => {
  console.log('当前内容：', searchValue)
  console.log(searchQuery.value)
}

const showPreviewModal = (url: string) => {
  currentPreviewUrl.value = url
  previewModalVisible.value = true
}

const handlePreviewCancel = () => {
  previewModalVisible.value = false
  currentPreviewUrl.value = ''
}

// 处理打开文档
const handleOpenDocument = (doc: DocumentItem) => {
  console.log('打开文档:', doc.name)
  // 实现打开文档逻辑
}

// 处理收藏/取消收藏
const handleToggleStar = (doc: DocumentItem) => {
  console.log('切换收藏状态:', doc.name)
  // 实现收藏逻辑
}

const currentRecentPage = ref(1)
const currentStarredPage = ref(1)
const pageSize = 5

interface DocumentItem {
  name: string
  path: string
  modified: string
  previewUrl?: string
}

const props = defineProps<{
  recentDocuments: DocumentItem[]
  starredDocuments: DocumentItem[]
}>()

const paginatedRecentDocuments = computed(() => {
  const start = (currentRecentPage.value - 1) * pageSize
  const end = start + pageSize
  return props.recentDocuments.slice(start, end)
})

const paginatedStarredDocuments = computed(() => {
  const start = (currentStarredPage.value - 1) * pageSize
  const end = start + pageSize
  return props.starredDocuments.slice(start, end)
})

const onRecentPageChange = (page: number) => {
  currentRecentPage.value = page
}

const onStarredPageChange = (page: number) => {
  currentStarredPage.value = page
}
</script>

<style scoped>
.full-modal .ant-modal {
  max-width: 100%;
  top: 0;
  padding-bottom: 0;
  margin: 0;
}
.full-modal .ant-modal-content {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 74px);
}
.full-modal .ant-modal-body {
  flex-grow: 1;
  padding: 0;
}
</style>
