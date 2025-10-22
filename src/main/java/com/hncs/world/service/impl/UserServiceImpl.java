package com.hncs.world.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hncs.world.common.ErrorCode;
import com.hncs.world.common.utils.EmailUtil;
import com.hncs.world.common.utils.JwtUtil;
import com.hncs.world.common.utils.VerificationCodeUtil;
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
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private VerificationCodeUtil verificationCodeUtil;

    @Override
    public RegisterVo register(UserRegisterDto dto) {
        // 1. 用户名唯一性校验（依赖数据库，属于核心业务）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, dto.getUserName());
        if (count(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.REGISTER_USERNAME_DUPLICATE, "用户名已存在");
        }

        // 2. 邮箱唯一性校验（依赖数据库，属于核心业务）
        queryWrapper.clear();
        queryWrapper.eq(User::getEmail, dto.getEmail());
        if (count(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.REGISTER_EMAIL_DUPLICATE, "邮箱已被注册");
        }

        // 3. 手机号唯一性校验（若有手机号则校验，依赖数据库）
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            queryWrapper.clear();
            queryWrapper.eq(User::getPhone, dto.getPhone());
            if (count(queryWrapper) > 0) {
                throw new BusinessException(ErrorCode.REGISTER_PHONE_DUPLICATE, "手机号已被注册");
            }
        }

        // 4. 创建用户（敏感字段处理）
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        // 密码加密存储
        user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
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

    @Override
    public LoginVo login(UserLoginDto dto) {
        // 1. 根据用户名或邮箱查询用户（核心业务：定位用户）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, dto.getUsername())
                .or()
                .eq(User::getEmail, dto.getUsername());
        User user = getOne(queryWrapper);

        // 2. 业务校验：用户是否存在
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_USER_NOT_EXIST, "用户不存在");
        }

        // 3. 业务校验：密码是否正确（依赖加密算法，属于核心逻辑）
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            // 记录失败日志（便于风控分析）
            log.warn("用户登录失败：密码错误，用户名/邮箱={}", dto.getUsername());
            throw new BusinessException(ErrorCode.LOGIN_PASSWORD_ERROR, "密码错误");
        }

        // 4. 业务校验：账号是否被禁用（依赖用户状态，属于业务规则）
        if (user.getStatus() == 0) {
            log.warn("用户登录失败：账号已禁用，用户ID={}", user.getUserId());
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

    @Override
    public void logout(String token, String refreshToken) {
        log.info("用户退出登录，token: {}", token);
    }

    @Override
    public void sendVerificationCode(String email) {
        // 1. 业务校验：邮箱是否已注册（依赖数据库查询，核心业务规则）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        if (user == null) {
            log.warn("发送验证码失败：邮箱未注册，邮箱={}", email);
            throw new BusinessException(ErrorCode.VERIFY_EMAIL_UNREGISTERED, "邮箱未注册");
        }

        // 2. 生成验证码（核心业务逻辑）
        String code = verificationCodeUtil.generateCode(email);

        // 3. 发送验证码并处理异常（核心业务逻辑，依赖外部服务）
        try {
            emailUtil.sendVerificationCode(email, code);
            log.info("验证码发送成功，邮箱={}", email);
        } catch (Exception e) {
            log.error("验证码发送失败，邮箱={}", email, e);
            throw new BusinessException(ErrorCode.VERIFY_SEND_FAILED, "验证码发送失败，请稍后重试");
        }
    }

    @Override
    public void resetPassword(ResetPasswordDto dto) {
        String email = dto.getEmail();
        String newPassword = dto.getNewPassword();

        // 1. 业务校验：验证码有效性（依赖验证码工具，核心业务）
        if (!verificationCodeUtil.validateCode(email, dto.getCode())) {
            log.warn("重置密码失败：验证码无效，邮箱={}", email);
            throw new BusinessException(ErrorCode.VERIFY_CODE_INVALID, "验证码错误或已过期");
        }

        // 2. 业务校验：用户是否存在（依赖数据库，核心业务）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        if (user == null) {
            log.warn("重置密码失败：用户不存在，邮箱={}", email);
            throw new BusinessException(ErrorCode.RESET_USER_NOT_EXIST, "用户不存在");
        }

        // 3. 业务校验：新密码是否与旧密码相同（安全增强，依赖数据库）
        if (BCrypt.checkpw(newPassword, user.getPassword())) {
            log.warn("重置密码失败：新密码与旧密码相同，用户ID={}", user.getUserId());
            throw new BusinessException(ErrorCode.RESET_PASSWORD_SAME_AS_OLD, "新密码不能与旧密码相同");
        }

        // 4. 核心逻辑：更新密码并记录时间
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        user.setUpdateTime(new Date());
        boolean updateSuccess = updateById(user);
        if (!updateSuccess) {
            log.error("重置密码失败：数据库更新异常，用户ID={}", user.getUserId());
            throw new BusinessException(ErrorCode.RESET_DB_UPDATE_FAIL, "重置密码失败，请稍后重试");
        }

        log.info("重置密码成功，用户ID={}", user.getUserId());
    }

    @Override
    public UserVo getUserVoById(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.QUERY_USER_NOT_EXIST, "用户不存在");
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public UserVo updateUserInfo(UserUpdateDto dto) {
        // 1. 核心业务校验：用户是否存在（依赖数据库，必须在Service层处理）
        User user = baseMapper.selectById(dto.getUserId());
        if (user == null) {
            log.warn("更新用户信息失败：用户不存在，用户ID={}", dto.getUserId());
            throw new BusinessException(ErrorCode.UPDATE_USER_NOT_EXIST, "用户不存在");
        }

        // 2. 核心业务校验：邮箱更新时的唯一性（依赖数据库，排除当前用户）
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            // 邮箱格式已在DTO层通过@Email校验，此处无需重复
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getEmail, dto.getEmail())
                    .ne(User::getUserId, dto.getUserId());
            long count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                log.warn("更新用户信息失败：邮箱已被占用，邮箱={}，用户ID={}", dto.getEmail(), dto.getUserId());
                throw new BusinessException(ErrorCode.UPDATE_EMAIL_DUPLICATE, "邮箱已被注册");
            }
            user.setEmail(dto.getEmail()); // 邮箱不同且唯一，更新
        }

        // 3. 核心业务校验：手机号更新时的唯一性（依赖数据库，排除当前用户）
        if (dto.getPhone() != null && !dto.getPhone().equals(user.getPhone())) {
            // 手机号格式已在DTO层通过@Pattern校验，此处无需重复
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, dto.getPhone())
                    .ne(User::getUserId, dto.getUserId());
            long count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                log.warn("更新用户信息失败：手机号已被占用，手机号={}，用户ID={}", dto.getPhone(), dto.getUserId());
                throw new BusinessException(ErrorCode.UPDATE_PHONE_DUPLICATE, "手机号已被注册");
            }
            user.setPhone(dto.getPhone()); // 手机号不同且唯一，更新
        }

        // 4. 其他字段更新（仅处理非空参数，依赖DTO层的格式校验）
        if (dto.getNickName() != null) {
            user.setNickName(dto.getNickName());
        }
        if (dto.getSex() != null) {
            // 性别范围已在DTO层通过@Min/@Max校验，此处无需重复
            user.setSex(dto.getSex());
        }

        // 5. 更新时间戳并执行数据库更新
        user.setUpdateTime(new Date());
        int rows = baseMapper.updateById(user);
        if (rows != 1) {
            log.error("更新用户信息失败：数据库操作异常，用户ID={}", dto.getUserId());
            throw new BusinessException(ErrorCode.UPDATE_DB_FAIL, "更新用户信息失败");
        }

        // 6. 转换为VO返回（屏蔽敏感字段）
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        log.info("更新用户信息成功，用户ID={}", dto.getUserId());
        return userVo;
    }

    @Override
    public void sendRegisterCode(String email) {
        // 1. 注册场景：校验邮箱是否已注册（已注册则不允许发送注册验证码）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        if (user != null) {
            log.warn("注册验证码发送失败：邮箱已注册，邮箱={}", email);
            throw new BusinessException(ErrorCode.REGISTER_EMAIL_DUPLICATE, "邮箱已注册，无需重复注册");
        }

        // 2. 检查发送频率（复用工具类方法）
        if (!verificationCodeUtil.checkSendFrequency(email)) {
            throw new BusinessException(ErrorCode.VERIFY_FREQUENCY_LIMIT, "验证码发送过于频繁，请60秒后再试");
        }

        // 3. 生成并发送验证码（与原有逻辑一致）
        String code = verificationCodeUtil.generateCode(email);
        try {
            emailUtil.sendVerificationCode(email, code);
            log.info("注册验证码发送成功，邮箱={}", email);
        } catch (Exception e) {
            log.error("注册验证码发送失败，邮箱={}", email, e);
            throw new BusinessException(ErrorCode.VERIFY_SEND_FAILED, "验证码发送失败，请稍后重试");
        }
    }
}