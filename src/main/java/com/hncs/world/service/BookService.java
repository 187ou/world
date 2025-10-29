package com.hncs.world.service;

import com.hncs.world.pojo.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hncs.world.pojo.vo.BookVo;
import com.hncs.world.pojo.vo.BookOpenVo;
import com.hncs.world.pojo.vo.BookPreviewVo;
import com.hncs.world.pojo.vo.BookSearchVo;

import java.util.List;

/**
* @author 24774
* @description 针对表【book(书籍信息表)】的数据库操作Service
* @createDate 2025-10-16 10:18:38
*/
public interface BookService extends IService<Book> {

    /**
     * 利用py脚本搜索书籍
     *
     * @param bookName 小说关键词
     * @return 搜索结果
     */
    List<BookSearchVo> bookSearch(String bookName);

    /**
     * 利用py脚本获取小说预览
     *
     * @param bookLink 小说链接
     * @return 预览结果
     */
    List<BookPreviewVo> bookPreview(String bookLink);

    /**
     * 利用py脚本获取小说内容
     *
     * @param bookLink 小说链接
     * @return 小说内容
     */
    BookOpenVo bookOpen(String bookLink);

    /**
     * 获取用户收藏的书籍
     *
     * @param userId 用户id
     * @return 收藏结果
     */
    List<BookVo> bookCollect(Long userId);

    /**
     * 获取用户最近阅读的书籍
     *
     * @param userId 用户id
     * @return 最近阅读结果
     */
    List<BookVo> bookRecently(Long userId);
}
