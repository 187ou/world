package com.hncs.world.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hncs.world.pojo.entity.Hot;
import com.hncs.world.service.HotService;
import com.hncs.world.mapper.HotMapper;
import org.springframework.stereotype.Service;

/**
* @author 24774
* @description 针对表【hot(热门书籍统计表)】的数据库操作Service实现
* @createDate 2025-10-16 10:18:38
*/
@Service
public class HotServiceImpl extends ServiceImpl<HotMapper, Hot>
    implements HotService{

}




