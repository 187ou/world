package com.hncs.world.service;

import com.hncs.world.pojo.dto.ResetPasswordDto;
import com.hncs.world.pojo.dto.UserLoginDto;
import com.hncs.world.pojo.dto.UserRegisterDto;
import com.hncs.world.pojo.dto.UserUpdateDto;
import com.hncs.world.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hncs.world.pojo.vo.LoginVo;
import com.hncs.world.pojo.vo.RegisterVo;
import com.hncs.world.pojo.vo.UserVo;

/**
* @author 24774
* @description 针对表【user(用户信息表)】的数据库操作Service
* @createDate 2025-10-16 10:18:38
*/
public interface UserService extends IService<User> {
    RegisterVo register(UserRegisterDto dto);
    LoginVo login(UserLoginDto dto);
    void logout(String token, String refreshToken);
    void sendVerificationCode(String email);
    void resetPassword(ResetPasswordDto dto);
    UserVo getUserVoById(Long userId);
    UserVo updateUserInfo(UserUpdateDto dto);
    void sendRegisterCode(String email);
}
