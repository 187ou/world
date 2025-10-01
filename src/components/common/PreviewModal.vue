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
    <iframe v-if="url" :src="url" class="preview-iframe" :style="iframeStyle"></iframe>
    <div v-else class="preview-placeholder">
      <a-empty :description="emptyDescription" />
      <div v-if="showExternalLink" class="preview-link">
        <a :href="url" target="_blank">在新窗口中打开</a>
      </div>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Modal as AModal, Empty as AEmpty } from 'ant-design-vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  url: {
    type: String,
    default: ''
  },
  title: {
    type: String,
    default: '预览'
  },
  width: {
    type: String,
    default: '80%'
  },
  height: {
    type: String,
    default: '700px'
  },
  wrapClassName: {
    type: String,
    default: ''
  },
  maskStyle: {
    type: Object,
    default: () => ({ backgroundColor: 'rgba(255, 255, 255, 0.7)' })
  },
  emptyDescription: {
    type: String,
    default: '无法加载预览内容'
  },
  showExternalLink: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:visible', 'cancel'])

const iframeStyle = computed(() => ({
  width: '100%',
  height: props.height,
  border: 'none'
}))

const handleCancel = () => {
  emit('update:visible', false)
  emit('cancel')
}

defineExpose({
  handleCancel
})
</script>

<style scoped lang="scss">
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

/* 全屏模式样式 */
:deep(.full-modal .ant-modal) {
  max-width: 100%;
  top: 0;
  padding-bottom: 0;
  margin: 0;
}

:deep(.full-modal .ant-modal-content) {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 74px);
}

:deep(.full-modal .ant-modal-body) {
  flex-grow: 1;
  padding: 0;
}
</style>
