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

    <template v-if="searchResults.length > 0">
      <TemplateList
        :templates="searchResults"
        :expanded-items="expandedItems"
        @toggle-expand="toggleExpand"
        @open-preview="openPreview"
      />
    </template>
    <a-empty v-else description="暂无搜索结果" />

    <PreviewModal
      :visible="previewVisible"
      @update:visible="previewVisible = $event"
      :url="previewUrl"
      :title="previewTitle"
      class="preview-modal"
      :mask-style="{ backgroundColor: 'rgba(255, 255, 255, 0.7)' }"
    />
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { InputSearch as AInputSearch, Empty as AEmpty } from 'ant-design-vue'
import TemplateList from '@/components/home/TemplateList.vue'
import PreviewModal from '@/components/common/PreviewModal.vue'

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
  isVisible: boolean
  templates: Array<Template>
}>()

const emit = defineEmits(['update:isVisible'])

const searchText = ref<string>('')
const searchResults = ref<Array<Template>>([])
const expandedItems = ref<string[]>([])
const previewVisible = ref(false)
const previewUrl = ref('')
const previewTitle = ref('')

watch(() => props.isVisible, (newVal) => {
  if (newVal) {
    searchText.value = ''
    searchResults.value = props.templates
    expandedItems.value = []
  }
})

const toggleExpand = (item: Template) => {
  const index = expandedItems.value.indexOf(item.name)
  if (index === -1) {
    expandedItems.value.push(item.name)
  } else {
    expandedItems.value.splice(index, 1)
  }
}

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

const openPreview = (hotspot: NewsHotspot) => {
  previewTitle.value = hotspot.title
  previewUrl.value = hotspot.url
  previewVisible.value = true
}
</script>
