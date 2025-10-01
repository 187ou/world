<template>
  <div class="news-hotspots">
    <a-tag
      v-for="(hotspot, index) in props.hotspots || mockHotspots"
      :key="index"
      color="blue"
      class="hotspot-tag"
      @click="$emit('open-preview', hotspot)"
    >
      {{ hotspot.title }}
      <span class="link-icon">🔗</span>
    </a-tag>
  </div>
</template>

<script setup lang="ts">
import { Tag as ATag } from 'ant-design-vue'

interface NewsHotspot {
  title: string
  url: string
}

const props = defineProps<{
  hotspots?: NewsHotspot[]
}>()

const mockHotspots: NewsHotspot[] = [
  { title: '国内疫情最新消息', url: 'https://www.bilibili.com/' },
  { title: '经济复苏新政策', url: 'https://www.iconfont.cn/' },
  { title: '教育改革新方向', url: 'https://news.example.com/education' },
  { title: '科技创新最新成果', url: 'https://news.example.com/tech' },
  { title: '国际关系新动向', url: 'https://news.example.com/international' },
  { title: '文化艺术新发展', url: 'https://news.example.com/culture' },
  { title: '体育赛事新消息', url: 'https://news.example.com/sports' },
  { title: '健康生活新理念', url: 'https://news.example.com/health' }
]
</script>

<style scoped lang="scss">
.news-hotspots {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;

  h4 {
    margin-right: 12px;
    margin-bottom: 0;
  }
}

.hotspot-tag {
  margin: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;

  &:hover {
    opacity: 0.8;
    background-color: #1890ff;
    color: white;
  }
}

.link-icon {
  font-size: 12px;
  margin-left: 4px;
}
</style>
