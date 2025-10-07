<template>
  <div class="form-control mb-4">
    <label v-if="label" class="label text-xs font-semibold uppercase">{{ label }}</label>
    <div v-if="type === 'password'" class="relative">
      <input
        :value="modelValue"
        @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)"
        :type="passwordVisible ? 'text' : 'password'"
        :placeholder="placeholder"
        :class="inputClass"
        :maxlength="maxlength"
      />
      <button
        v-if="showPasswordToggle"
        type="button"
        @click="togglePasswordVisibility"
        class="absolute inset-y-0 right-0 pr-3 flex items-center"
      >
        {{ passwordVisible ? '🙈' : '🐵' }}
      </button>
    </div>
    <div v-else-if="type === 'captcha'" class="flex items-center gap-3">
      <input
        :value="modelValue"
        @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)"
        type="text"
        :placeholder="placeholder"
        :class="inputClass"
        :maxlength="maxlength"
      />
      <slot name="captcha"></slot>
    </div>
    <input
      v-else
      :value="modelValue"
      @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)"
      :type="type"
      :placeholder="placeholder"
      :class="inputClass"
      :maxlength="maxlength"
      v-bind="$attrs"
    />
    <div v-if="error" class="text-red-500 text-sm mt-1">{{ error }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

withDefaults(defineProps<{
  label?: string
  modelValue: string
  type?: 'text' | 'password' | 'email' | 'captcha'
  placeholder?: string
  error?: string
  maxlength?: number | string
  showPasswordToggle?: boolean
  inputClass?: string
}>(), {
  type: 'text',
  inputClass: 'input input-bordered w-full',
  showPasswordToggle: false
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

const passwordVisible = ref(false)

const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value
}
</script>

