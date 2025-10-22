# world文档项目

该项目是一个实现在文档内看小说的工具，包含小说搜索、章节列表获取和章节内容提取等功能。

## 关键功能模块

### 1. 小说搜索 (BooksSearch.py)
- 根据关键词搜索小说
- 返回小说名称和对应的链接
- 数据格式适配SearchBookVo类

### 2. 章节列表获取 (ChapterSearch.py)
- 根据小说链接获取章节列表
- 过滤掉请假和通知类章节
- 生成适配的章节链接

### 3. 章节内容提取 (ObtainChapterText.py)
- 根据章节链接获取章节内容
- 支持多种页面结构的解析
- 包含内容净化处理（去除广告、多余标签等）
- 提供cloudscraper和Selenium两种获取方式，提高成功率
- 数据格式适配OpenBookVo类

## 使用方法

通过前端传递对应参数即可，除此之外你也可以直接调用脚本

### 小说搜索
```bash
python BooksSearch.py "搜索关键词"
```

### 获取章节列表
```bash
python ChapterSearch.py "小说详情页URL"
```

### 获取章节内容
```bash
python ObtainChapterText.py "章节URL"
```

## 依赖库

- beautifulsoup4: 用于HTML解析
- cloudscraper: 用于绕过Cloudflare防护
- requests: 用于HTTP请求
- selenium (可选): 作为获取章节内容的备用方案
- webdriver-manager (可选): 配合selenium使用，管理浏览器驱动

### 安装方式
可通过pip安装所需依赖：
```bash
pip install beautifulsoup4 cloudscraper requests selenium webdriver-manager
```

## 注意事项

1. 本项目仅用于学习和研究目的
2. 使用时请遵守目标网站的robots协议
3. 爬取频率不宜过高，以免给目标网站造成负担
4. 本项目只针对一个网站进行爬取
