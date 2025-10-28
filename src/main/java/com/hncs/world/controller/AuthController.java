package com.hncs.world.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hncs.world.common.ErrorCode;
import com.hncs.world.pojo.entity.User;
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

import javax.servlet.http.HttpServletRequest;
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
    public Result<RegisterVo> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        // 1. 跨字段校验：密码与确认密码一致性（简单逻辑，适合在Controller层处理）
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "两次输入的密码不一致");
        }

        // 2. 验证码有效性校验（依赖外部工具，非Service层核心业务）
        boolean isCodeValid = verificationCodeUtil.validateCode(userRegisterDto.getEmail(), userRegisterDto.getCode());
        if (!isCodeValid) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "验证码错误或已过期");
        }

        // 3. 调用Service处理核心注册业务
        RegisterVo registerVo = userService.register(userRegisterDto);
        return Result.success(registerVo);
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<LoginVo> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        // 1. 补充邮箱格式校验（如果用邮箱登录）
        String emailRegex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        if (userLoginDto.getUsername().matches(emailRegex)) {
            // 邮箱登录时，校验邮箱格式（增强版，比DTO的长度校验更精确）
            if (userLoginDto.getUsername().length() > 50) { // 邮箱最长一般不超过50位
                throw new BusinessException(ErrorCode.INVALID_PARAMS, "邮箱格式过长");
            }
        } else {
            // 用户名登录时，补充特殊字符校验（假设用户名只能包含字母、数字、下划线）
            String usernameRegex = "^[a-zA-Z0-9_]+$";
            if (!userLoginDto.getUsername().matches(usernameRegex)) {
                throw new BusinessException(ErrorCode.INVALID_PARAMS, "用户名只能包含字母、数字和下划线");
            }
        }

        // 2. 调用Service处理核心登录业务（查库、密码校验等）
        LoginVo loginVo = userService.login(userLoginDto);
        return Result.success(loginVo);
    }

    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public Result<Void> logout(HttpServletRequest request) {
        // 1. 从请求头提取 accessToken（去掉 Bearer 前缀）
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "登出失败，Token 格式错误");
        }
        String accessToken = authorizationHeader.substring(7); // 截取 Bearer 后的 Token

        // 2. 调用 Service 层登出逻辑（如果有 refreshToken，同理从请求头提取）
        userService.logout(accessToken);

        return Result.success();
    }

    @PostMapping("/send-verification-code")
    @ApiOperation("发送重置密码验证码")
    public Result<SendCodeVo> sendVerificationCode(@Valid @RequestBody SendCodeDto sendCodeDto) {
        String email = sendCodeDto.getEmail();

        // 1. 校验发送频率（防止恶意刷验证码，轻量逻辑适合Controller层）
        boolean canSend = verificationCodeUtil.checkSendFrequency(email);
        if (!canSend) {
            log.info("验证码发送过于频繁，邮箱={}", email);
            throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "验证码发送过于频繁，请60秒后再试");
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
    public Result<Void> resetPassword(HttpServletRequest request,@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        log.info("接收到用户更改密码的请求，参数：{}", resetPasswordDto);
        Long userId = (Long) request.getAttribute("userId");
        // 1. 跨字段校验：新密码与确认密码一致性（简单逻辑，适合Controller层）
        if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "两次输入的密码不一致");
        }

        // 2. 补充校验：防止密码与邮箱相同（安全增强，轻量逻辑）
        if (resetPasswordDto.getNewPassword().equals(resetPasswordDto.getEmail())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "密码不能与邮箱相同");
        }

        // 3. 调用Service处理核心业务（依赖数据库和验证码校验）
        userService.resetPassword(userId,resetPasswordDto);
        return Result.success();
    }

    @PutMapping("/update-info")
    @ApiOperation("更新用户基础信息")
    public Result<UserVo> updateUserInfo( HttpServletRequest request, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        log.info("接收到用户信息更新请求，参数：{}", userUpdateDto);
        // 从请求域获取userId（拦截器已校验过Token，确保是当前登录用户）
        Long userId = (Long) request.getAttribute("userId");

        // 1. 校验：如果更新昵称，不允许包含特殊字符（轻量逻辑，适合Controller）
        if (userUpdateDto.getNickName() != null && !userUpdateDto.getNickName().matches("^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "昵称只能包含中文、字母和数字");
        }

        // 2. 校验：如果更新手机号，补充非空判断（避免空字符串更新）
        if (userUpdateDto.getPhone() != null && userUpdateDto.getPhone().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "手机号不能为空字符串");
        }

        // 3. 调用Service处理核心业务（依赖数据库的校验和更新）
        UserVo updatedUser = userService.updateUserInfo(userId,userUpdateDto);
        return Result.success(updatedUser);
    }

    @PostMapping("/send-register-code")
    @ApiOperation("发送注册验证码")
    public Result<SendCodeVo> sendRegisterCode(@Valid @RequestBody SendCodeDto sendCodeDto) {
        String email = sendCodeDto.getEmail();

        // 1. 校验发送频率（复用工具类，与其他场景保持一致）
        boolean canSend = verificationCodeUtil.checkSendFrequency(email);
        if (!canSend) {
            log.info("注册验证码发送过于频繁，邮箱={}", email);
            throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "验证码发送过于频繁，请60秒后再试");
        }

        // 2. 调用注册专用的发送验证码方法（允许向未注册邮箱发送）
        userService.sendRegisterCode(email);

        // 3. 构建返回结果
        SendCodeVo sendCodeVo = new SendCodeVo();
        sendCodeVo.setEmail(email);
        sendCodeVo.setExpiresIn(300); // 5分钟有效期
        return Result.success(sendCodeVo);
    }

    @PostMapping("/send-email-bind-code")
    @ApiOperation("发送邮箱换绑验证码")
    public Result<SendCodeVo> sendEmailBindCode(HttpServletRequest request,@Valid @RequestBody EmailBindCodeDto emailBindCodeDto) {
        String newEmail = emailBindCodeDto.getNewEmail();
        Long userId = (Long) request.getAttribute("userId");
        log.info("接收邮箱换绑验证码请求，用户ID={}，新邮箱={}", userId, newEmail);

        // 1. 校验用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            log.info("邮箱换绑验证码发送失败：用户不存在，用户ID={}", userId);
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "用户不存在");
        }

        // 2.新增：校验新邮箱是否与当前邮箱一致
        if (newEmail.equals(user.getEmail())) {
            log.info("邮箱换绑验证码发送失败：新邮箱与当前邮箱一致，用户ID={}，邮箱={}", userId, newEmail);
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "新邮箱与当前绑定邮箱一致，无需换绑");
        }

        // 3. 校验新邮箱是否已被其他用户注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, newEmail)
                .ne(User::getUserId, userId);
        if (userService.count(queryWrapper) > 0) {
            log.info("邮箱换绑验证码发送失败：新邮箱已被注册，邮箱={}，用户ID={}", newEmail, userId);
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "该邮箱已被其他账号注册，无法换绑");
        }

        // 4. 校验发送频率
        String cacheKey = "email_bind:" + newEmail + ":" + userId;
        if (!verificationCodeUtil.checkSendFrequency(cacheKey)) {
            log.info("邮箱换绑验证码发送失败：频率超限，邮箱={}，用户ID={}", newEmail, userId);
            throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "验证码发送过于频繁，请60秒后再试");
        }

        // 5. 生成并发送验证码
        String code = verificationCodeUtil.generateCode(cacheKey);
        try {
            emailUtil.sendVerificationCode(newEmail, "您正在换绑邮箱，验证码为：" + code + "，5分钟内有效");
            log.info("邮箱换绑验证码发送成功，用户ID={}，新邮箱={}", userId, newEmail);
        } catch (Exception e) {
            log.info("邮箱换绑验证码发送失败，用户ID={}，新邮箱={}", userId, newEmail, e);
            throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "验证码发送失败，请检查邮箱有效性");
        }

        // 6. 构建返回结果
        SendCodeVo vo = new SendCodeVo();
        vo.setEmail(newEmail);
        vo.setExpiresIn(300);
        return Result.success(vo);
    }

    @PutMapping("/update-email")
    @ApiOperation("更新用户邮箱")
    public Result<Void> updateUserEmail( HttpServletRequest request,@Valid @RequestBody UpdateEmailDto updateEmailDto) {
        Long userId = (Long) request.getAttribute("userId");

        userService.updateEmail(userId,updateEmailDto);

        return Result.success();
    }

    @GetMapping("/info")
    @ApiOperation("获取当前登录用户的信息")
    public Result<UserVo> getUserInfo(HttpServletRequest request) {
        // 1. 从拦截器传递的request中获取当前登录用户的ID（无需前端传参）
        Long loginUserId = (Long) request.getAttribute("userId");
        log.info("接收到获取当前登录用户信息的请求，用户ID：{}", loginUserId);

        // 2. 调用Service查询当前用户信息
        UserVo userVo = userService.getUserInfoById(loginUserId);

        return Result.success(userVo);
    }
}