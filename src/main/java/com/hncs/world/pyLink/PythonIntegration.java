package com.hncs.world.pyLink;

import com.hncs.world.common.ErrorCode;
import com.hncs.world.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            // 获取错误输出
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            String errorLine;
            StringBuilder errorResult = new StringBuilder();
            while ((errorLine = errorReader.readLine()) != null) {
                errorResult.append(errorLine).append("\n");
            }

            // 等待进程结束
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Python脚本执行失败，错误码: {}，错误信息: {}", exitCode, errorResult);
                throw new BusinessException(ErrorCode.PY_PARSE_ERROR);
            }

            return result.toString().trim();

        } catch (Exception e) {
            log.error("Python脚本执行失败");
            throw new BusinessException(ErrorCode.PY_PARSE_ERROR);
        }
    }

}


