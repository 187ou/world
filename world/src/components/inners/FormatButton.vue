<template>
  <ToolButton
    :title="title"
    :type="isActive ? 'primary' : 'text'"
    @click="$emit('click')"
  >
    <slot></slot>
  </ToolButton>
</template>

<script setup lang="ts">
import ToolButton from '@/components/inners/ToolButton.vue'

defineProps<{
  title: string;
  isActive?: boolean;
}>();

defineEmits<{
  (e: 'click'): void;
}>();
</script>
