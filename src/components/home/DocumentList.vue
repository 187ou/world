<template>
  <div class="space-y-3">
    <template v-for="(doc, index) in paginatedDocuments" :key="index">
      <div
        class="group bg-white rounded-lg border border-gray-300 hover:border-blue-400 transition-all duration-300 overflow-hidden">
        <div class="flex flex-col md:flex-row p-2">
          <!-- 文档图标 -->
          <div class="w-12 h-12 flex-shrink-0">
            <img :src="iconSrc" class="w-full h-full object-cover" :alt="iconAlt">
          </div>

          <!-- 文档信息 -->
          <div class="flex-1 md:ml-6 flex flex-col">
            <div class="flex flex-col md:flex-row md:items-center justify-between">
              <h3 class="font-medium text-gray-900 text-lg">{{ doc.name }}</h3>
            </div>
            <p class="text-sm text-gray-500 mt-1">{{ doc.path }}</p>
          </div>
          <div class="flex items-center justify-between">
            <div class="flex flex-col items-end mt-3 md:mt-0">
              <div class="flex flex-wrap gap-2">
                <slot name="actions" :doc="doc"></slot>
              </div>
              <span class="text-xs mt-2">{{ doc.modified }}</span>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>

  <div class="flex justify-center mt-4 p-4">
    <a-pagination
      v-model:current="currentPage"
      :total="documents.length"
      :page-size="pageSize"
      show-less-items
      @change="onPageChange"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { Pagination as APagination } from 'ant-design-vue'

interface DocumentItem {
  name: string
  path: string
  modified: string
  previewUrl?: string
}

const props = defineProps<{
  documents: DocumentItem[]
  iconSrc: string
  iconAlt: string
  pageSize?: number
}>()

const currentPage = ref(1)
const pageSize = props.pageSize || 5

const paginatedDocuments = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return props.documents.slice(start, end)
})

const onPageChange = (page: number) => {
  currentPage.value = page
}
</script>
