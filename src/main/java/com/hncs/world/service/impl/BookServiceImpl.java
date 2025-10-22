package com.hncs.world.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hncs.world.common.ErrorCode;
import com.hncs.world.exception.BusinessException;
import com.hncs.world.pojo.entity.Book;
import com.hncs.world.pojo.vo.BookOpenVo;
import com.hncs.world.pojo.vo.BookPreviewVo;
import com.hncs.world.pojo.vo.BookSearchVo;
import com.hncs.world.pyLink.PyPath;
import com.hncs.world.pyLink.PythonDataParser;
import com.hncs.world.pyLink.PythonIntegration;
import com.hncs.world.service.BookService;
import com.hncs.world.mapper.BookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
* @author 24774
* @description 针对表【book(书籍信息表)】的数据库操作Service实现
* @createDate 2025-10-16 10:18:38
*/
@Service
@Slf4j
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

    @Override
    public List<BookSearchVo> bookSearch(String bookName) {
        try {
            log.info("开始搜索书籍: {}", bookName);

            // 调用Python脚本
            String pythonResult = PythonIntegration.callPythonScript(bookName, PyPath.SEARCH_BOOK_PY_PATH.getPath());

            // 解析Python返回的数据
            List<BookSearchVo> bookList = PythonDataParser.parse(pythonResult, BookSearchVo.class);

            log.info("搜索完成，找到 {} 本相关书籍", bookList.size());
            return bookList;

        } catch (Exception e) {
            log.error("搜索书籍失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.PY_EXECUTE_FAILED, "小说搜索失败");
        }
    }

    @Override
    public List<BookPreviewVo> bookPreview(String bookLink) {
        try {
            log.info("开始预览小说: {}", bookLink);

            //对小说链接进行处理
            String encodedJson = URLEncoder.encode(bookLink, String.valueOf(StandardCharsets.UTF_8));

            // 调用Python脚本
            String pythonResult = PythonIntegration.callPythonScript(encodedJson, PyPath.SEARCH_CHAPTER_PY_PATH.getPath());

            // 解析Python返回的数据
            List<BookPreviewVo> bookPreviewVoList = PythonDataParser.parse(pythonResult, BookPreviewVo.class);

            log.info("搜索完成，找到 {} 小说章节", bookPreviewVoList.size());
            return bookPreviewVoList;

        } catch (Exception e) {
            log.error("预览小说失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.PY_EXECUTE_FAILED, "小说预览失败");
        }
    }

    @Override
    public BookOpenVo bookOpen(String chapterLink) {
        try {
            log.info("开始打开小说: {}", chapterLink);

            // 调用Python脚本
            String pythonResult = PythonIntegration.callPythonScript(chapterLink, PyPath.OBTAIN_CHAPTER_TEXT_PY_PATH.getPath());

            // 解析Python返回的数据
            BookOpenVo bookOpenVo = PythonDataParser.parseSingle(pythonResult, BookOpenVo.class);

            log.info("搜索完成，本章节字数为：{}", bookOpenVo.getChapterTxtSize());
            return bookOpenVo;

        } catch (Exception e) {
            log.error("打开小说失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.PY_EXECUTE_FAILED, "小说打开失败");
        }
    }
}




