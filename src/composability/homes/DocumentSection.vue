<template>
  <section class="document-section">
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
        <DocumentList
          :documents="recentDocuments"
          :icon-src="docIcon"
          icon-alt="文档图标"
          :page-size="5">
          <template #actions="{ doc }">
            <a-button
              class="px-2 py-1 bg-blue-700 hover:bg-blue-800 text-white rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]"
              @click="router.push({ path: '/inner', query: { url: doc.previewUrl } })">
              打开
            </a-button>
            <a-button
              class="px-2 py-1 bg-gray-200 hover:bg-gray-300 text-gray-700 rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]">
              收藏
            </a-button>
            <a-button
              class="px-2 py-1 bg-gray-200 hover:bg-gray-300 text-gray-700 rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]"
              @click="doc.previewUrl && showPreviewModal(doc.previewUrl)">
              预览
            </a-button>
          </template>
        </DocumentList>
      </a-tab-pane>

      <a-tab-pane key="starred" tab="我的收藏">
        <DocumentList
          :documents="starredDocuments"
          :icon-src="starIcon"
          icon-alt="收藏图标"
          :page-size="5">
          <template #actions="{ doc }">
            <a-button
              class="px-2 py-1 bg-blue-700 hover:bg-blue-800 text-white rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]"
              @click="router.push({ path: '/inner', query: { url: doc.previewUrl } })">
              打开
            </a-button>
            <a-button
              class="px-2 py-1 bg-gray-200 hover:bg-gray-300 text-gray-700 rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]">
              取消收藏
            </a-button>
            <a-button
              class="px-2 py-1 bg-gray-200 hover:bg-gray-300 text-gray-700 rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]"
              @click="doc.previewUrl && showPreviewModal(doc.previewUrl)">
              预览
            </a-button>
          </template>
        </DocumentList>
      </a-tab-pane>
    </a-tabs>

    <!-- 预览模态框 -->
    <PreviewModal
      :visible="previewModalVisible"
      @update:visible="previewModalVisible = $event"
      :url="currentPreviewUrl"
      title="请勿点击内部！仅供预览！"
      wrap-class-name="full-modal"
      height="70vh"
      @cancel="handlePreviewCancel"
    />
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  Button as AButton,
  Tabs as ATabs,
  TabPane as ATabPane,
  InputSearch as AInputSearch
} from 'ant-design-vue'
import PreviewModal from '@/components/common/PreviewModal.vue'
import DocumentList from '@/components/home/DocumentList.vue'
import docIcon from '@/assets/home/文档.png'
import starIcon from '@/assets/home/收藏.png'

const router = useRouter()

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

interface DocumentItem {
  name: string
  path: string
  modified: string
  previewUrl?: string
}

const { recentDocuments, starredDocuments } = defineProps<{
  recentDocuments: DocumentItem[]
  starredDocuments: DocumentItem[]
}>()
</script>

<style scoped lang="scss">
.document-section {
  height: calc(100vh - 300px);
}

.full-modal {
  .ant-modal {
    max-width: 100%;
    top: 0;
    padding-bottom: 0;
    margin: 0;
  }

  .ant-modal-content {
    display: flex;
    flex-direction: column;
    height: calc(100vh - 74px);
  }

  .ant-modal-body {
    flex-grow: 1;
    padding: 0;
  }
}
</style>
