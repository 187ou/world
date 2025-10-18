<template>
  <div class="membership-center p-6 max-w-6xl mx-auto">
    <div class="current-level-card bg-gradient-to-r from-blue-50 to-blue-100 rounded-2xl p-6 mb-8 shadow-sm">
      <div class="flex flex-col md:flex-row items-center justify-between">
        <div class="text-center md:text-left mb-4 md:mb-0">
          <h3 class="text-2xl font-bold text-gray-800">当前会员等级</h3>
          <p class="text-blue-600 mt-1">{{ getCurrentLevelInfo().description }}</p>
        </div>
        <div class="flex items-center">
          <div
            :class="[
              'level-badge w-20 h-20 rounded-full flex items-center justify-center font-bold text-2xl shadow-sm',
              getCurrentLevelInfo().color
            ]"
          >
            <span :class="getCurrentLevelInfo().textColor">{{ getCurrentLevelInfo().id }}</span>
          </div>
          <div class="ml-4">
            <h2 class="text-3xl font-bold text-gray-800">{{ getCurrentLevelInfo().name }}</h2>
            <p class="text-gray-600 mt-1">当前积分: {{ currentPoints }}分</p>
          </div>
        </div>
      </div>
    </div>

    <div class="all-levels">
      <h3 class="text-2xl font-bold text-gray-800 mb-6 text-center">会员等级体系</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div
          v-for="(level, index) in membershipLevels"
          :key="level.id"
          :class="[
            'level-card rounded-2xl p-5 transition-all duration-300 transform hover:-translate-y-1 bg-white',
            currentLevel === level.id
              ? 'border-2 border-blue-100 shadow-sm'
              : 'border border-gray-100'
          ]"
        >
          <div class="flex justify-between items-start mb-4">
            <div
              :class="[
                'level-icon w-10 h-10 rounded-full flex items-center justify-center font-bold shadow-sm',
                level.color
              ]"
            >
              <span :class="level.textColor">{{ level.id }}</span>
            </div>
            <span
              v-if="currentLevel === level.id"
              class="px-2 py-1 bg-blue-50 text-blue-600 text-xs font-semibold rounded-full"
            >
              当前等级
            </span>
          </div>

          <h4 class="font-bold text-xl mb-2 text-gray-800">{{ level.name }}</h4>
          <p class="text-gray-600 text-sm mb-4">{{ level.description }}</p>

          <div class="benefits-list mb-4">
            <h5 class="font-semibold text-gray-700 mb-2">专属权益</h5>
            <ul class="space-y-2">
              <li
                v-for="(benefit, index) in level.benefits"
                :key="index"
                class="flex items-start text-sm"
              >
                <span class="text-green-500 mr-2 mt-0.5">✓</span>
                <span class="text-gray-700">{{ benefit }}</span>
              </li>
            </ul>
          </div>

          <div class="mt-4 pt-4 border-t border-gray-100">
            <div class="flex justify-between text-xs text-gray-500 mb-1">
              <span>等级要求</span>
              <span>{{ index * 25 }}积分</span>
            </div>
            <div class="w-full bg-gray-100 rounded-full h-2">
              <div
                :class="[
                  'h-2 rounded-full',
                  level.color
                ]"
                :style="{ width: ((index + 1) * 25) + '%' }"
              ></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface MembershipLevel {
  id: number
  name: string
  description: string
  benefits: string[]
  color: string
  textColor: string
}

const currentLevel = ref<number>(1)
// 新增当前积分数据
const currentPoints = ref<number>(68)

const membershipLevels: MembershipLevel[] = [
  {
    id: 1,
    name: '普通会员',
    description: '基础会员权益',
    benefits: ['基础功能使用', '每日签到奖励', '社区互动'],
    color: 'bg-blue-100',
    textColor: 'text-blue-700'
  },
  {
    id: 2,
    name: '黄金会员',
    description: '进阶会员权益',
    benefits: ['普通会员所有权益', '专属模板下载', '优先客服支持', '月度礼包'],
    color: 'bg-amber-100',
    textColor: 'text-amber-700'
  },
  {
    id: 3,
    name: '钻石会员',
    description: '高级会员权益',
    benefits: ['黄金会员所有权益', '免费预览权限', '定制化服务', '季度大礼包'],
    color: 'bg-cyan-100',
    textColor: 'text-cyan-700'
  },
  {
    id: 4,
    name: '至尊会员',
    description: '顶级会员权益',
    benefits: ['钻石会员所有权益', '专属客服', '内测功能优先体验', '年度豪华礼包'],
    color: 'bg-purple-100',
    textColor: 'text-purple-700'
  }
]

const getCurrentLevelInfo = () => {
  return membershipLevels.find(level => level.id === currentLevel.value) || membershipLevels[0]
}
</script>

<style scoped lang="scss">
.membership-center {
  .current-level-card {
    background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
  }

  .level-card {
    &:hover {
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
    }
  }
}
</style>
