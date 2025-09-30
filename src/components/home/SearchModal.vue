<template>
  <a-modal
    :open="isVisible"
    title="搜索模板"
    @cancel="handleCancel"
    :footer="null"
    width="1000px"
  >
    <a-input-search
      v-model:value="searchText"
      placeholder="输入模板名称进行搜索"
      enter-button=""
      @search="onSearch"
      size="large"
      class="mb-4"
    />
    <div class="search-results" v-if="searchResults.length > 0">
      <a-list item-layout="horizontal" :data-source="searchResults">
        <template #renderItem="{ item }">
          <a-list-item>
            <a-list-item-meta :title="item.name" :description="item.description">
              <template #avatar>
                <a-avatar :src="item.previewUrl" />
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </div>
    <a-empty v-else description="暂无搜索结果" />
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { Modal as AModal, InputSearch as AInputSearch, List as AList, ListItem as AListItem, ListItemMeta as AListItemMeta, Avatar as AAvatar, Empty as AEmpty } from 'ant-design-vue'

interface Template {
  name: string
  description: string
  previewUrl: string
}

const props = defineProps<{
  isVisible: boolean
  templates: Array<Template>
}>()

const emit = defineEmits(['update:isVisible'])

const searchText = ref<string>('')
const searchResults = ref<Array<Template>>([])

watch(() => props.isVisible, (newVal) => {
  console.log('SearchModal isVisible changed:', newVal)
  if (newVal) {
    searchText.value = ''
    searchResults.value = props.templates
  }
})

const onSearch = (searchValue: string) => {
  if (searchValue) {
    searchResults.value = props.templates.filter(template =>
      template.name.toLowerCase().includes(searchValue.toLowerCase()) ||
      template.description.toLowerCase().includes(searchValue.toLowerCase())
    )
  } else {
    searchResults.value = props.templates
  }
}

const handleCancel = () => {
  emit('update:isVisible', false)
}
</script>

<style scoped>
.search-results {
  max-height: 600px;
  overflow-y: auto;
}
</style>
