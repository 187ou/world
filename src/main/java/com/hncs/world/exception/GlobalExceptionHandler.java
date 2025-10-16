package com.hncs.world.exception;

import com.hncs.world.common.ErrorCode;
import com.hncs.world.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理项目中抛出的各类异常，返回标准化的Result对象
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理业务异常（自定义异常）
     * @param e 业务异常对象
     * @return 统一返回结果
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        // 记录异常日志，包含错误码、消息和详情，便于调试
        log.error("业务异常: [{}] {}，详情: {}", e.getErrorCode(), e.getMessage(), e.getDetail(), e);

        // 构建并返回错误结果
        return Result.error(e);
    }

    /**
     * 处理系统异常（非预期异常）
     * @param e 系统异常对象
     * @return 统一返回结果
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleSystemException(Exception e) {
        // 记录异常日志，系统异常通常需要重点关注
        log.error("系统异常: {}", e.getMessage(), e);

        // 使用通用服务器错误码返回，避免暴露敏感信息
        return Result.error(ErrorCode.SERVER_ERROR);
    }
}
