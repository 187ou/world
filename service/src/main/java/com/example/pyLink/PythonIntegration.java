package com.example.pyLink;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonIntegration {
    public static String callPythonScript(String inputData) {
        try {
            // 准备Python命令
            String[] command = {"python", "E:\\Code\\Python\\pythonProject\\test\\urlGet.py", inputData};

            Process process = Runtime.getRuntime().exec(command);

            // 获取Python脚本的输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // 等待进程结束
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python脚本执行失败");
            }

            return result.toString();

        } catch (Exception e) {
            throw new RuntimeException("调用Python脚本失败", e);
        }
    }

    public static void main(String[] args) {
        String javaData = "斗破苍穹";
        String pythonResult = callPythonScript(javaData);
        System.out.println("Python处理结果: " + pythonResult);
    }
}
