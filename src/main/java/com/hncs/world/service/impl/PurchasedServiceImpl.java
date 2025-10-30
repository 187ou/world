package com.hncs.world.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hncs.world.common.ErrorCode;
import com.hncs.world.exception.BusinessException;
import com.hncs.world.pojo.dto.UserPurchasedBookDto;
import com.hncs.world.pojo.entity.Book;
import com.hncs.world.pojo.entity.Purchased;
import com.hncs.world.pojo.entity.User;
import com.hncs.world.pojo.vo.BookVo;
import com.hncs.world.service.PurchasedService;
import com.hncs.world.mapper.PurchasedMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 24774
* @description 针对表【purchased(用户书籍购买记录表)】的数据库操作Service实现
* @createDate 2025-10-29 17:15:55
*/
@Service
public class PurchasedServiceImpl extends ServiceImpl<PurchasedMapper, Purchased>
    implements PurchasedService{

    @Resource
    private UserServiceImpl userService;

    @Resource
    private BookServiceImpl bookServiceImpl;

    // Gson, 序列化工具
    private static final Gson GSON = new Gson();

    /**
     * 获取已购小说接口
     * @param userId 用户ID
     * @return 已购小说列表
     */
    @Override
    public List<BookVo> bookPurchased(Long userId) {
        if(userId == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "userId is empty");
        }

        // 数据库条件查询用户已购小说id - 返回的是Json形式
        QueryWrapper<Purchased> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Purchased purchased = this.getOne(queryWrapper);

        // 未查询到已购记录
        if(purchased == null){
            return null;
        }

        // Json串反序列为List
        List<Long> purchasedBookIdList = GSON.fromJson(purchased.getPurchasedBook(), new TypeToken<List<Long>>(){});

        // 通过bookId查询小说
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.in("id", purchasedBookIdList);
        List<Book> bookList = bookServiceImpl.list(bookQueryWrapper);

        // 封装成BookVo
        return bookList.stream().map(book -> {
            BookVo bookVo = new BookVo();
            BeanUtils.copyProperties(book, bookVo);
            return bookVo;
        }).collect(Collectors.toList());
    }

    /**
     * 添加已购小说接口
     * @param userId 用户ID
     * @param userPurchasedBookDto 添加的已购小说信息
     * @return 用户余额
     */
    @Override
    public Integer bookPurchasedAdd(Long userId, UserPurchasedBookDto userPurchasedBookDto) {
        if(userId == null){
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "userId is empty");
        }

        //获取用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        User user = userService.getOne(queryWrapper);

        //获取用户购买记录
        QueryWrapper<Purchased> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("user_id", userId);
        Purchased purchased = this.getOne(queryWrapper1);

        //判断用户购买记录是否存在
        if(purchased == null){
            purchased = new Purchased();
            purchased.setUserId(userId);
        }

        //获取用户购买的小说记录
        Set<String> purchasedBooks;
        if(purchased.getPurchasedBook() == null){
            purchasedBooks = new HashSet<>();
        }else{
            purchasedBooks = GSON.fromJson(purchased.getPurchasedBook(), new TypeToken<Set<String>>() {
            });
        }

        //判断用户是否已购该书籍
        if(purchasedBooks.contains(userPurchasedBookDto.getBookName())){
            throw new BusinessException(ErrorCode.USER_PURCHASED_BOOK_EXIST, "用户已购该书籍");
        }

        //判断用户余额是否充足
        if(user.getMoney() < userPurchasedBookDto.getCost()){
            throw new BusinessException(ErrorCode.USER_MONEY_NOT_ENOUGH, "无法购买书籍");
        }

        //更新用户余额
        Integer balance = user.getMoney() - userPurchasedBookDto.getCost();
        user.setMoney(balance);

        //添加用户已购书籍
        purchasedBooks.add(userPurchasedBookDto.getBookName());

        //更新用户
        userService.updateById(user);

        //更新用户已购记录
        purchased.setPurchasedBook(GSON.toJson(purchasedBooks));
        this.updateById(purchased);

        return balance;
    }
}




