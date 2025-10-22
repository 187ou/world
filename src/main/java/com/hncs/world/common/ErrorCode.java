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
    // 注册相关错误（401xx）
    REGISTER_USERNAME_DUPLICATE(401001, "用户名已存在"),
    REGISTER_EMAIL_DUPLICATE(401002, "邮箱已被注册"),
    REGISTER_PHONE_DUPLICATE(401003, "手机号已被注册"),
    // 登录相关错误（402xx）
    LOGIN_USER_NOT_EXIST(402001, "用户不存在"),
    LOGIN_PASSWORD_ERROR(402002, "密码错误"),
    LOGIN_ACCOUNT_DISABLED(402003, "账号已被禁用"),
    LOGIN_EMAIL_FORMAT_ERROR(402004, "邮箱格式不正确，请输入有效的邮箱地址"),
    // 验证码相关错误（403xx）
    VERIFY_EMAIL_UNREGISTERED(403001, "邮箱未注册"),
    VERIFY_EMAIL_FORMAT_ERROR(403002, "邮箱格式不正确，请输入有效的邮箱地址"),
    VERIFY_SEND_FAILED(403003, "验证码发送失败，请稍后重试"),
    VERIFY_CODE_INVALID(403004, "验证码错误或已过期"),
    VERIFY_FREQUENCY_LIMIT(403005,  "验证码发送过于频繁，请60秒后再试"),
    // 密码重置相关错误（404xx）
    RESET_USER_NOT_EXIST(404001, "用户不存在"),
    RESET_PASSWORD_SAME_AS_OLD(404002, "新密码不能与旧密码相同"),
    RESET_DB_UPDATE_FAIL(404003, "重置密码失败，请稍后重试"),
    // 用户查询相关错误（405xx）
    QUERY_USER_NOT_EXIST(405001, "用户不存在"),
    // 用户更新相关错误（406xx）
    UPDATE_USER_NOT_EXIST(406001, "用户不存在"),
    UPDATE_EMAIL_FORMAT_ERROR(406002, "邮箱格式不正确，请输入有效的邮箱地址"),
    UPDATE_EMAIL_DUPLICATE(406003, "邮箱已被注册"),
    UPDATE_SEX_INVALID(406004, "性别参数无效"),
    UPDATE_DB_FAIL(406005, "更新用户信息失败"),
    UPDATE_PHONE_DUPLICATE(406006, "手机号已被注册"),

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