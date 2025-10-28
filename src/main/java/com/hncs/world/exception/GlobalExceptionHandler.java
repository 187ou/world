package com.hncs.world.exception;

import com.hncs.world.common.ErrorCode;
import com.hncs.world.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理项目中抛出的各类异常，返回标准化的Result对象
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理404 - 接口不存在
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result<?>> handleNoHandlerFound(NoHandlerFoundException e) {
        // 日志记录：访问不存在的路径，用warn级别
        log.info("访问不存在的路径: {} {}", e.getHttpMethod(), e.getRequestURL());

        Result<?> result = Result.error(ErrorCode.NOT_FOUND.getCode(), "请求的接口不存在");
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理业务异常（自定义异常）
     * @param e 业务异常对象
     * @return 统一返回结果
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        // 日志记录：包含错误码、提示、详情，便于定位问题
        log.info("业务异常: [{}] 提示={}, 详情={}", e.getErrorCode(), e.getMessage(), e.getDetail(), e);
        return Result.error(e); // 调用Result的error(BusinessException)方法
    }

    /**
     * 处理DTO参数校验失败异常（如@NotNull、@Email等注解校验失败）
     * @param e 参数校验异常对象
     * @return 统一返回结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        // 拼接所有字段错误（格式：字段名: 错误提示, 字段名: 错误提示）
        String errorMsg = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        // 日志记录：参数校验失败属于客户端问题，用warn级别
        log.info("DTO参数校验失败: {}", errorMsg);

        // 调用新增的error(int, String)方法，返回具体错误信息
        return Result.error(ErrorCode.INVALID_PARAMS.getCode(), errorMsg);
    }

    /**
     * 处理系统异常（非预期异常，如NPE、数据库异常等）
     * @param e 系统异常对象
     * @return 统一返回结果
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleSystemException(Exception e) {
        // 日志记录：系统异常需详细堆栈，用error级别
        log.info("系统异常: 异常信息={}", e.getMessage(), e);

        // 返回通用系统错误，不暴露敏感信息（如堆栈）
        return Result.error(ErrorCode.SERVER_ERROR);
    }
}