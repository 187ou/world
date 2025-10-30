package com.hncs.world.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hncs.world.common.ErrorCode;
import com.hncs.world.exception.BusinessException;
import com.hncs.world.pojo.dto.UserCollectBookDto;
import com.hncs.world.pojo.dto.UserPurchasedBookDto;
import com.hncs.world.pojo.entity.Book;
import com.hncs.world.pojo.entity.User;
import com.hncs.world.pojo.vo.BookVo;
import com.hncs.world.pojo.vo.BookOpenVo;
import com.hncs.world.pojo.vo.BookPreviewVo;
import com.hncs.world.pojo.vo.BookSearchVo;
import com.hncs.world.pyLink.PyPath;
import com.hncs.world.pyLink.PythonDataParser;
import com.hncs.world.pyLink.PythonIntegration;
import com.hncs.world.service.BookService;
import com.hncs.world.mapper.BookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 24774
* @description 针对表【book(书籍信息表)】的数据库操作Service实现
* @createDate 2025-10-16 10:18:38
*/
@Service
@Slf4j
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

    // Gson, 序列化工具
    private static final Gson GSON = new Gson();

    /**
     * 小说搜索
     *
     * @param bookName 小说关键词
     * @return 小说列表
     */
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
            log.info("搜索书籍失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.PY_EXECUTE_FAILED, "小说搜索失败");
        }
    }

    /**
     * 小说预览
     *
     * @param bookLink 小说链接
     * @return 小说章节列表
     */
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
            log.info("预览小说失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.PY_EXECUTE_FAILED, "小说预览失败");
        }
    }

    /**
     * 小说打开
     *
     * @param chapterLink 小说章节链接
     * @return 小说章节内容
     */
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
            log.info("打开小说失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.PY_EXECUTE_FAILED, "小说打开失败");
        }
    }

    /**
     * 获取用户收藏
     *
     * @param userId 用户ID
     * @return 收藏列表
     */
    @Override
    public List<BookVo> bookCollect(Long userId) {
        if(userId == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "userId is empty");
        }

        // 数据库条件查询用户收藏
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("link_user", userId.toString());
        List<Book> bookList = this.list(queryWrapper);

        //用户没有收藏任何书籍
        if(bookList.isEmpty()){
            return null;
        }

        // 将数据库结果转换为VO
        return bookList.stream().map(book -> {
            BookVo bookVo = new BookVo();
            BeanUtils.copyProperties(book, bookVo);
            return bookVo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取用户最近阅读
     *
     * @param userId 用户ID
     * @return 最近阅读列表
     */
    @Override
    public List<BookVo> bookRecently(Long userId) {
        List<BookVo> bookVoList = bookCollect(userId);

        if(bookVoList == null){
            return null;
        }

        //根据更新时间倒序
        return bookVoList.stream()
                .sorted((b1, b2) -> b2.getUpdateTime().compareTo(b1.getUpdateTime()))
                .collect(Collectors.toList());
    }

    /**
     * 用户收藏小说
     *
     * @param userId 用户id
     * @param userCollectBookDto 收藏书籍信息
     */
    @Override
    public void bookCollectAdd(Long userId, UserCollectBookDto userCollectBookDto) {
        if(userId == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "userId is empty");
        }

        //预设书籍
        Book book;

        //判断书籍是否存在
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_name", userCollectBookDto.getBookName());
        //存在则获取，不存在则创建
        if(this.count(queryWrapper) > 0){
            //获取书籍联系用户id
            book = this.getOne(queryWrapper);
            String linkUser = book.getLinkUser();

            //Json反序列化，添加联系用户id
            Set<String> userIds = GSON.fromJson(linkUser, new TypeToken<Set<String>>(){});

            //判断用户是否已收藏
            if(userIds.contains(userId.toString())){
                throw new BusinessException(ErrorCode.USER_COLLECT_ERROR, "用户已收藏该书籍");
            }
            userIds.add(userId.toString());

            //Json序列化
            book.setLinkUser(GSON.toJson(userIds));
        }else{
            //构建书籍
            book = new Book();
            book.setBookName(userCollectBookDto.getBookName());
            book.setBookLink(userCollectBookDto.getBookLink());

            //添加联系用户id
            Set<String> userIds = new HashSet<>();
            userIds.add(userId.toString());

            //Json序列化
            book.setLinkUser(GSON.toJson(userIds));
        }

        this.saveOrUpdate(book);
    }

    /**
     * 用户取消收藏小说
     *
     * @param userId 用户id
     * @param userCollectBookDto 收藏书籍信息
     */
    @Override
    public void bookCollectRemove(Long userId, UserCollectBookDto userCollectBookDto) {
        if(userId == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "userId is empty");
        }

        //获取小说信息
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_name", userCollectBookDto.getBookName());
        Book book = this.getOne(queryWrapper);

        if(book == null){
            throw new BusinessException(ErrorCode.USER_COLLECT_ERROR, "书籍信息为空");
        }

        //Json反序列化，获取并处理小说关联用户信息
        String linkUser = book.getLinkUser();
        Set<String> userIds = GSON.fromJson(linkUser, new TypeToken<Set<String>>(){});

        //删除用户关联记录，并更新小说
        userIds.remove(userId.toString());
        book.setLinkUser(GSON.toJson(userIds));

        //更新小说
        this.updateById(book);
    }
}




