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
    //用户信息错误
    USER_NOT_FOUND(401001,"用户不存在"),
    //账号相关错误
    LOGIN_ACCOUNT_DISABLED(402001, "账号错误"),
    // 验证码相关错误（403xx）
    VERIFY_CODE_INVALID(403001, "验证错误"),
    //接口错误（404xx）
    NOT_FOUND(404001, "接口错误"),
    // 密码重置相关错误（405xx）
    RESET_DB_UPDATE_FAIL(405001, "重置密码失败，请稍后重试"),
    // 用户更新相关错误（406xx）
    UPDATE_DB_FAIL(406001, "更新用户信息失败"),

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
