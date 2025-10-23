package com.hncs.world.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hncs.world.common.ErrorCode;
import com.hncs.world.utils.EmailUtil;
import com.hncs.world.utils.JwtUtil;
import com.hncs.world.utils.VerificationCodeUtil;
import com.hncs.world.exception.BusinessException;
import com.hncs.world.pojo.dto.ResetPasswordDto;
import com.hncs.world.pojo.dto.UserLoginDto;
import com.hncs.world.pojo.dto.UserRegisterDto;
import com.hncs.world.pojo.dto.UserUpdateDto;
import com.hncs.world.pojo.entity.User;
import com.hncs.world.pojo.vo.LoginVo;
import com.hncs.world.pojo.vo.RegisterVo;
import com.hncs.world.pojo.vo.TokenVo;
import com.hncs.world.pojo.vo.UserVo;
import com.hncs.world.service.UserService;
import com.hncs.world.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 24774
 * @description 针对表【user(用户信息表)】的数据库操作Service实现
 * @createDate 2025-10-16 10:18:38
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private VerificationCodeUtil verificationCodeUtil;

    /**
     * 用户注册
     * @param userRegisterDto 注册信息
     * @return
     */
    @Override
    public RegisterVo register(UserRegisterDto userRegisterDto) {
        // 1. 用户名唯一性校验（依赖数据库，属于核心业务）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userRegisterDto.getUserName());
        if (count(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "用户名已存在");
        }

        // 2. 邮箱唯一性校验（依赖数据库，属于核心业务）
        queryWrapper.clear();
        queryWrapper.eq(User::getEmail, userRegisterDto.getEmail());
        if (count(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "邮箱已被注册");
        }

        // 3. 手机号唯一性校验（若有手机号则校验，依赖数据库）
        if (userRegisterDto.getPhone() != null && !userRegisterDto.getPhone().isEmpty()) {
            queryWrapper.clear();
            queryWrapper.eq(User::getPhone, userRegisterDto.getPhone());
            if (count(queryWrapper) > 0) {
                throw new BusinessException(ErrorCode.INVALID_PARAMS, "手机号已被注册");
            }
        }

        // 4. 创建用户（敏感字段处理）
        User user = new User();
        BeanUtils.copyProperties(userRegisterDto, user);
        // 密码加密存储
        user.setPassword(BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt()));
        // 初始化用户状态和基础信息
        user.setLevel(1);
        user.setMoney(0);
        user.setStatus(1); // 默认为启用状态
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        save(user);

        // 5. 转换为VO返回（屏蔽敏感字段）
        RegisterVo registerVo = new RegisterVo();
        BeanUtils.copyProperties(user, registerVo);
        return registerVo;
    }

    /**
     * 用户登录
     * @param userLoginDto 登录信息
     * @return
     */
    @Override
    public LoginVo login(UserLoginDto userLoginDto) {
        // 1. 根据用户名或邮箱查询用户（核心业务：定位用户）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userLoginDto.getUsername())
                .or()
                .eq(User::getEmail, userLoginDto.getUsername());
        User user = this.getOne(queryWrapper);

        // 2. 业务校验：用户是否存在
        if (user == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "用户不存在");
        }

        // 3. 业务校验：密码是否正确（依赖加密算法，属于核心逻辑）
        if (!BCrypt.checkpw(userLoginDto.getPassword(), user.getPassword())) {
            // 记录失败日志（便于风控分析）
            log.info("用户登录失败：密码错误，用户名/邮箱={}", userLoginDto.getUsername());
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "密码错误");
        }

        // 4. 业务校验：账号是否被禁用（依赖用户状态，属于业务规则）
        if (user.getStatus() == 0) {
            log.info("用户登录失败：账号已禁用，用户ID={}", user.getUserId());
            throw new BusinessException(ErrorCode.LOGIN_ACCOUNT_DISABLED, "账号已被禁用");
        }

        // 5. 生成token（核心业务：身份认证）
        String token = jwtUtil.generateToken(user);

        // 6. 构建返回结果（屏蔽敏感字段）
        LoginVo loginVo = new LoginVo();
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        loginVo.setUser(userVo);

        TokenVo tokenVo = new TokenVo();
        tokenVo.setAccessToken(token);
        tokenVo.setExpiresIn(900); // 15分钟有效期
        loginVo.setTokens(tokenVo);

        // 记录登录成功日志
        log.info("用户登录成功，用户ID={}", user.getUserId());
        return loginVo;
    }

    /**
     * 用户登出
     * @param token 用户登录后携带的token
     * @param refreshToken 用户登录后携带的refreshToken
     */
    @Override
    public void logout(String token, String refreshToken) {
        log.info("用户退出登录，token: {}", token);
    }

    /**
     * 发送验证码
     * @param email 用户邮箱
     */
    @Override
    public void sendVerificationCode(String email) {
        // 1. 业务校验：邮箱是否已注册（依赖数据库查询，核心业务规则）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        if (user == null) {
            log.info("发送验证码失败：邮箱未注册，邮箱={}", email);
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "邮箱未注册");
        }

        // 2. 生成验证码（核心业务逻辑）
        String code = verificationCodeUtil.generateCode(email);

        // 3. 发送验证码并处理异常（核心业务逻辑，依赖外部服务）
        try {
            emailUtil.sendVerificationCode(email, code);
            log.info("验证码发送成功，邮箱={}", email);
        } catch (Exception e) {
            log.info("验证码发送失败，邮箱={}", email, e);
            throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "验证码发送失败，请稍后重试");
        }
    }

    /**
     * 重置密码
     * @param resetPasswordDto 重置密码信息
     */
    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        String email = resetPasswordDto.getEmail();
        String newPassword = resetPasswordDto.getNewPassword();

        // 1. 业务校验：验证码有效性（依赖验证码工具，核心业务）
        if (!verificationCodeUtil.validateCode(email, resetPasswordDto.getCode())) {
            log.info("重置密码失败：验证码无效，邮箱={}", email);
            throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "验证码错误或已过期");
        }

        // 2. 业务校验：用户是否存在（依赖数据库，核心业务）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        if (user == null) {
            log.info("重置密码失败：用户不存在，邮箱={}", email);
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "用户不存在");
        }

        // 3. 业务校验：新密码是否与旧密码相同（安全增强，依赖数据库）
        if (BCrypt.checkpw(newPassword, user.getPassword())) {
            log.info("重置密码失败：新密码与旧密码相同，用户ID={}", user.getUserId());
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "新密码不能与旧密码相同");
        }

        // 4. 核心逻辑：更新密码并记录时间
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        user.setUpdateTime(new Date());
        boolean updateSuccess = updateById(user);
        if (!updateSuccess) {
            log.info("重置密码失败：数据库更新异常，用户ID={}", user.getUserId());
            throw new BusinessException(ErrorCode.RESET_DB_UPDATE_FAIL, "重置密码失败，请稍后重试");
        }

        log.info("重置密码成功，用户ID={}", user.getUserId());
    }

    /**
     * 更新用户信息
     * @param userUpdateDto 更新的用户信息
     * @return 更新后的用户信息
     */
    @Override
    public UserVo updateUserInfo(UserUpdateDto userUpdateDto) {
        // 1. 校验用户是否存在（原有逻辑）
        User user = baseMapper.selectById(userUpdateDto.getUserId());
        if (user == null) {
            log.info("更新用户信息失败：用户不存在，用户ID={}", userUpdateDto.getUserId());
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "用户不存在");
        }
        // 2. 邮箱更新逻辑（新增验证码校验）
        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().equals(user.getEmail())) {

            // 2.1 校验邮箱验证码（核心新增步骤）
            String newEmail = userUpdateDto.getEmail();
            Long userId = userUpdateDto.getUserId();
            String emailCode = userUpdateDto.getEmailVerificationCode(); // 从DTO获取验证码

            // 校验验证码是否为空（DTO层已通过@AssertTrue确保，此处可作为双重保障）
            if (emailCode == null || emailCode.trim().isEmpty()) {
                throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "请输入邮箱验证码");
            }

            // 校验验证码有效性（缓存key与发送时一致："email_bind:新邮箱:用户ID"）
            String cacheKey = "email_bind:" + newEmail + ":" + userId;
            boolean isCodeValid = verificationCodeUtil.validateCode(cacheKey, emailCode);
            if (!isCodeValid) {
                log.info("更新用户信息失败：邮箱验证码无效，邮箱={}，用户ID={}", newEmail, userId);
                throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "邮箱验证码错误或已过期");
            }

            // 2.2 校验邮箱唯一性（原有逻辑保留，防止并发场景下被注册）
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getEmail, newEmail)
                    .ne(User::getUserId, userId);
            long count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                log.info("更新用户信息失败：邮箱已被占用，邮箱={}，用户ID={}", newEmail, userId);
                throw new BusinessException(ErrorCode.INVALID_PARAMS, "邮箱已被注册");
            }

            // 2.3 验证通过，更新邮箱
            user.setEmail(newEmail);
        }

        // 3. 手机号更新逻辑（原有逻辑不变）
        if (userUpdateDto.getPhone() != null && !userUpdateDto.getPhone().equals(user.getPhone())) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, userUpdateDto.getPhone())
                    .ne(User::getUserId, userUpdateDto.getUserId());
            long count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                log.info("更新用户信息失败：手机号已被占用，手机号={}，用户ID={}", userUpdateDto.getPhone(), userUpdateDto.getUserId());
                throw new BusinessException(ErrorCode.INVALID_PARAMS, "手机号已被注册");
            }
            user.setPhone(userUpdateDto.getPhone());
        }

        // 4. 其他字段更新（原有逻辑不变）
        if (userUpdateDto.getNickName() != null) {
            user.setNickName(userUpdateDto.getNickName());
        }
        if (userUpdateDto.getSex() != null) {
            user.setSex(userUpdateDto.getSex());
        }

        // 5. 执行更新（原有逻辑不变）
        user.setUpdateTime(new Date());
        int rows = baseMapper.updateById(user);
        if (rows != 1) {
            log.info("更新用户信息失败：数据库操作异常，用户ID={}", userUpdateDto.getUserId());
            throw new BusinessException(ErrorCode.UPDATE_DB_FAIL, "更新用户信息失败");
        }

        // 6. 转换VO返回（原有逻辑不变）
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        log.info("更新用户信息成功，用户ID={}", userUpdateDto.getUserId());
        return userVo;
    }

    /**
     * 发送注册验证码
     * @param email 用户邮箱
     */
    @Override
    public void sendRegisterCode(String email) {
        // 1. 注册场景：校验邮箱是否已注册（已注册则不允许发送注册验证码）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        if (user != null) {
            log.info("注册验证码发送失败：邮箱已注册，邮箱={}", email);
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "邮箱已注册，无需重复注册");
        }

        // 2. 检查发送频率（复用工具类方法）
        if (!verificationCodeUtil.checkSendFrequency(email)) {
            throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "验证码发送过于频繁，请60秒后再试");
        }

        // 3. 生成并发送验证码（与原有逻辑一致）
        String code = verificationCodeUtil.generateCode(email);
        try {
            emailUtil.sendVerificationCode(email, code);
            log.info("注册验证码发送成功，邮箱={}", email);
        } catch (Exception e) {
            log.info("注册验证码发送失败，邮箱={}", email, e);
            throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "验证码发送失败，请稍后重试");
        }
    }
}

