package com.hncs.world.common;

/**
 * 自定义错误码
 *
 * @author peng
 */
public enum ErrorCode {
    // 成功
    SUCCESS(200000, "操作成功"),

    // 客户端错误（4xx开头）
    INVALID_PARAMS(400000, "输入参数错误"),

    // 服务器错误（5xx开头）
    SERVER_ERROR(500000, "系统繁忙，请稍后再试"),
    PY_NOT_FOUND(500001, "Python脚本未找到"),
    PY_PARSE_ERROR(500002, "Python解析错误"),
    PY_RETURN_EMPTY(500003, "Python返回数据为空"),
    PY_EXECUTE_FAILED(500004, "Python执行失败");


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
