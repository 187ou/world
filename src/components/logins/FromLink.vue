<template>
  <a
    href="#"
    :class="linkClass"
    @click.prevent="$emit('click')"
  >
    <slot></slot>
  </a>
</template>

<script setup lang="ts">
withDefaults(defineProps<{
  linkClass?: string
}>(), {
  linkClass: 'text-sm text-gray-500'
})

defineEmits(['click'])
</script>
