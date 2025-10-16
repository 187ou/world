package com.hncs.world.exception;

import com.hncs.world.common.ErrorCode;
import lombok.Getter;

/**
 * 业务异常类，结合自定义错误码使用
 *
 * @author peng
 */
@Getter
public class BusinessException extends RuntimeException {
    // 错误码
    private final int errorCode;
    // 错误详情（可用于后端调试）
    private final String detail;
    // 异常发生时间
    private final long timestamp;

    /**
     * 全参构造器（最灵活）
     * @param errorCode 错误码
     * @param message 错误提示（前端展示）
     * @param detail 错误详情
     * @param cause 原始异常
     */
    public BusinessException(int errorCode, String message, String detail, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.detail = detail;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 基于错误码枚举的快捷构造（推荐使用）
     * 使用枚举中定义的错误码和消息
     */
    public BusinessException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null, null);
    }

    /**
     * 基于错误码枚举+自定义消息
     * 自定义信息存入detail
     */
    public BusinessException(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), errorCode.getMessage(), message, null);
    }

    /**
     * 基于错误码枚举+自定义消息+详情
     * 更详细的错误信息描述
     */
    public BusinessException(ErrorCode errorCode, String message, String detail) {
        this(errorCode.getCode(), message, detail, null);
    }

    /**
     * 基于错误码枚举+原始异常
     * 保留枚举错误码和消息，附加原始异常信息
     */
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        this(errorCode.getCode(), errorCode.getMessage(),
                cause != null ? cause.getMessage() : null, cause);
    }

    /**
     * 基于错误码枚举+自定义消息+原始异常
     * 灵活组合错误信息和异常链
     */
    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        this(errorCode.getCode(), message, cause != null ? cause.getMessage() : null, cause);
    }

    /**
     * 自定义错误码+消息（非枚举定义的临时错误码）
     */
    public BusinessException(int errorCode, String message) {
        this(errorCode, message, null, null);
    }

    /**
     * 自定义错误码+消息+详情
     */
    public BusinessException(int errorCode, String message, String detail) {
        this(errorCode, message, detail, null);
    }
}