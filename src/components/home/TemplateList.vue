<template>
  <div class="search-results">
    <a-list item-layout="horizontal" :data-source="templates">
      <template #renderItem="{ item }">
        <a-list-item :key="item.name">
          <a-list-item-meta
            :title="item.name"
            :description="item.description"
          >
            <template #avatar>
              <a-avatar :src="item.avatar || man" />
            </template>
          </a-list-item-meta>

          <template #extra>
            <a-button type="link" @click="$emit('toggle-expand', item)">
              {{ expandedSet.has(item.name) ? '收起' : '展开' }}
            </a-button>
          </template>
        </a-list-item>

        <div v-if="expandedSet.has(item.name)" class="expanded-content">
          <div v-if="loadingSet.has(item.name)" class="loading-wrapper">
            <a-spin size="small" />
            <span class="loading-text">man~</span>
          </div>

          <NewsHotspots
            v-else
            :hotspots="item.newsHotspots || []"
            :book-name="item.name"
            @open-preview="(h: NewsHotspot) => $emit('open-preview', h)"
          />
        </div>
      </template>
    </a-list>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import {
  Avatar as AAvatar,
  List as AList,
  ListItem as AListItem,
  ListItemMeta as AListItemMeta,
  Button as AButton,
  Spin as ASpin
} from 'ant-design-vue'
import NewsHotspots from './NewsHotspots.vue'
import man from '@/assets/home/man.webp'

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
  templates: Array<Template>
  expandedItems: string[]
  loadingSet: Set<string>
}>()

defineEmits(['toggle-expand', 'open-preview'])

const expandedSet = computed(() => new Set(props.expandedItems))
</script>

<style scoped lang="scss">
.expanded-content {
  padding: 12px 16px;
  background-color: #fafafa;
  border-radius: 8px;
  margin-bottom: 12px;
  transition: all 0.2s ease-in-out;

  .loading-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 12px 0;
    gap: 6px;
    color: #888;
    font-size: 13px;

    .loading-text {
      user-select: none;
      font-size: 13px;
    }
  }
}
</style>

