<template>
  <div class="flex items-center">
    <ToolButton title="缩小" @click="zoomOut">
      <ZoomOutOutlined />
    </ToolButton>
    <ToolButton title="放大" @click="zoomIn">
      <ZoomInOutlined />
    </ToolButton>
    <span class="mr-2">{{ zoom }}%</span>
  </div>
</template>

<script setup lang="ts">
import { ZoomOutOutlined, ZoomInOutlined } from '@ant-design/icons-vue';
import ToolButton from '@/components/inners/ToolButton.vue'

const props = defineProps<{
  zoom: number;
}>();

const emit = defineEmits<{
  (e: 'update:zoom', value: number): void;
}>();

const zoomOut = () => {
  const newZoom = Math.max(10, props.zoom - 10);
  emit('update:zoom', newZoom);
};

const zoomIn = () => {
  const newZoom = Math.min(200, props.zoom + 10);
  emit('update:zoom', newZoom);
};
</script>
