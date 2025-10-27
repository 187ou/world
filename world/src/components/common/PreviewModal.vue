<template>
  <a-modal
    :open="visible"
    :title="title"
    :footer="null"
    :width="width"
    :wrap-class-name="wrapClassName"
    :mask-style="maskStyle"
    @cancel="handleCancel"
    @update:open="(val) => emit('update:visible', val)"
  >
    <div class="iframe-container">
      <transition name="fade">
        <div v-if="loading" class="iframe-loading">
          <div class="loader"></div>
        </div>
      </transition>

      <iframe
        v-if="url"
        :src="url"
        class="preview-iframe"
        :style="iframeStyle"
        @load="handleLoad"
      ></iframe>

      <div v-else class="preview-placeholder">
        <a-empty :description="emptyDescription" />
        <div v-if="showExternalLink" class="preview-link">
          <a :href="url" target="_blank">在新窗口中打开</a>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { Modal as AModal, Empty as AEmpty } from 'ant-design-vue'

const props = defineProps({
  visible: Boolean,
  url: String,
  title: { type: String, default: '预览' },
  width: { type: String, default: '80%' },
  height: { type: String, default: '700px' },
  wrapClassName: { type: String, default: '' },
  maskStyle: { type: Object, default: () => ({ backgroundColor: 'rgba(255,255,255,0.7)' }) },
  emptyDescription: { type: String, default: '无法加载预览内容' },
  showExternalLink: { type: Boolean, default: true }
})

const emit = defineEmits(['update:visible', 'cancel'])

const loading = ref(true)

const iframeStyle = computed(() => ({
  width: '100%',
  height: props.height,
  border: 'none',
  display: loading.value ? 'none' : 'block'
}))

const handleCancel = () => {
  emit('update:visible', false)
  emit('cancel')
}

const handleLoad = () => {
  setTimeout(() => (loading.value = false), 200) // 避免闪屏，虽说可有可无
}

watch(
  () => [props.visible, props.url],
  ([visible, url]) => {
    if (visible && url) loading.value = true
  }
)
</script>

<style scoped lang="scss">
.iframe-container {
  position: relative;
  min-height: 200px;
}

.iframe-loading {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(3px);
  z-index: 10;
  transition: opacity 0.3s ease;
}

.loader {
  width: 48px;
  height: 48px;
  border: 3px solid #e0e0e0;
  border-top-color: #1890ff;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.preview-iframe {
  width: 100%;
  height: 700px;
  border: none;
}

.preview-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;

  .preview-link {
    margin-top: 16px;

    a {
      color: #1890ff;
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}
</style>
