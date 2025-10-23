package com.hncs.world.controller;

import com.hncs.world.common.ErrorCode;
import com.hncs.world.utils.EmailUtil;
import com.hncs.world.utils.JwtUtil;
import com.hncs.world.utils.VerificationCodeUtil;
import com.hncs.world.common.Result;
import com.hncs.world.exception.BusinessException;
import com.hncs.world.pojo.dto.*;
import com.hncs.world.pojo.vo.*;
import com.hncs.world.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "认证管理")
@Slf4j
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private EmailUtil emailUtil;
    
    @Autowired
    private VerificationCodeUtil verificationCodeUtil;

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result<RegisterVo> register(@Valid @RequestBody UserRegisterDto dto) {
        // 1. 跨字段校验：密码与确认密码一致性（简单逻辑，适合在Controller层处理）
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "两次输入的密码不一致");
        }

        // 2. 验证码有效性校验（依赖外部工具，非Service层核心业务）
        boolean isCodeValid = verificationCodeUtil.validateCode(dto.getEmail(), dto.getCode());
        if (!isCodeValid) {
            throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "验证码错误或已过期");
        }

        // 3. 调用Service处理核心注册业务
        RegisterVo registerVo = userService.register(dto);
        return Result.success(registerVo);
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<LoginVo> login(@Valid @RequestBody UserLoginDto dto) {
        // 1. 补充邮箱格式校验（如果用邮箱登录）
        String emailRegex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        if (dto.getUsername().matches(emailRegex)) {
            // 邮箱登录时，校验邮箱格式（增强版，比DTO的长度校验更精确）
            if (dto.getUsername().length() > 50) { // 邮箱最长一般不超过50位
                throw new BusinessException(ErrorCode.LOGIN_EMAIL_FORMAT_ERROR, "邮箱格式过长");
            }
        } else {
            // 用户名登录时，补充特殊字符校验（假设用户名只能包含字母、数字、下划线）
            String usernameRegex = "^[a-zA-Z0-9_]+$";
            if (!dto.getUsername().matches(usernameRegex)) {
                throw new BusinessException(ErrorCode.INVALID_PARAMS, "用户名只能包含字母、数字和下划线");
            }
        }

        // 2. 调用Service处理核心登录业务（查库、密码校验等）
        LoginVo loginVo = userService.login(dto);
        return Result.success(loginVo);
    }
    
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result<Void> logout(@RequestHeader("Authorization") String token,
                              @Valid @RequestBody LogoutDto dto) {
        userService.logout(token, dto.getRefreshToken());
        return Result.success();
    }

    @PostMapping("/send-verification-code")
    @ApiOperation("发送验证码")
    public Result<SendCodeVo> sendVerificationCode(@Valid @RequestBody SendCodeDto dto) {
        String email = dto.getEmail();

        // 1. 校验发送频率（防止恶意刷验证码，轻量逻辑适合Controller层）
        boolean canSend = verificationCodeUtil.checkSendFrequency(email);
        if (!canSend) {
            log.warn("验证码发送过于频繁，邮箱={}", email);
            throw new BusinessException(ErrorCode.VERIFY_FREQUENCY_LIMIT, "验证码发送过于频繁，请60秒后再试");
        }

        // 2. 调用Service处理核心业务（依赖数据库的校验和发送逻辑）
        userService.sendVerificationCode(email);

        // 3. 构建返回结果
        SendCodeVo sendCodeVo = new SendCodeVo();
        sendCodeVo.setEmail(email);
        sendCodeVo.setExpiresIn(300); // 5分钟有效期
        return Result.success(sendCodeVo);
    }

    @PostMapping("/reset-password")
    @ApiOperation("重置密码")
    public Result<Void> resetPassword(@Valid @RequestBody ResetPasswordDto dto) {
        // 1. 跨字段校验：新密码与确认密码一致性（简单逻辑，适合Controller层）
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "两次输入的密码不一致");
        }

        // 2. 补充校验：防止密码与邮箱相同（安全增强，轻量逻辑）
        if (dto.getNewPassword().equals(dto.getEmail())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "密码不能与邮箱相同");
        }

        // 3. 调用Service处理核心业务（依赖数据库和验证码校验）
        userService.resetPassword(dto);
        return Result.success();
    }

    @PutMapping("/info")
    @ApiOperation("更新用户信息")
    public Result<UserVo> updateUserInfo(@Valid @RequestBody UserUpdateDto dto) {
        log.info("接收到用户信息更新请求，参数：{}", dto);

        // 1. 校验：如果更新昵称，不允许包含特殊字符（轻量逻辑，适合Controller）
        if (dto.getNickName() != null && !dto.getNickName().matches("^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "昵称只能包含中文、字母和数字");
        }

        // 2. 校验：如果更新手机号，补充非空判断（避免空字符串更新）
        if (dto.getPhone() != null && dto.getPhone().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "手机号不能为空字符串");
        }

        // 3. 调用Service处理核心业务（依赖数据库的校验和更新）
        UserVo updatedUser = userService.updateUserInfo(dto);
        return Result.success(updatedUser);
    }

    @PostMapping("/send-register-code")
    @ApiOperation("发送注册验证码")
    public Result<SendCodeVo> sendRegisterCode(@Valid @RequestBody SendCodeDto dto) {
        String email = dto.getEmail();

        // 1. 校验发送频率（复用工具类，与其他场景保持一致）
        boolean canSend = verificationCodeUtil.checkSendFrequency(email);
        if (!canSend) {
            log.warn("注册验证码发送过于频繁，邮箱={}", email);
            throw new BusinessException(ErrorCode.VERIFY_FREQUENCY_LIMIT, "验证码发送过于频繁，请60秒后再试");
        }

        // 2. 调用注册专用的发送验证码方法（允许向未注册邮箱发送）
        userService.sendRegisterCode(email);

        // 3. 构建返回结果
        SendCodeVo sendCodeVo = new SendCodeVo();
        sendCodeVo.setEmail(email);
        sendCodeVo.setExpiresIn(300); // 5分钟有效期
        return Result.success(sendCodeVo);
    }
}