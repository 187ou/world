<template>
  <a-form
    :model="formState"
    :label-col="{ span: 4 }"
    :wrapper-col="{ span: 16 }"
    @finish="onFinish"
  >
    <a-form-item
      label="新邮箱"
      name="newEmail"
      :rules="[{ required: true, message: '请输入新邮箱!' }, { type: 'email', message: '请输入有效的邮箱地址!' }]"
    >
      <div class="flex items-center gap-2">
        <a-input
          v-model:value="formState.newEmail"
          placeholder="请输入新邮箱"
          @blur="validateEmail"
        />
        <VerificationCode
          :email="formState.newEmail"
          :mode="2"
          @send-success="onSendSuccess"
          @send-error="onSendError"
        />
      </div>
      <div v-if="errors.newEmail" class="text-red-500 text-sm">{{ errors.newEmail }}</div>
    </a-form-item>

    <a-form-item
      label="验证码"
      name="code"
      :rules="[{ required: true, message: '请输入验证码!' }]"
    >
      <a-input
        v-model:value="formState.code"
        placeholder="请输入验证码"
      />
    </a-form-item>

    <a-form-item :wrapper-col="{ offset: 4, span: 16 }" style="text-align: center;">
      <a-button type="primary" html-type="submit" :loading="submitting">
        确认修改
      </a-button>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { updateEmail } from '@/apis/api'
import { formFilters } from '@/filters/formFilters.ts'
import VerificationCode from '@/components/logins/VerificationCode.vue'

interface FormState {
  newEmail: string
  code: string
}

const formState = reactive<FormState>({
  newEmail: '',
  code: ''
})

const errors = reactive({
  newEmail: ''
})

const submitting = ref(false)

// 邮箱验证
const validateEmail = () => {
  const result = formFilters.validateEmail(formState.newEmail)
  errors.newEmail = result.message
  return result.isValid
}

const onSendSuccess = () => {
  message.success('验证码已发送至您的新邮箱')
}

const onSendError = (error: unknown) => {
  console.error(error)
  message.error('发送验证码失败')
}

const onFinish = async (values: FormState) => {
  if (!validateEmail()) {
    message.error('请输入有效的邮箱地址')
    return
  }

  try {
    submitting.value = true
    const updateData = {
      newEmail: values.newEmail,
      code: values.code
    }

    const response = await updateEmail(updateData)

    if (response.data) {
      message.success('邮箱修改成功')
      formState.newEmail = ''
      formState.code = ''
    } else {
      message.error(response.data.message || '修改失败')
    }
  } catch (error) {
    console.error(error)
    message.error('修改邮箱时发生错误')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped lang="scss">
.text-red-500 {
  color: #ff4d4f;
}

.text-sm {
  font-size: 12px;
  line-height: 1.5;
  margin-top: 4px;
}
</style>
