<template>
  <div class="search-results">
    <a-list item-layout="horizontal" :data-source="templates">
      <template #renderItem="{ item }">
        <a-list-item @click="() => $emit('toggle-expand', item)" class="clickable-item">
          <a-list-item-meta :title="item.name" :description="item.description">
            <template #avatar>
              <a-avatar :src="item.previewUrl" />
            </template>
          </a-list-item-meta>
          <template #extra>
            <a-button type="link">
              {{ expandedItems.includes(item.name) ? '收起' : '展开' }}
            </a-button>
          </template>
        </a-list-item>

        <div v-if="expandedItems.includes(item.name)" class="expanded-content">
          <NewsHotspots
            :hotspots="item.newsHotspots"
            @open-preview="(h) => $emit('open-preview', h)"
          />
        </div>
      </template>
    </a-list>
  </div>
</template>

<script setup lang="ts">
import { Avatar as AAvatar, List as AList, ListItem as AListItem, ListItemMeta as AListItemMeta, Button as AButton } from 'ant-design-vue'
import NewsHotspots from './NewsHotspots.vue'

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

defineProps<{
  templates: Array<Template>
  expandedItems: string[]
}>()

defineEmits(['toggle-expand', 'open-preview'])
</script>

<style scoped lang="scss">
.search-results {
  max-height: 500px;
  overflow-y: auto;
}

.clickable-item {
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: #f5f5f5;
  }
}

.expanded-content {
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 4px;
  margin-bottom: 16px;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
}
</style>
