<template>
  <button
    @click="handleSendCode"
    :disabled="isSendingCode || !email"
    class="text-sm text-[#8c7569] disabled:opacity-50"
  >
    {{ isSendingCode ? '发送中...' : '发送验证码' }}
  </button>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { registerCode, sendVerificationCode, sendEmailBindCode } from '@/apis/api.ts'
import { message } from 'ant-design-vue'
import { formFilters } from '@/filters/formFilters'
import { useUserStore } from '@/stores/user.ts'

const useStore = useUserStore()

const props = defineProps<{
  email: string
  mode: number
}>()

const emit = defineEmits<{
  sendSuccess: []
  sendError: [error: unknown]
}>()

const isSendingCode = ref(false)

const handleSendCode = async () => {
  const emailResult = formFilters.validateEmail(props.email)
  if (!emailResult.isValid) {
    message.error(emailResult.message || '请输入有效的邮箱地址')
    return
  }

  isSendingCode.value = true
  try {
    let res
    if (props.mode === 0) {
      res = await sendVerificationCode({ email: props.email })
    } else if (props.mode === 1) {
      res = await registerCode(props.email)
    } else {
      const userId = useStore.user?.id
      if (userId === undefined) {
        message.error('用户未登录或用户ID不存在')
        emit('sendError', new Error('用户未登录'))
        isSendingCode.value = false
        return
      }
      res = await sendEmailBindCode({ newEmail: props.email, userId: userId })
    }

    console.log('发送验证码到邮箱:', res)
    message.success('验证码已发送')
    emit('sendSuccess')
  } catch (error) {
    console.error('发送验证码失败:', error)
    message.error('发送验证码失败，请重试')
    emit('sendError', error)
  } finally {
    isSendingCode.value = false
  }
}
</script>
