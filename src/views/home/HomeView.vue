<template>
  <div
    class="flex h-screen overflow-hidden bg-gray-50 text-gray-900 transition-colors duration-300">
    <!-- 左侧导航栏 -->
    <SideBar :navItems="navItems" :secondaryNavItems="secondaryNavItems" @show-personal-center="showPersonalCenterModal" @show-novel-rank="showNovelRankModal" />
    <!-- 主内容区 -->
    <div class="flex-1 flex flex-col overflow-y-auto">
      <!-- 内容滚动区域 -->
      <main class="flex-1 p-6 md:p-8">
        <!-- 模板区域 -->
        <TemplateSection :templates="templates" @show-search-modal="showSearchModal" />
        <!-- 最近文档区域 -->
        <DocumentSection
          :recentDocuments="recentDocuments"
          :starredDocuments="starredDocuments"
          :purchasedDocuments="purchasedDocuments"
        />
      </main>
    </div>
    <SearchModal :isVisible="isSearchModalVisible" :templates="templates" @update:isVisible="isSearchModalVisible = $event" />
    <PersonalCenterModal :isVisible="isPersonalCenterVisible" @update:isVisible="isPersonalCenterVisible = $event" />
    <NovelRankModal :isVisible="isNovelRankModalVisible" @update:isVisible="isNovelRankModalVisible = $event" @select-novel="handleNovelSelected" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import SideBar from '@/layouts/homes/SideBar.vue'
import TemplateSection from '@/layouts/homes/TemplateSection.vue'
import DocumentSection from '@/layouts/homes/DocumentSection.vue'
import SearchModal from '@/layouts/homes/SearchModal.vue'
import PersonalCenterModal from '@/layouts/homes/PersonalCenterModal.vue'
import NovelRankModal from '@/components/home/NovelRankModal.vue'
import type { RankItem } from '@/types/rank'

import {
  FileAddOutlined,
  FolderOpenOutlined,
  SaveOutlined,
  DownloadOutlined,
  ShareAltOutlined,
  HistoryOutlined,
  StarOutlined,
  DeleteOutlined,
  SettingOutlined,
} from '@ant-design/icons-vue'

const images = import.meta.glob('@/assets/home/*.{jpg,jpeg,png,webp}', { eager: true, as: 'url' })

// 导航项数据
const navItems = [
  { icon: FileAddOutlined, label: '新建' },
  { icon: FolderOpenOutlined, label: '打开' },
  { icon: SaveOutlined, label: '保存' },
  { icon: DownloadOutlined, label: '导出' },
  { icon: ShareAltOutlined, label: '共享' },
  { icon: HistoryOutlined, label: '历史版本' },
  { icon: StarOutlined, label: '收藏' },
  { icon: DeleteOutlined, label: '回收站' },
  { icon: SettingOutlined, label: '设置' }
]

// 二级导航数据
const secondaryNavItems = [
  { label: 'man' },
  { label: 'what' },
  { label: 'can' },
  { label: 'I' },
  { label: 'say' },
]

// 模板数据
const templates = [
  {
    name: '空白文档',
    description: '创建一个新的空白文档',
    previewUrl: images['/src/assets/home/first.jpg'],
    avatar: images['/src/assets/home/first.jpg']
  },
  {
    name: '简历模板',
    description: '专业的简历和求职信',
    previewUrl: images['/src/assets/home/second.jpg'],
    avatar: images['/src/assets/home/second.jpg']
  },
  {
    name: '报告模板',
    description: '正式的报告和文档',
    previewUrl: images['/src/assets/home/third.webp'],
    avatar: images['/src/assets/home/third.webp']
  },
  {
    name: '新闻稿',
    description: '专业的新闻稿格式',
    previewUrl: images['/src/assets/home/fourth.jpg'],
    avatar: images['/src/assets/home/fourth.jpg']
  },
  {
    name: '会议记录',
    description: '结构化的会议记录',
    previewUrl: images['/src/assets/home/fifth.jpg'],
    avatar: images['/src/assets/home/fifth.jpg']
  },
  {
    name: '项目计划',
    description: '详细的项目计划模板',
    previewUrl: images['/src/assets/home/sixth.jpg'],
    avatar: images['/src/assets/home/sixth.jpg']
  }
]

