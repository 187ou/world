<template>
  <aside
    class="w-16 md:w-45 bg-gradient-to-b from-white to-yellow-50/50 border-r border-gray-200 flex flex-col transition-all duration-300 z-10">
    <div class="flex items-center justify-center md:justify-start p-4 border-b border-gray-200">
      <div class="w-15 h-15 flex-shrink-0">
        <img src="@/assets/home/word.png" class="w-full h-full object-cover" alt="">
      </div>
      <h1 class="ml-3 text-lg font-semibold hidden md:block">World</h1>
    </div>

    <!-- 导航菜单 -->
    <nav class="flex-1 py-4">
      <a-menu mode="inline" :inline-collapsed="false" class="!bg-transparent !border-0">
        <a-menu-item v-for="(item, index) in navItems" :key="index"
                     class="py-2 px-4 rounded-lg hover:bg-blue-100 group">
          <a
            @click="handleNavClick(item.label)"
            href="#"
            class="flex items-center justify-center md:justify-start text-gray-600 hover:text-blue-600 transition-all duration-300 relative z-10"
          >
            <component :is="item.icon" class="text-xl"/>
            <span class="ml-3 hidden md:block">{{ item.label }}</span>
          </a>
          <span
            class="absolute left-0 top-0 h-full w-1 bg-blue-600 transform scale-y-0 group-hover:scale-y-100 transition-transform duration-300 origin-center"></span>
        </a-menu-item>

        <!-- 新增二级导航 -->
        <a-sub-menu key="sub-menu-secondary" class="!bg-transparent !border-0">
          <template #title>
            <span class="flex items-center justify-center md:justify-start text-gray-600 transition-all duration-300">
              <UserOutlined class="text-xl"/>
              <span class="ml-3 hidden md:block">友情赞助</span>
            </span>
          </template>
          <a-menu-item v-for="(item, index) in secondaryNavItems" :key="`sub-item-${index}`"
                       class="py-2 px-4 rounded-lg hover:bg-blue-100 group">
            <a class="flex items-center justify-center md:justify-start text-gray-600 hover:text-blue-600 transition-all duration-300 relative z-10">
              <span class="ml-3 hidden md:block">{{ item.label }}</span>
            </a>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </nav>

    <!-- 用户信息 -->
    <div class="p-4 border-t border-gray-200">
      <div class="flex items-center cursor-pointer group" @click="showPersonalCenter">
        <div
          class="w-8 h-8 rounded-full overflow-hidden border-2 border-transparent group-hover:border-blue-500 transition-all">
          <img src="@/assets/home/person.png" alt="用户头像"
               class="w-full h-full object-cover">
        </div>
        <span class="ml-2 text-sm font-medium hidden md:block">User</span>
        <i class="fa fa-chevron-down text-xs ml-1 text-gray-500 hidden md:block"></i>
      </div>
    </div>
  </aside>
</template>

<script setup lang="ts">
import {
  Menu as AMenu,
  MenuItem as AMenuItem,
  SubMenu as ASubMenu,
} from 'ant-design-vue'
import { UserOutlined } from '@ant-design/icons-vue'
import type { Component } from 'vue'

// 定义导航项的接口
interface NavItem {
  label: string;
  icon?: Component;
}

defineProps<{
  navItems: NavItem[];
  secondaryNavItems: NavItem[];
}>()

const emit = defineEmits(['show-personal-center', 'show-novel-rank'])

const showPersonalCenter = () => {
  emit('show-personal-center')
}

const handleNavClick = (label: string) => {
  if (label === '历史版本') {
    console.log('点击了历史版本')
    emit('show-novel-rank')
  }
}
</script>

