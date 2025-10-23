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
     * @param userRegisterDto 注册信息
     * @return
     */
    RegisterVo register(UserRegisterDto userRegisterDto);

    /**
     * 用户登录
     * @param userLoginDto 登录信息
     * @return
     */
    LoginVo login(UserLoginDto userLoginDto);

    /**
     * 用户登出
     * @param token 用户登录后携带的token
     * @param refreshToken
     */
    void logout(String token, String refreshToken);

    /**
     * 发送验证码
     * @param email 用户邮箱
     */
    void sendVerificationCode(String email);

    /**
     * 重置密码
     * @param resetPasswordDto 重置密码信息
     */
    void resetPassword(ResetPasswordDto resetPasswordDto);


    /**
     * 更新用户信息
     * @param userUpdateDto 更新的用户信息
     * @return
     */
    UserVo updateUserInfo(UserUpdateDto userUpdateDto);

    /**
     * 发送注册验证码
     * @param email 用户邮箱
     */
    void sendRegisterCode(String email);
}
