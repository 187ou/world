<template>
  <section class="mb-10">
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-xl font-semibold">模板</h2>
      <a-button
        class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors flex items-center group"
        @click="emit('show-search-modal')"
      >
        查看全部
        <i
          class="fa fa-angle-right ml-1 transform group-hover:translate-x-1 transition-transform"></i>
      </a-button>
    </div>

    <div
      class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-6 gap-5">
      <template v-for="(template, index) in templates.slice(0, 6)" :key="index">
        <TemplateItem
          :template="template"
          @create="emit('show-search-modal')"
        />
      </template>
    </div>
  </section>
</template>

<script setup lang="ts">
import { Button as AButton } from 'ant-design-vue'
import TemplateItem from '@/components/home/TemplateItem.vue'

interface Template {
  name: string
  description: string
  previewUrl: string
}

defineProps<{
  templates: Array<Template>
}>()

const emit = defineEmits(['show-search-modal'])
</script>
