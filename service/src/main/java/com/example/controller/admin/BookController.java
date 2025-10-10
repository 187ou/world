package com.example.controller.admin;

import com.example.common.result.UnifyList;
import com.example.pojo.dto.BookDto;
import com.example.pojo.dto.ChapterDto;
import com.example.pojo.vo.ChapterVo;
import com.example.pojo.vo.OpenBookVo;
import com.example.pojo.vo.SearchBookVo;
import com.example.common.result.Result;
import com.example.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小说控制类
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "小说相关接口") // 替换 @Api
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 查询小说
     * @param searchKey
     * @return
     */
    @GetMapping("/search")
    //@Operation(summary = "查询小说") // 替换 @ApiOperation
    public Result<UnifyList<SearchBookVo>> search(@RequestParam String searchKey){
        log.info("查询小说：{}", searchKey);
        List<SearchBookVo> bookList = bookService.searchBook(searchKey);
        log.info("查询完成，找到 {} 本相关书籍", bookList.size());
        return Result.success(new UnifyList<>(bookList.size(), bookList));
    }

    /**
     * 查询小说章节
     * @param bookDto
     * @return
     */
    @PostMapping("/search")
    public Result<UnifyList<ChapterVo>> searchChapters(@RequestBody BookDto bookDto){
        log.info("查询小说章节：{}", bookDto);
        List<ChapterVo> chapterList = bookService.searchChapters(bookDto);
        log.info("查询完成，找到 {} 章", chapterList.size());
        return Result.success(new UnifyList<>(chapterList.size(), chapterList));
    }

    /**
     * 打开小说章节
     * @param chapter
     * @return
     */
    @PostMapping("/open")
    public Result<OpenBookVo> openBook(@RequestBody ChapterDto chapter){
        log.info("打开小说章节：{}", chapter.getChapterLink());
        OpenBookVo openBookVo = bookService.openBook(chapter.getChapterLink());
        log.info("打开完成，返回章节内容");
        return Result.success(openBookVo);
    }
}