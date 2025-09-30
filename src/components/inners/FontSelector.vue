<template>
  <a-select
    v-model:value="localValue"
    class="w-32 mr-2"
    @change="handleChange"
  >
    <a-select-option value="宋体">宋体</a-select-option>
    <a-select-option value="微软雅黑">微软雅黑</a-select-option>
    <a-select-option value="黑体">黑体</a-select-option>
    <a-select-option value="楷体">楷体</a-select-option>
  </a-select>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';

const props = defineProps<{
  modelValue: string;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void;
}>();

const localValue = ref(props.modelValue);

const handleChange = (value: string) => {
  emit('update:modelValue', value);
};

watch(() => props.modelValue, (newVal) => {
  localValue.value = newVal;
});
</script>
