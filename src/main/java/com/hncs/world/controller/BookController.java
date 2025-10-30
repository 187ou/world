package com.hncs.world.controller;

import com.hncs.world.common.ErrorCode;
import com.hncs.world.common.Result;
import com.hncs.world.common.UnifyList;
import com.hncs.world.exception.BusinessException;
import com.hncs.world.pojo.dto.*;
import com.hncs.world.pojo.vo.*;
import com.hncs.world.service.impl.BookServiceImpl;
import com.hncs.world.service.impl.PurchasedServiceImpl;
import com.hncs.world.service.impl.ReadServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/book")
@Slf4j
@Api(tags = "小说相关接口")
public class BookController {
    @Resource
    private BookServiceImpl bookServiceImpl;

    @Resource
    private PurchasedServiceImpl purchasedServiceImpl;

    @Autowired
    private ReadServiceImpl readServiceImpl;
    /**
     * 小说搜索接口
     * @param bookName 小说关键词
     * @return 小说列表
     */
    @GetMapping("/search")
    @ApiOperation("小说搜索接口")
    public Result<UnifyList<BookSearchVo>> bookSearch(String bookName) {
        if(bookName == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "bookName is empty");
        }
        List<BookSearchVo> bookSearchVoList = bookServiceImpl.bookSearch(bookName);
        return Result.success(new UnifyList<>(bookSearchVoList.size(), bookSearchVoList));
    }

    /**
     * 小说预览接口
     * @param bookPreviewDto 小说链接
     * @return 小说预览列表
     */
    @PostMapping("/preview")
    @ApiOperation("小说预览接口")
    public Result<UnifyList<BookPreviewVo>> bookPreview(@RequestBody BookPreviewDto bookPreviewDto) {
        if(bookPreviewDto == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "bookLink is empty");
        }
        List<BookPreviewVo> bookPreviewVoList = bookServiceImpl.bookPreview(bookPreviewDto.getBookLink());
        return Result.success(new UnifyList<>(bookPreviewVoList.size(), bookPreviewVoList));
    }

    /**
     * 小说阅读接口
     * @param bookOpenDto 小说章节链接
     * @return 小说章节内容
     */
    @PostMapping("/open")
    @ApiOperation("小说阅读接口")
    public Result<BookOpenVo> bookOpen(@RequestBody BookOpenDto bookOpenDto) {
        if(bookOpenDto == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "ChapterLink is empty");
        }
        BookOpenVo result = bookServiceImpl.bookOpen(bookOpenDto.getChapterLink());
        return Result.success(result);
    }

    /**
     * 获取小说收藏接口
     * @param request 请求
     * @return 小说收藏列表
     */
    @GetMapping("/collect")
    @ApiOperation("获取小说收藏接口")
    public Result<UnifyList<BookVo>> bookCollect(HttpServletRequest request) {
        // 通过request获取用户ID
        Long userId = (Long) request.getAttribute("userId");
        List<BookVo> bookVoList = bookServiceImpl.bookCollect(userId);

        if(bookVoList == null){
            return Result.success(new UnifyList<>(0, null), "用户没有收藏任何小说");
        }

        return Result.success(new UnifyList<>(bookVoList.size(), bookVoList));
    }

    /**
     * 获取已购小说接口
     * @param request 请求
     * @return 已购小说列表
     */
    @GetMapping("/purchased")
    @ApiOperation("获取已购小说接口")
    public Result<UnifyList<BookVo>> bookPurchased(HttpServletRequest request) {
        // 通过request获取用户ID
        Long userId = (Long) request.getAttribute("userId");
        List<BookVo> bookVoList = purchasedServiceImpl.bookPurchased(userId);

        if(bookVoList == null){
            return Result.success(new UnifyList<>(0, null), "用户没有购买任何小说");
        }

        return Result.success(new UnifyList<>(bookVoList.size(), bookVoList));
    }

    /**
     * 获取最近阅读小说接口
     * @param request 请求
     * @return 最近阅读小说列表
     */
    @GetMapping("/recently")
    @ApiOperation("获取最近阅读小说接口")
    public Result<UnifyList<BookVo>> bookRecently(HttpServletRequest request){
        // 通过request获取用户ID
        Long userId = (Long) request.getAttribute("userId");
        List<BookVo> bookVoList = bookServiceImpl.bookRecently(userId);

        if(bookVoList == null){
            return Result.success(new UnifyList<>(0, null), "用户没有阅读任何小说");
        }

        return Result.success(new UnifyList<>(bookVoList.size(), bookVoList));
    }

    @PostMapping("/collect/add")
    @ApiOperation("添加小说收藏接口")
    public Result<Void> bookCollectAdd(@RequestBody UserCollectBookDto userCollectBookDto, HttpServletRequest request){
        if(userCollectBookDto == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "book is empty");
        }

        // 通过request获取用户ID
        Long userId = (Long) request.getAttribute("userId");

        bookServiceImpl.bookCollectAdd(userId,userCollectBookDto);

        return Result.success();
    }

    @PostMapping("/collect/remove")
    @ApiOperation("删除小说收藏接口")
    public Result<Void> bookCollectRemove(@RequestBody UserCollectBookDto userCollectBookDto, HttpServletRequest request){
        if(userCollectBookDto == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "book is empty");
        }
        // 通过request获取用户ID
        Long userId = (Long) request.getAttribute("userId");

        bookServiceImpl.bookCollectRemove(userId,userCollectBookDto);

        return Result.success();
    }

    /**
     * 添加已购小说接口
     * @param userPurchasedBookDto 要购买的小说信息
     * @param request 请求
     * @return 用户剩余金额
     */
    @PostMapping("/purchased/add")
    @ApiOperation("添加购买小说接口")
    public Result<Integer> bookPurchasedAdd(@RequestBody UserPurchasedBookDto userPurchasedBookDto, HttpServletRequest request){
        if(userPurchasedBookDto == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "book is empty");
        }

        // 通过request获取用户ID
        Long userId = (Long) request.getAttribute("userId");

        Integer balance = purchasedServiceImpl.bookPurchasedAdd(userId,userPurchasedBookDto);

        return Result.success(1);
    }

    @PostMapping("/read-record")
    @ApiOperation("获取小说阅读记录")
    public Result<ReadRecordVo> bookReadRecord(@Valid @RequestBody ReadRecordDto readRecordDto, HttpServletRequest request){
        // 通过request获取用户ID
        Long userId = (Long) request.getAttribute("userId");

        String bookName =readRecordDto.getBookName();

        String token=userId+"_"+bookName;

        ReadRecordVo readRecordVo= readServiceImpl.getBookReadRecord(token);

        if(readRecordVo==null){
            log.info("用户没有阅读这本小说，用户ID={}", userId);
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "用户未阅读过该小说");
        }

        return Result.success(readRecordVo);
    }

    @PostMapping("/saver-record")
    @ApiOperation("保存小说阅读记录")
    public Result<Integer> saveReadRecord(@Valid @RequestBody SaveRecordDto saveRecordDto, HttpServletRequest request){
        // 通过request获取用户ID
        Long userId = (Long) request.getAttribute("userId");

        String bookName =saveRecordDto.getBookName();

        String token=userId+"_"+bookName;

        Integer code= readServiceImpl.saveBookReadRecord(token,saveRecordDto);

        if(code==0){
            log.info("保存记录失败，用户ID={}", userId);
            throw new BusinessException(ErrorCode.SERVER_ERROR, "保存记录失败");
        }
        log.info("保存记录成功，用户ID={}", userId);
        return Result.success(1);
    }
}
