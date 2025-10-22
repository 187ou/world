package com.hncs.world.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VerificationCodeUtil {

    private final Map<String, CodeInfo> codeCache = new ConcurrentHashMap<>();
    // 新增：记录邮箱最近发送验证码的时间（毫秒）
    private final Map<String, Long> lastSendTimeCache = new ConcurrentHashMap<>();

    @Value("${verification.code.expiration:300}")
    private Integer expiration; // 验证码有效期（秒）

    @Value("${verification.code.send-interval:60}")
    private Integer sendInterval; // 发送间隔（秒，默认60秒）

    /**
     * 生成验证码并缓存
     */
    public String generateCode(String email) {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        CodeInfo codeInfo = new CodeInfo(code, System.currentTimeMillis());
        codeCache.put(email, codeInfo);
        // 记录本次发送时间
        lastSendTimeCache.put(email, System.currentTimeMillis());
        return code;
    }

    /**
     * 校验验证码
     */
    public boolean validateCode(String email, String code) {
        CodeInfo codeInfo = codeCache.get(email);
        if (codeInfo == null) {
            return false;
        }

        // 检查是否过期
        if (System.currentTimeMillis() - codeInfo.getCreateTime() > expiration * 1000) {
            codeCache.remove(email);
            return false;
        }

        // 检查是否匹配（匹配后移除缓存，防止重复使用）
        if (codeInfo.getCode().equals(code)) {
            codeCache.remove(email);
            return true;
        }

        return false;
    }

    /**
     * 检查是否允许发送验证码（控制发送频率）
     * @param email 目标邮箱
     * @return true-允许发送，false-发送过于频繁
     */
    public boolean checkSendFrequency(String email) {
        Long lastSendTime = lastSendTimeCache.get(email);
        // 从未发送过，允许发送
        if (lastSendTime == null) {
            return true;
        }
        // 距离上次发送已超过间隔时间，允许发送
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastSendTime) > sendInterval * 1000;
    }

    public CodeInfo getCodeInfo(String email) {
        return codeCache.get(email);
    }

    @Data
    @AllArgsConstructor
    public static class CodeInfo {
        private String code;
        private Long createTime;
    }
}