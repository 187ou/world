<template>
  <div class="news-hotspots">
    <div
      v-for="(hotspot, index) in currentPageData"
      :key="hotspot.url"
      class="hotspot-item"
    >
      <span class="hotspot-title">{{ hotspot.title }}</span>
      <div class="hotspot-actions">
        <a-button
          type="link"
          class="action-btn preview-btn"
          @click="$emit('open-preview', hotspot)"
        >
          预览
        </a-button>
        <a-button
          type="link"
          class="action-btn collect-btn"
          @click="collectDocument(hotspot.url, index)"
        >
          收藏
        </a-button>
      </div>
    </div>

    <a-pagination
      v-if="totalData.length > pageSize"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="totalData.length"
      @change="handlePageChange"
      class="hotspot-pagination"
    />
  </div>
</template>

<script setup lang="ts">
import { Button as AButton, Pagination as APagination, message } from 'ant-design-vue'
import { ref, computed, watch } from 'vue'
import { addBookCollection } from '@/apis/modules/bookApi.ts'

interface NewsHotspot {
  title: string
  url: string
}

const props = defineProps<{
  hotspots?: NewsHotspot[]
  bookName: string
}>()

const pageSize = 10
const currentPage = ref(1)

const totalData = computed(() => props.hotspots || mockHotspots)

const currentPageData = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize
  const endIndex = startIndex + pageSize
  return totalData.value.slice(startIndex, endIndex)
})

const handlePageChange = (page: number) => {
  currentPage.value = page
}

const collectDocument = async (url: string, index: number) => {
  try {
    const currentIndex = index + (currentPage.value - 1) * pageSize
    const title = totalData.value[currentIndex]?.title || '未知文档'
    console.log(`开始收藏《${title}》的书籍《${props.bookName}》`)

    const response = await addBookCollection({
      bookName: props.bookName,
      bookLink: url
    })

    if (response) {
      message.success('收藏成功')
    }
  } catch (error) {
    console.error('收藏失败:', error)
    message.error('收藏失败')
  }
}

const mockHotspots: NewsHotspot[] = [
  { title: '国内疫情最新消息', url: 'https://www.bilibili.com/' },
  { title: '经济复苏新政策', url: 'https://www.iconfont.cn/' },
  { title: '教育改革新方向', url: 'https://news.example.com/education' },
  { title: '科技创新最新成果', url: 'https://news.example.com/tech' },
  { title: '国际关系新动向', url: 'https://news.example.com/international' },
  { title: '文化艺术新发展', url: 'https://news.example.com/culture' },
  { title: '体育赛事新消息', url: 'https://news.example.com/sports' },
  { title: '健康生活新理念', url: 'https://news.example.com/health' },
  { title: '环境保护新举措', url: 'https://news.example.com/env' },
  { title: '交通建设新进展', url: 'https://news.example.com/traffic' },
  { title: '就业市场新动态', url: 'https://news.example.com/job' },
]

watch(totalData, (newVal, oldVal) => {
  if (newVal.length !== oldVal.length) {
    currentPage.value = 1
  }
})
</script>

<style scoped lang="scss">
.news-hotspots {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.hotspot-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background-color: #fff;
  border-radius: 4px;
  border: 1px solid #f0f0f0;
  transition: background-color 0.2s;

  &:hover {
    background-color: #f5f5f5;
  }
}

.hotspot-title {
  flex: 1;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 16px;
  max-width: calc(100% - 120px);
}

.hotspot-actions {
  display: flex;
  gap: 16px;
}

.action-btn {
  padding: 4px 8px;
  height: auto;
}

.preview-btn {
  color: #1890ff;

  &:hover {
    color: #096dd9;
  }
}

.collect-btn {
  color: #faad14;

  &:hover {
    color: #d48806;
    background-color: transparent;
  }
}

.hotspot-pagination {
  margin-top: 16px;
  align-self: center;
}
</style>
