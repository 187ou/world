package com.hncs.world.common;

import com.hncs.world.exception.BusinessException;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String description; //错误信息
    private String msg; //响应信息
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object, String msg) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        Result<T> result = new Result<>();
        result.msg = errorCode.getMessage();
        result.code = errorCode.getCode();
        return result;
    }

    public static <T> Result<T> error(BusinessException e) {
        Result<T> result = new Result<>();
        result.msg = e.getMessage();
        result.code = e.getErrorCode();
        result.description = e.getDetail();
        return result;
    }

    //    用于DTO参数校验，返回具体字段错误
    public static <T> Result<T> error(int code, String customMsg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = customMsg;
        result.description = null;
        return result;
    }

}
