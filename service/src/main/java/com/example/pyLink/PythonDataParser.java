package com.example.pyLink;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Python返回数据解析器
 */
@Slf4j
public class PythonDataParser<T> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 解析Python返回的标准JSON数据
     * @param pythonResult Python返回的JSON字符串
     * @param clazz 目标类型的Class对象
     * @return 解析后的对象列表
     */
    public List<T> parsePythonResult(String pythonResult, Class<T> clazz) {
        List<T> result = new ArrayList<>();

        if (pythonResult == null || pythonResult.trim().isEmpty()) {
            log.warn("Python返回数据为空");
            return result;
        }

        try {
            // 使用类型工厂构建集合类型，避免匿名内部类的复杂性
            result = objectMapper.readValue(pythonResult,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            log.info("成功解析到 {} 条数据", result.size());

        } catch (Exception e) {
            log.error("解析Python返回数据失败: {}, 原始数据: {}", e.getMessage(), pythonResult);
        }

        return result;
    }

    /**
     * 静态工具方法，方便直接调用
     * @param pythonResult Python返回的JSON字符串
     * @param clazz 目标类型的Class对象
     * @return 解析后的对象列表
     */
    public static <T> List<T> parse(String pythonResult, Class<T> clazz) {
        PythonDataParser<T> parser = new PythonDataParser<>();
        return parser.parsePythonResult(pythonResult, clazz);
    }
}
