# 环境配置说明

## 系统要求
- Python 3.7+
- Java 8+
- Maven 3.6+

## Python环境配置

### 1. 安装依赖包
```bash
pip install requests beautifulsoup4 cloudscraper selenium
```

### 2. Chrome驱动（可选）
如需使用Selenium备用方案，请安装Chrome浏览器并下载对应版本的ChromeDriver。

## 项目配置

### 1. Python脚本位置
将Python脚本放置在项目的 `resources/python/` 目录下：
- `booksSearch.py` - 小说搜索脚本
- `chapterSearch.py` - 章节搜索脚本

### 2. Maven依赖
确保项目中包含以下依赖：
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

### 3. 日志配置
配置日志系统以查看运行状态和调试信息。

## 运行说明
项目启动后会自动加载Python脚本，无需额外配置。确保Python环境变量已正确设置。