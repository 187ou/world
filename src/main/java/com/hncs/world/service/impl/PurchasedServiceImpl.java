package com.hncs.world.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hncs.world.pojo.entity.Purchased;
import com.hncs.world.service.PurchasedService;
import com.hncs.world.mapper.PurchasedMapper;
import org.springframework.stereotype.Service;

/**
* @author 24774
* @description 针对表【purchased(用户书籍购买记录表)】的数据库操作Service实现
* @createDate 2025-10-16 10:18:38
*/
@Service
public class PurchasedServiceImpl extends ServiceImpl<PurchasedMapper, Purchased>
    implements PurchasedService{

}




