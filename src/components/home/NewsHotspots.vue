<template>
  <div class="news-hotspots">
    <div
      v-for="(hotspot, index) in currentPageData"
      :key="index"
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
          class="action-btn open-btn"
          @click="openDocument(hotspot.url)"
        >
          打开
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
import { Button as AButton, Pagination as APagination } from 'ant-design-vue'
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

interface NewsHotspot {
  title: string
  url: string
}

const props = defineProps<{
  hotspots?: NewsHotspot[]
}>()

const router = useRouter()

const openDocument = (url: string) => {
  router.push({ path: '/inner', query: { url } })
}

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

.open-btn {
  color: #666;

  &:hover {
    color: #333;
    background-color: transparent;
  }
}

.hotspot-pagination {
  margin-top: 16px;
  align-self: center;
}
</style>
