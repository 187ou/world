import http from '@/utils/http.ts';
import type { SearchBookVo } from '@/types/book.ts';

/**
 * 搜索小说
 * @param bookName 搜索关键词
 * @returns 后端直接返回的响应数据（包含code、message、data，其中data是{total, list}结构）
 */
export const searchBooks = async (bookName: string) => {
  return await http.get('/api/book/search', {
    params: { bookName }
  });
};

/**
 * 搜索小说章节
 * @param bookDto 搜索条件对象，包含bookName和bookLink
 * @returns 后端返回的章节列表数据（包含code、message、data，其中data是{total, list}结构）
 */
export const searchChapters = async (bookDto: SearchBookVo) => {
  return await http.post('/api/book/preview', bookDto);
};

/**
 * 打开小说章节
 * @param chapterLink 章节链接
 * @returns 后端返回的章节内容数据（包含code、message、data，其中data是OpenBookVo结构）
 */
export const openBook = async (chapterLink: string) => {
  return await http.post('/api/book/open', { chapterLink });
};

/**
 * 获取用户小说收藏列表
 * @returns 收藏的小说列表数据（包含code、message、data，其中data是{total, list}结构）
 */
export const getBookCollection = async () => {
  return await http.get('/api/book/collect');
};

/**
 * 获取用户已购买的小说列表
 * @returns 已购买的小说列表数据（包含code、message、data，其中data是{total, list}结构）
 */
export const getPurchasedBooks = async () => {
  return await http.get('/api/book/purchased');
};

/**
 * 获取用户最近阅读的小说列表
 * @returns 最近阅读的小说列表数据（包含code、message、data，其中data是{total, list}结构）
 */
export const getRecentlyReadBooks = async () => {
  return await http.get('/api/book/recently');
};

/**
 * 添加小说收藏
 * @param userCollectBookDto 包含小说信息的对象
 * @returns 添加结果数据
 */
export const addBookCollection = async (userCollectBookDto: SearchBookVo) => {
  return await http.post('/api/book/collect/add', userCollectBookDto);
};

/**
 * 删除小说收藏
 * @param userCollectBookDto 包含小说信息的对象
 * @returns 删除结果数据
 */
export const removeBookCollection = async (userCollectBookDto: SearchBookVo) => {
  return await http.post('/api/book/collect/remove', userCollectBookDto);
};

/**
 * 购买小说
 * @param userPurchasedBookDto 包含小说信息的对象
 * @returns 购买结果数据，包含用户剩余金额
 */
export const purchaseBook = async (userPurchasedBookDto: { bookName: string; bookLink: string; cost: number }) => {
  return await http.post('/api/book/purchased/add', userPurchasedBookDto);
};

/**
 * 获取小说阅读记录
 * @param readRecordDto 包含小说名称的对象
 * @returns 小说阅读记录数据
 */
export const getBookReadRecord = async (readRecordDto: { bookName: string }) => {
  return await http.post('/api/book/read-record', readRecordDto);
};

/**
 * 保存小说阅读记录
 * @param saveRecordDto 包含小说阅读记录信息的对象
 * @returns 保存结果数据
 */
export const saveBookReadRecord = async (saveRecordDto: { bookName: string; chapterName?: string; chapterLink?: string }) => {
  return await http.post('/api/book/saver-record', saveRecordDto);
};







