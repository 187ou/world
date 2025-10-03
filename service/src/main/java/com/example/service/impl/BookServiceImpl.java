package com.example.service.impl;

import com.example.common.json.JacksonUntil;
import com.example.common.path.PyPath;
import com.example.mapper.BookMapper;
import com.example.pojo.dto.BookDto;
import com.example.pojo.vo.ChapterVo;
import com.example.pojo.vo.SearchBookVo;
import com.example.pyLink.PythonDataParser;
import com.example.pyLink.PythonIntegration;
import com.example.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 小说服务实现类
 */
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private PythonIntegration pythonIntegration;

    /**
     * 查询小说
     * @param searchKey
     * @return
     */
    @Override
    public List<SearchBookVo> searchBook(String searchKey) {
        try {
            log.info("开始搜索书籍: {}", searchKey);

            // 调用Python脚本
            String pythonResult = PythonIntegration.callPythonScript(searchKey, PyPath.SEARCH_BOOK_PY_PATH.getPath());
            log.debug("Python返回原始数据: {}", pythonResult);

            // 解析Python返回的数据
            List<SearchBookVo> bookList = PythonDataParser.parse(pythonResult, SearchBookVo.class);

            log.info("搜索完成，找到 {} 本相关书籍", bookList.size());
            return bookList;

        } catch (Exception e) {
            log.error("搜索书籍失败: {}", e.getMessage(), e);
            throw new RuntimeException("搜索服务暂时不可用", e);
        }
    }

    /**
     * 查询小说章节
     * @param bookDto
     * @return
     */
    @Override
    public List<ChapterVo> searchChapters(BookDto bookDto) {
        try {
            log.info("开始搜索对应小说章节: {}", bookDto.getBookName());
            log.info("书籍链接: {}", bookDto.getBookLink());

            // 1. 转换为JSON
            String jsonString = JacksonUntil.toJsonString(bookDto);
            log.debug("转换前的JSON: {}", jsonString);

            // 2. 对JSON进行URL编码
            String encodedJson = URLEncoder.encode(jsonString, StandardCharsets.UTF_8);
            log.debug("URL编码后的JSON: {}", encodedJson);

            // 3. 调用Python脚本
            String pythonResult = PythonIntegration.callPythonScript(encodedJson, PyPath.SEARCH_CHAPTER_PY_PATH.getPath());
            log.info("Python返回数据长度: {}", pythonResult.length());
            log.debug("Python返回原始数据: {}", pythonResult);

            // 4. 解析Python返回的数据
            List<ChapterVo> chapterVoList = PythonDataParser.parse(pythonResult, ChapterVo.class);

            log.info("搜索完成，找到 {} 章", chapterVoList.size());
            return chapterVoList;

        } catch (Exception e) {
            log.error("搜索章节失败: {}", e.getMessage(), e);
            throw new RuntimeException("搜索服务暂时不可用", e);
        }
    }
}