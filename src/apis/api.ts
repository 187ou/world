import http from '@/utils/http.ts';
import type { SearchBookVo } from '@/types/book.ts';

/**
 * 搜索小说
 * @param searchKey 搜索关键词
 * @returns 后端直接返回的响应数据（包含code、message、data，其中data是{total, list}结构）
 */
export const searchBooks = async (searchKey: string) => {
  return await http.get('/admin/search', {
    params: { searchKey }
  });
};

/**
 * 搜索小说章节
 * @param bookDto 搜索条件对象，包含bookName和bookLink
 * @returns 后端返回的章节列表数据（包含code、message、data，其中data是{total, list}结构）
 */
export const searchChapters = async (bookDto: SearchBookVo) => {
  return await http.post('/admin/search', bookDto);
};

/**
 * 打开小说章节
 * @param chapterLink 章节链接
 * @returns 后端返回的章节内容数据（包含code、message、data，其中data是OpenBookVo结构）
 */
export const openBook = async (chapterLink: string) => {
  return await http.post('/admin/open', { chapterLink });
};


