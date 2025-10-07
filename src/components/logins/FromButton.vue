<template>
  <button
    :class="buttonClass"
    :type="type"
    @click="$emit('click')"
    :disabled="disabled"
  >
    <slot></slot>
  </button>
</template>

<script setup lang="ts">
withDefaults(defineProps<{
  type?: 'button' | 'submit' | 'reset'
  buttonClass?: string
  disabled?: boolean
}>(), {
  type: 'button',
  buttonClass: 'btn text-white',
  disabled: false
})

defineEmits(['click'])
</script>
