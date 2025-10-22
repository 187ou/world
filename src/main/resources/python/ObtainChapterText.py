# chapter_content.py
import io
import sys
import requests
from bs4 import BeautifulSoup
import json
import urllib.parse
import cloudscraper
import re
import time


def get_chapter_content(chapter_url):
    """
    通过章节链接获取章节内容
    返回适配OpenBookVo类的JSON数据
    """
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36 Edg/140.0.0.0",
        "Referer": "https://www.owlook.com.cn/",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
        "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8",
    }

    # 使用cloudscraper绕过Cloudflare防护
    scraper = cloudscraper.create_scraper()

    try:
        print(f"正在获取章节内容: {chapter_url}", file=sys.stderr)

        # 获取章节内容页面
        response = scraper.get(chapter_url, headers=headers, timeout=30)
        response.encoding = 'utf-8'

        soup = BeautifulSoup(response.text, "html.parser")

        """ # 调试：保存HTML内容到文件以便分析
        with open('debug_content.html', 'w', encoding='utf-8') as f:
            f.write(soup.prettify())
        print("已保存HTML内容到debug_content.html", file=sys.stderr) """

        # 提取章节名称 - 尝试多种选择器
        chapter_name = "未知章节"
        chapter_name_selectors = [
            'h1',
            '.title',
            '#content_name',
            'div.title',
            'head title'
        ]

        for selector in chapter_name_selectors:
            element = soup.select_one(selector)
            if element:
                chapter_name = element.get_text(strip=True)
                print(f"找到章节名称: {chapter_name}", file=sys.stderr)
                break

        # 提取章节内容 - 根据实际HTML结构调整选择器
        content_text = ""
        content_selectors = [
            '#nr_content',
            '#content',
            '.content',
            '.novel_content',
            '.text-content',
            '.chapter-content',
            '#BookView',
            '#intContent'
        ]

        content_element = None
        for selector in content_selectors:
            content_element = soup.select_one(selector)
            if content_element:
                print(f"找到内容元素，选择器: {selector}", file=sys.stderr)
                break

        if content_element:
            # 清理内容中的广告和无关标签
            unwanted_tags = ['script', 'style', 'iframe', 'ins', 'ad']
            for tag in unwanted_tags:
                for element in content_element.find_all(tag):
                    element.decompose()

            # 移除特定class的元素（通常是广告）
            unwanted_classes = ['ad', 'ads', 'advertisement', 'banner', 'promo']
            for class_name in unwanted_classes:
                for element in content_element.find_all(class_=re.compile(class_name, re.I)):
                    element.decompose()

            # 获取文本内容
            content_text = content_element.get_text(separator='\n', strip=True)

            # 清理多余的空行和空格
            content_text = re.sub(r'\n\s*\n', '\n\n', content_text)
            content_text = re.sub(r'[ \t]+', ' ', content_text)
            content_text = content_text.strip()

            print(f"提取到文本长度: {len(content_text)}", file=sys.stderr)
        else:
            # 如果没有找到标准的内容区域，尝试提取所有文本
            print("未找到标准内容区域，尝试提取body文本", file=sys.stderr)
            body = soup.find('body')
            if body:
                # 移除不需要的元素
                for tag in ['script', 'style', 'nav', 'header', 'footer']:
                    for element in body.find_all(tag):
                        element.decompose()

                content_text = body.get_text(separator='\n', strip=True)
                content_text = re.sub(r'\n\s*\n', '\n\n', content_text)
                content_text = content_text.strip()

        # 如果内容为空，尝试其他方法
        if not content_text or len(content_text) < 50:
            print("内容过短，尝试备用提取方法", file=sys.stderr)
            # 尝试查找包含大量文本的div
            all_divs = soup.find_all('div')
            for div in all_divs:
                text = div.get_text(strip=True)
                if len(text) > 200:  # 假设内容长度大于200字符
                    content_text = text
                    break

        # 计算文本大小（字符数）
        chapter_text_size = str(len(content_text))

        # 构建适配OpenBookVo的数据
        chapter_data = {
            "chapterName": chapter_name,
            "chapterTxt": content_text,
            "chapterTxtSize": chapter_text_size
        }

        return chapter_data

    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        return {
            "chapterName": "错误",
            "chapterTxt": f"获取章节内容时出错: {str(e)}",
            "chapterTxtSize": "0"
        }


def get_chapter_content_with_selenium(chapter_url):
    """备用方案：使用Selenium获取章节内容"""
    try:
        from selenium import webdriver
        from selenium.webdriver.chrome.options import Options
        from selenium.webdriver.chrome.service import Service
        from webdriver_manager.chrome import ChromeDriverManager

        print("使用Selenium备用方案...", file=sys.stderr)

        # 配置Chrome选项
        chrome_options = Options()
        chrome_options.add_argument('--headless')
        chrome_options.add_argument('--disable-gpu')
        chrome_options.add_argument('--no-sandbox')
        chrome_options.add_argument('--disable-dev-shm-usage')
        chrome_options.add_argument('--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36')

        try:
            # 自动下载和管理ChromeDriver
            service = Service(ChromeDriverManager().install())
            driver = webdriver.Chrome(service=service, options=chrome_options)
        except Exception as e:
            print(f"自动ChromeDriver失败: {e}", file=sys.stderr)
            return None

        try:
            driver.get(chapter_url)
            time.sleep(5)  # 等待页面加载

            # 获取处理后的HTML
            html_content = driver.page_source
            soup = BeautifulSoup(html_content, 'html.parser')

            # 提取章节名称
            chapter_name = "未知章节"
            title_selectors = ['h1', '.title', '#content_name']
            for selector in title_selectors:
                element = soup.select_one(selector)
                if element:
                    chapter_name = element.get_text(strip=True)
                    break

            # 提取章节内容
            content_element = None
            content_selectors = ['#nr_content', '#content', '.content', '#intContent']
            for selector in content_selectors:
                content_element = soup.select_one(selector)
                if content_element:
                    break

            if content_element:
                # 清理内容
                for tag in ['script', 'style']:
                    for element in content_element.find_all(tag):
                        element.decompose()

                content_text = content_element.get_text(separator='\n', strip=True)
                content_text = re.sub(r'\n\s*\n', '\n\n', content_text)
            else:
                content_text = "未能获取章节内容"

            # 计算文本大小
            chapter_text_size = str(len(content_text))

            # 构建适配OpenBookVo的数据
            chapter_data = {
                "chapterName": chapter_name,
                "chapterTxt": content_text,
                "chapterTxtSize": chapter_text_size
            }

            return chapter_data

        finally:
            driver.quit()

    except Exception as e:
        print(f"Selenium error: {e}", file=sys.stderr)
        return None


if __name__ == "__main__":
    # 解决中文乱码
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

    # 从命令行参数获取章节URL
    if len(sys.argv) > 1:
        chapter_url = sys.argv[1]

        # 首先尝试使用cloudscraper
        result = get_chapter_content(chapter_url)

        # 如果cloudscraper失败或内容过短，尝试使用Selenium
        if not result or len(result.get("chapterTxt", "")) < 100:
            selenium_result = get_chapter_content_with_selenium(chapter_url)
            if selenium_result:
                result = selenium_result

        # 输出JSON结果，适配OpenBookVo类
        print(json.dumps(result, ensure_ascii=False))