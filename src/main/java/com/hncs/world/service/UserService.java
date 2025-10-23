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
    /**
     * 用户注册
     * @param dto 注册信息
     * @return
     */
    RegisterVo register(UserRegisterDto dto);

    /**
     * 用户登录
     * @param dto 登录信息
     * @return
     */
    LoginVo login(UserLoginDto dto);

    /**
     * 用户登出
     * @param token
     * @param refreshToken
     */
    void logout(String token, String refreshToken);

    /**
     * 发送验证码
     * @param email
     */
    void sendVerificationCode(String email);

    /**
     * 重置密码
     * @param dto
     */
    void resetPassword(ResetPasswordDto dto);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    UserVo getUserVoById(Long userId);

    /**
     * 更新用户信息
     * @param dto
     * @return
     */
    UserVo updateUserInfo(UserUpdateDto dto);

    /**
     * 发送注册验证码
     * @param email
     */
    void sendRegisterCode(String email);
}
