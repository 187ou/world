<template>
  <a-select
    v-model:value="localValue"
    class="w-16 mr-2"
    @change="handleChange"
  >
    <a-select-option value="8">8</a-select-option>
    <a-select-option value="9">9</a-select-option>
    <a-select-option value="10">10</a-select-option>
    <a-select-option value="11">11</a-select-option>
    <a-select-option value="12">12</a-select-option>
    <a-select-option value="14">14</a-select-option>
    <a-select-option value="16">16</a-select-option>
    <a-select-option value="18">18</a-select-option>
    <a-select-option value="20">20</a-select-option>
    <a-select-option value="22">22</a-select-option>
    <a-select-option value="24">24</a-select-option>
    <a-select-option value="26">26</a-select-option>
    <a-select-option value="28">28</a-select-option>
    <a-select-option value="36">36</a-select-option>
    <a-select-option value="48">48</a-select-option>
    <a-select-option value="72">72</a-select-option>
  </a-select>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';

const props = defineProps<{
  modelValue: string | number;
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
