package com.hncs.world.controller;

import com.hncs.world.common.ErrorCode;
import com.hncs.world.common.Result;
import com.hncs.world.common.UnifyList;
import com.hncs.world.exception.BusinessException;
import com.hncs.world.pojo.dto.BookOpenDto;
import com.hncs.world.pojo.dto.BookPreviewDto;
import com.hncs.world.pojo.vo.BookOpenVo;
import com.hncs.world.pojo.vo.BookPreviewVo;
import com.hncs.world.pojo.vo.BookSearchVo;
import com.hncs.world.service.impl.BookServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/book")
@Slf4j
@Api(tags = "小说相关接口")
public class BookController {
    @Resource
    private BookServiceImpl bookServiceImpl;

    @GetMapping("/search")
    @ApiOperation("小说搜索接口")
    public Result<UnifyList<BookSearchVo>> bookSearch(String bookName) {
        if(bookName == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "bookName is empty");
        }
        List<BookSearchVo> bookSearchVoList = bookServiceImpl.bookSearch(bookName);
        return Result.success(new UnifyList<>(bookSearchVoList.size(), bookSearchVoList));
    }

    @PostMapping("/preview")
    @ApiOperation("小说预览接口")
    public Result<UnifyList<BookPreviewVo>> bookPreview(@RequestBody BookPreviewDto bookPreviewDto) {
        if(bookPreviewDto == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "bookLink is empty");
        }
        List<BookPreviewVo> bookPreviewVoList = bookServiceImpl.bookPreview(bookPreviewDto.getBookLink());
        return Result.success(new UnifyList<>(bookPreviewVoList.size(), bookPreviewVoList));
    }

    @PostMapping("/open")
    @ApiOperation("小说阅读接口")
    public Result<BookOpenVo> bookOpen(@RequestBody BookOpenDto bookOpenDto) {
        if(bookOpenDto == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "ChapterLink is empty");
        }
        BookOpenVo result = bookServiceImpl.bookOpen(bookOpenDto.getChapterLink());
        return Result.success(result);
    }
}
