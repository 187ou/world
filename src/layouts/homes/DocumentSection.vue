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
              @click="showPurchaseModal(doc)">
              购买
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

      <a-tab-pane key="starred" tab="文档收藏">
        <DocumentList
          :documents="starredDocuments"
          :icon-src="starIcon"
          icon-alt="收藏图标"
          :page-size="5">
          <template #actions="{ doc }">
            <a-button
              class="px-2 py-1 bg-blue-700 hover:bg-blue-800 text-white rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]"
              @click="showPurchaseModal(doc)">
              购买
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

      <a-tab-pane key="purchased" tab="我的购买">
        <DocumentList
          :documents="purchasedDocuments"
          :icon-src="buyIcon"
          icon-alt="购买图标"
          :page-size="5">
          <template #actions="{ doc }">
            <a-button
              class="px-2 py-1 bg-blue-700 hover:bg-blue-800 text-white rounded-md text-sm font-medium transition-colors transform hover:translate-y-[-2px]"
              @click="router.push({ path: '/inner', query: { url: doc.previewUrl } })">
              打开
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

    <!-- 购买确认模态框 -->
    <a-modal
      v-model:visible="purchaseModalVisible"
      title="确认购买"
      @cancel="handlePurchaseCancel"
      :footer="null"
    >
      <div class="purchase-modal-content">
        <p>您即将购买: <strong>{{ currentDoc?.name }}</strong></p>
        <div class="purchase-modal-actions">
          <a-button @click="handlePurchaseCancel">取消</a-button>
          <a-button type="primary" @click="confirmPurchase">确认购买</a-button>
        </div>
      </div>
    </a-modal>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  Button as AButton,
  Tabs as ATabs,
  TabPane as ATabPane,
  InputSearch as AInputSearch,
  Modal as AModal
} from 'ant-design-vue'
import PreviewModal from '@/components/common/PreviewModal.vue'
import DocumentList from '@/components/home/DocumentList.vue'
import docIcon from '@/assets/home/文档.png'
import starIcon from '@/assets/home/收藏.png'
import buyIcon from '@/assets/home/购买.png'

const router = useRouter()

const activeTabKey = ref('recent')
const searchQuery = ref('')
const previewModalVisible = ref(false)
const currentPreviewUrl = ref('')
const purchaseModalVisible = ref(false)
const currentDoc = ref<DocumentItem | null>(null)

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

const { recentDocuments, starredDocuments, purchasedDocuments } = defineProps<{
  recentDocuments: DocumentItem[]
  starredDocuments: DocumentItem[]
  purchasedDocuments: DocumentItem[]
}>()

const showPurchaseModal = (doc: DocumentItem) => {
  currentDoc.value = doc
  purchaseModalVisible.value = true
}

const handlePurchaseCancel = () => {
  purchaseModalVisible.value = false
  currentDoc.value = null
}

const confirmPurchase = () => {
  if (currentDoc.value?.previewUrl) {
    // 这里可以添加实际的购买逻辑
    console.log('购买文档:', currentDoc.value)
  }
}
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

.purchase-modal-content {
  .purchase-modal-actions {
    margin-top: 24px;
    text-align: right;

    button {
      margin-left: 12px;
    }
  }
}
</style>
