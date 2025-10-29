package com.hncs.world.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hncs.world.common.ErrorCode;
import com.hncs.world.exception.BusinessException;
import com.hncs.world.pojo.entity.Book;
import com.hncs.world.pojo.entity.Purchased;
import com.hncs.world.pojo.vo.BookVo;
import com.hncs.world.service.PurchasedService;
import com.hncs.world.mapper.PurchasedMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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
    private BookServiceImpl bookServiceImpl;

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
        Gson gson = new Gson();
        List<Long> purchasedBookIdList = gson.fromJson(purchased.getPurchasedBook(), new TypeToken<List<Long>>(){});

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
}




