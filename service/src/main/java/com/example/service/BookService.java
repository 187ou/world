package com.example.service;

import com.example.pojo.dto.BookDto;
import com.example.pojo.vo.ChapterVo;
import com.example.pojo.vo.SearchBookVo;

import java.util.List;

public interface BookService {
    /**
     * 搜索小说
     * @param searchKey
     * @return
     */
    List<SearchBookVo> searchBook(String searchKey);

    /**
     * 搜索小说章节
     * @param bookDto
     * @return
     */
    List<ChapterVo> searchChapters(BookDto bookDto);
}
