package com.hncs.world.pyLink;

import com.hncs.world.common.ErrorCode;
import com.hncs.world.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Slf4j
public enum PyPath {
    SEARCH_BOOK_PY_PATH("python/booksSearch.py"),
    SEARCH_CHAPTER_PY_PATH("python/chapterSearch.py"),
    OBTAIN_CHAPTER_TEXT_PY_PATH("python/ObtainChapterText.py");

    private final String classPath;

    PyPath(String classPath) {
        this.classPath = classPath;
    }

    public String getPath() {
        try {
            // 1. 首先尝试从类路径加载
            ClassPathResource resource = new ClassPathResource(classPath);

            // 2. 如果资源存在且是文件系统路径
            if (resource.exists()) {
                File file = resource.getFile();
                if (file.exists() && file.isFile()) {
                    return file.getAbsolutePath();
                }
            }

            // 3. 如果无法直接访问文件（打包后环境），提取到临时文件
            return extractFromJar(resource);

        } catch (Exception e) {
            log.error("无法加载Python脚本: {}", classPath, e);
            throw new BusinessException(ErrorCode.PY_NOT_FOUND);
        }
    }

    /**
     * 从JAR包中提取资源到临时文件
     */
    private String extractFromJar(Resource resource) throws IOException {
        // 创建临时文件
        String[] pathParts = classPath.split("/");
        String filename = pathParts[pathParts.length - 1];
        File tempFile = File.createTempFile("python_", "_" + filename);
        tempFile.deleteOnExit(); // JVM退出时删除临时文件

        // 复制资源内容到临时文件
        try (InputStream inputStream = resource.getInputStream()) {
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile.getAbsolutePath();
    }

    /**
     * 检查Python脚本是否存在
     */
    public boolean exists() {
        try {
            ClassPathResource resource = new ClassPathResource(classPath);
            return resource.exists();
        } catch (Exception e) {
            return false;
        }
    }
}