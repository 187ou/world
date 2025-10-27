<template>
  <a-tooltip :title="title">
    <a-button
      :type="type"
      :class="buttonClass"
      :style="{ border: 'none' }"
      @click="$emit('click')"
    >
      <slot></slot>
    </a-button>
  </a-tooltip>
</template>

<script setup lang="ts">
defineProps<{
  title: string;
  type?: 'primary' | 'default' | 'text';
  buttonClass?: string;
}>();

defineEmits<{
  (e: 'click'): void;
}>();
</script>