// 最近文档数据
const recentDocuments = [
  {
    name: '2023年度市场分析报告.docx',
    path: 'OneDrive > 市场部 > 年度报告',
    modified: '今天 14:30',
    starred: true,
    previewUrl: 'https://search.bilibili.com/all?keyword=%E7%83%9F%E8%8A%B1%E6%98%93%E5%86%B7&from_source=webtop_search&spm_id_from=333.1007&search_source=2'
  },
  {
    name: '产品开发会议纪要.docx',
    path: '本地 > 工作 > 会议记录',
    modified: '昨天 10:15',
    starred: false,
    previewUrl: 'https://www.owlook.com.cn/chapter?url=http://www.60ksw.com/ks/100/100736/&novels_name=斗破苍穹'
  },
  {
    name: '新员工入职指南.docx',
    path: 'SharePoint > 人力资源 > 培训',
    modified: '2023/11/28 09:45',
    starred: true,
    previewUrl: 'https://www.owlook.com.cn/chapter?url=http://www.60ksw.com/ks/100/100736/&novels_name=斗破苍穹'
  },
  {
    name: '客户反馈汇总分析.docx',
    path: 'OneDrive > 客户服务 > 分析',
    modified: '2023/11/25 16:20',
    starred: false,
    previewUrl: 'https://www.owlook.com.cn/chapter?url=http://www.60ksw.com/ks/100/100736/&novels_name=斗破苍穹'
  }
]

// 我的收藏文档数据
const starredDocuments = [
  {
    name: '重要项目计划书.docx',
    path: 'OneDrive > 项目部 > 核心文件',
    modified: '2023/12/01 09:00',
    starred: true,
    previewUrl: 'https://www.owlook.com.cn/chapter?url=http://www.60ksw.com/ks/100/100736/&novels_name=斗破苍穹'
  },
  {
    name: '年度总结报告.pptx',
    path: '本地 > 个人文件 > 报告',
    modified: '2023/11/30 17:00',
    starred: true,
    previewUrl: 'https://www.owlook.com.cn/chapter?url=http://www.60ksw.com/ks/100/100736/&novels_name=斗破苍穹'
  }
]

// 最近购买文档数据
const purchasedDocuments = [
  {
    name: 'Vue.js 3 实战.pdf',
    path: '本地 > 学习 > 前端',
    modified: '今天 09:00',
    previewUrl: 'https://www.owlook.com.cn/chapter?url=http://www.60ksw.com/ks/100/100736/&novels_name=斗破苍穹'
  },
  {
    name: '深入理解 TypeScript.epub',
    path: 'OneDrive > 学习 > 编程',
    modified: '昨天 15:00',
    previewUrl: 'https://www.owlook.com.cn/chapter?url=http://www.60ksw.com/ks/100/100736/&novels_name=斗破苍穹'
  },
  {
    name: '设计模式精讲.mobi',
    path: 'SharePoint > 学习 > 架构',
    modified: '2023/11/29 11:00',
    previewUrl: 'https://www.owlook.com.cn/chapter?url=http://www.60ksw.com/ks/100/100736/&novels_name=斗破苍穹'
  }
]

// 初始化
onMounted(() => {
  // console.log('实验挂载中……')
})

const isSearchModalVisible = ref(false)
const isPersonalCenterVisible = ref(false)
const isNovelRankModalVisible = ref(false)

const showSearchModal = () => {
  isSearchModalVisible.value = true
}

const showPersonalCenterModal = () => {
  isPersonalCenterVisible.value = true
}

const showNovelRankModal = () => {
  isNovelRankModalVisible.value = true
}

const handleNovelSelected = (novel: RankItem) => {
  console.log('Selected Novel:', novel)
  // 在这里可以添加处理选中小说的逻辑，例如跳转到小说详情页或显示小说内容
}
</script>


<style scoped lang="scss">
::-webkit-scrollbar {
  width: 6px;
  height: 6px;

  &-track {
    background: transparent;
  }

  &-thumb {
    background-color: rgba(156, 163, 175, 0.5);
    border-radius: 3px;

    &:hover {
      background-color: rgba(156, 163, 175, 0.8);
    }
  }
}
</style>
