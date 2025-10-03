package com.example.common.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Json转换工具类
 */
@Slf4j
public class JacksonUntil extends ObjectMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将JSON字符串转换为对象列表
     *
     * @param jsonString JSON字符串
     * @param clazz 对象类型
     * @return 对象列表
     */
    public static <T> List<T> parseJsonToList(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            log.error("JSON转换失败: {}", e.getMessage(), e);
            throw new RuntimeException("JSON转换失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param object 待转换对象
     * @return JSON字符串
     */
    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("对象转JSON失败: {}", e.getMessage(), e);
            throw new RuntimeException("对象转JSON失败: " + e.getMessage(), e);
        }
    }
}
