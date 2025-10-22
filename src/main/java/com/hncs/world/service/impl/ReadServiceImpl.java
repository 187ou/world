package com.hncs.world.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hncs.world.pojo.entity.Read;
import com.hncs.world.service.ReadService;
import com.hncs.world.mapper.ReadMapper;
import org.springframework.stereotype.Service;

/**
* @author 24774
* @description 针对表【read(用户阅读记录表)】的数据库操作Service实现
* @createDate 2025-10-16 10:18:38
*/
@Service
public class ReadServiceImpl extends ServiceImpl<ReadMapper, Read>
    implements ReadService{

}




