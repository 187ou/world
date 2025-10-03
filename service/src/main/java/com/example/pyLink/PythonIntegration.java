package com.example.pyLink;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * java调用python脚本类
 */
@Slf4j
@Component
public class PythonIntegration {
    public static String callPythonScript(String inputData, String path) {
        try {
            log.info("传递给py的Json: {}", inputData);

            // 准备Python命令
            String[] command = {"python", path, inputData};

            Process process = Runtime.getRuntime().exec(command);

            // 获取Python脚本的输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8"));

            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            // 获取错误输出
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), "UTF-8"));
            String errorLine;
            StringBuilder errorResult = new StringBuilder();
            while ((errorLine = errorReader.readLine()) != null) {
                errorResult.append(errorLine).append("\n");
            }

            // 等待进程结束
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python脚本执行失败: " + errorResult.toString());
            }

            return result.toString().trim();

        } catch (Exception e) {
            throw new RuntimeException("调用Python脚本失败", e);
        }
    }

}


