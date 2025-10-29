package com.hncs.world.service;

import com.hncs.world.pojo.entity.Purchased;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hncs.world.pojo.vo.BookVo;

import java.util.List;

/**
* @author 24774
* @description 针对表【purchased(用户书籍购买记录表)】的数据库操作Service
* @createDate 2025-10-29 17:15:55
*/
public interface PurchasedService extends IService<Purchased> {

    /**
     * 获取已购小说接口
     * @param userId 用户ID
     * @return 已购小说列表
     */
    List<BookVo> bookPurchased(Long userId);
}
