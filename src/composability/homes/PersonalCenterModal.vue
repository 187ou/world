<template>
  <a-modal
    :open="isVisible"
    title="个人中心"
    @cancel="handleCancel"
    :footer="null"
    width="800px"
  >
    <div class="personal-center-content">
      <a-tabs v-model:activeKey="activeKey">
        <a-tab-pane key="1" tab="个人信息">
          <UserInfoForm />
        </a-tab-pane>
        <a-tab-pane key="2" tab="修改密码">
          <PasswordForm />
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Modal as AModal, Tabs as ATabs, TabPane as ATabPane } from 'ant-design-vue'
import UserInfoForm from '@/components/home/UserInfoForm.vue'
import PasswordForm from '@/components/home/PasswordForm.vue'

const props = defineProps<{
  isVisible: boolean
}>()

const emit = defineEmits(['update:isVisible'])

const activeKey = ref('1')

const handleCancel = () => {
  emit('update:isVisible', false)
}
</script>
