<template>
  <div
    class="flex h-screen overflow-hidden bg-gray-50 text-gray-900 transition-colors duration-300">
    <!-- 左侧导航栏 -->
    <SideBar :navItems="navItems" :secondaryNavItems="secondaryNavItems"
             @show-personal-center="showPersonalCenterModal"
             @show-novel-rank="showNovelRankModal" />
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
          :on-recent-tab-click="fetchRecentlyReadNovels"
          :on-starred-tab-click="fetchCollectedNovels"
          :on-purchased-tab-click="fetchPurchasedNovels"
        />
      </main>
    </div>
    <SearchModal :isVisible="isSearchModalVisible" :templates="templates"
                 @update:isVisible="isSearchModalVisible = $event" />
    <PersonalCenterModal :isVisible="isPersonalCenterVisible"
                         @update:isVisible="isPersonalCenterVisible = $event" />
    <NovelRankModal :isVisible="isNovelRankModalVisible"
                    @update:isVisible="isNovelRankModalVisible = $event"
                    @select-novel="handleNovelSelected" />
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
  getBookCollection,
  getPurchasedBooks,
  getRecentlyReadBooks,
} from '@/apis/modules/bookApi.ts'

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
  { icon: SettingOutlined, label: '设置' },
]

// 二级导航数据
const secondaryNavItems = [
  { label: 'man' },
  { label: 'what' },
  { label: 'can' },
  { label: 'I' },
  { label: 'say' },
]

const images = import.meta.glob('@/assets/home/*.{jpg,jpeg,png,webp}', { eager: true, as: 'url' })

// 模板数据
const templates = [
  {
    name: '空白文档',
    description: '创建一个新的空白文档',
    previewUrl: images['/src/assets/home/first.jpg'],
    avatar: images['/src/assets/home/first.jpg'],
  },
  {
    name: '简历模板',
    description: '专业的简历和求职信',
    previewUrl: images['/src/assets/home/second.jpg'],
    avatar: images['/src/assets/home/second.jpg'],
  },
  {
    name: '报告模板',
    description: '正式的报告和文档',
    previewUrl: images['/src/assets/home/third.webp'],
    avatar: images['/src/assets/home/third.webp'],
  },
  {
    name: '新闻稿',
    description: '专业的新闻稿格式',
    previewUrl: images['/src/assets/home/fourth.jpg'],
    avatar: images['/src/assets/home/fourth.jpg'],
  },
  {
    name: '会议记录',
    description: '结构化的会议记录',
    previewUrl: images['/src/assets/home/fifth.jpg'],
    avatar: images['/src/assets/home/fifth.jpg'],
  },
  {
    name: '项目计划',
    description: '详细的项目计划模板',
    previewUrl: images['/src/assets/home/sixth.jpg'],
    avatar: images['/src/assets/home/sixth.jpg'],
  },
]

// 最近文档数据
const recentDocuments = ref([])
const fetchRecentlyReadNovels = async () => {
  try {
    const response = await getRecentlyReadBooks()
    console.log('获取最近阅读小说成功:', response.data)
    if (response.data && response.data.list) {
      recentDocuments.value = response.data.list.map((item: unknown) => {
        const bookItem = item as { bookName?: string; bookLink?: string; updateTime?: string }
        return {
          name: bookItem.bookName || '未知小说',
          path: '本地 > 阅读历史 > 小说',
          modified: bookItem.updateTime || '',
          starred: false,
          previewUrl: bookItem.bookLink || '',
        }
      })
    }
  } catch (error) {
    console.error('获取最近阅读小说失败:', error)
  }
}

// 我的收藏文档数据
const starredDocuments = ref([])
const fetchCollectedNovels = async () => {
  try {
    const response = await getBookCollection()
    console.log('获取收藏小说成功:', response.data)
    if (response.data && response.data.list) {
      starredDocuments.value = response.data.list.map((item: unknown) => {
        const bookItem = item as { bookName?: string; bookLink?: string; updateTime?: string }
        return {
          name: bookItem.bookName || '未知小说',
          path: '本地 > 个人文件 > 报告',
          modified: bookItem.updateTime || '',
          starred: true,
          previewUrl: bookItem.bookLink || '',
        }
      })
    }
  } catch (error) {
    console.error('获取收藏小说失败:', error)
  }
}

// 最近购买文档数据
const purchasedDocuments = ref([])
const fetchPurchasedNovels = async () => {
  try {
    const response = await getPurchasedBooks()
    console.log('获取已购买小说成功:', response.data)
    if (response.data && response.data.list) {
      purchasedDocuments.value = response.data.list.map((item: unknown) => {
        const bookItem = item as { bookName?: string; bookLink?: string; updateTime?: string }
        return {
          name: bookItem.bookName || '未知小说',
          path: '本地 > 学习 > Vue3',
          modified: bookItem.updateTime || '',
          previewUrl: bookItem.bookLink || '',
        }
      })
    }
  } catch (error) {
    console.error('获取已购买小说失败:', error)
  }
}

// 初始化
onMounted(() => {
  fetchRecentlyReadNovels()
  fetchCollectedNovels()
  fetchPurchasedNovels()
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
