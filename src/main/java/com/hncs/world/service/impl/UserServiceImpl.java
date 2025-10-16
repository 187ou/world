package com.hncs.world.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hncs.world.pojo.entity.User;
import com.hncs.world.service.UserService;
import com.hncs.world.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 24774
* @description 针对表【user(用户信息表)】的数据库操作Service实现
* @createDate 2025-10-16 10:18:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




