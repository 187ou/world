<template>
  <div class="p-6">
    <h2 class="text-xl font-bold mb-4">接口测试页</h2>

    <!-- 搜索书籍 -->
    <div class="mb-4 flex gap-2">
      <a-input
        v-model:value="keyword"
        placeholder="输入书名关键字"
        style="width: 300px"
        @pressEnter="handleSearchBooks"
      />
      <a-button type="primary" @click="handleSearchBooks" :loading="loadingBooks">
        搜索书籍
      </a-button>
    </div>

    <!-- 显示书籍搜索结果 -->
    <a-list bordered :data-source="bookList" v-if="bookList.length">
      <template #renderItem="{ item }">
        <a-list-item :key="item.bookName">
          <div class="flex justify-between w-full items-center">
            <span>{{ item.bookName }}</span>
            <a-button type="link" @click="handleLoadChapters(item)" :loading="loadingChapters && currentBook?.bookName === item.bookName">
              查看章节
            </a-button>
          </div>
        </a-list-item>
      </template>
    </a-list>

    <!-- 显示章节列表 -->
    <div v-if="chapterList.length" class="mt-6">
      <h3 class="text-lg font-semibold mb-2">《{{ currentBook?.bookName }}》的章节列表：</h3>
      <a-list bordered :data-source="chapterList">
        <template #renderItem="{ item }">
          <a-list-item>
            <a :href="item.url" target="_blank">{{ item.title }}</a>
          </a-list-item>
        </template>
      </a-list>
    </div>

    <a-empty v-if="!bookList.length && !loadingBooks" description="请先搜索书籍" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Input as AInput, Button as AButton, List as AList, Empty as AEmpty } from 'ant-design-vue'
import { searchBooks, searchChapters } from '@/apis/api'
import type { SearchBookVo } from '@/types/book'
import type { Chapter } from '@/types/chapter'

// 注册 ant 组件
const keyword = ref('')
const bookList = ref<SearchBookVo[]>([])
const chapterList = ref<Chapter[]>([])
const currentBook = ref<SearchBookVo | null>(null)

const loadingBooks = ref(false)
const loadingChapters = ref(false)

// 搜索书籍
const handleSearchBooks = async () => {
  if (!keyword.value.trim()) return
  loadingBooks.value = true
  chapterList.value = []
  currentBook.value = null

  try {
    const res = await searchBooks(keyword.value.trim())
    console.log('📚 搜索结果:', res)
    bookList.value = res?.data?.list || []
  } catch (err) {
    console.error('❌ 搜索书籍失败:', err)
    bookList.value = []
  } finally {
    loadingBooks.value = false
  }
}

// 加载章节
const handleLoadChapters = async (book: SearchBookVo) => {
  loadingChapters.value = true
  currentBook.value = book
  chapterList.value = []

  try {
    const res = await searchChapters(book)
    console.log('📖 章节结果:', res)
    chapterList.value = res?.data?.list || []
  } catch (err) {
    console.error('❌ 加载章节失败:', err)
    chapterList.value = []
  } finally {
    loadingChapters.value = false
  }
}
</script>

<style scoped>
.p-6 {
  padding: 24px;
}
</style>
