import io
import sys
import requests
from bs4 import BeautifulSoup
import json
import urllib.parse
import cloudscraper


def get_chapters(input_data):
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36 Edg/140.0.0.0"
    }

    # 使用cloudscraper绕过Cloudflare防护
    scraper = cloudscraper.create_scraper()

    try:
        # 获取章节列表页面
        response = scraper.get(input_data, headers=headers)
        response.encoding = 'utf-8'

        soup = BeautifulSoup(response.text, "html.parser")

        # 提取小说基本信息
        base_url = soup.find('input', {'id': 'url'})
        novel_name = soup.find('input', {'id': 'novels_name'})

        base_url_value = base_url['value'] if base_url else ""
        novel_name_value = novel_name['value'] if novel_name else "未知小说"

        # 提取所有章节链接
        chapter_links = soup.select('.book_list ul li a')

        chapters = []

        for link in chapter_links:
            chapter_href = link.get('href')
            chapter_name = link.get_text(strip=True)

            # 构建完整的章节信息
            if chapter_href:
                # 如果已经是owllook_content格式，直接使用
                if 'owllook_content' in chapter_href:
                    chapter_url = f"https://www.owlook.com.cn{chapter_href}"
                else:
                    # 构建源URL
                    source_url = urllib.parse.urljoin(base_url_value, chapter_href)
                    # 构建owllook格式URL
                    chapter_url = f"https://www.owlook.com.cn/owllook_content?url={urllib.parse.quote(source_url)}&name={urllib.parse.quote(chapter_name)}&chapter_url={urllib.parse.quote(base_url_value)}&novels_name={urllib.parse.quote(novel_name_value)}"

                # 只记录章节标题和章节链接
                chapter = {
                    "chapterName": chapter_name,
                    "chapterLink": chapter_url
                }
                chapters.append(chapter)

        return chapters

    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        return []


def get_chapters_with_selenium(url):
    """备用方案：使用Selenium获取章节信息"""
    try:
        from selenium import webdriver
        from selenium.webdriver.chrome.options import Options
        import time

        # 配置Chrome选项
        chrome_options = Options()
        chrome_options.add_argument('--headless')
        chrome_options.add_argument('--disable-gpu')
        chrome_options.add_argument('--no-sandbox')
        chrome_options.add_argument('--disable-dev-shm-usage')

        driver = webdriver.Chrome(options=chrome_options)

        try:
            driver.get(url)
            time.sleep(3)  # 等待页面加载

            # 获取处理后的HTML
            html_content = driver.page_source
            soup = BeautifulSoup(html_content, 'html.parser')

            # 提取章节
            chapters = []
            chapter_links = soup.select('.book_list ul li a')

            for link in chapter_links:
                chapter_href = link.get('href')
                chapter_name = link.get_text(strip=True)

                if chapter_href:
                    # 构建完整URL
                    if chapter_href.startswith('/'):
                        chapter_url = f"https://www.owlook.com.cn{chapter_href}"
                    else:
                        chapter_url = chapter_href

                    # 只记录章节标题和章节链接
                    chapter = {
                        "chapterName": chapter_name,
                        "chapterLink": chapter_url
                    }
                    chapters.append(chapter)

            return chapters

        finally:
            driver.quit()

    except Exception as e:
        print(f"Selenium error: {e}", file=sys.stderr)
        return []


if __name__ == "__main__":
    # 解决中文乱码
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

    # 从命令行参数获取小说目录URL
    if len(sys.argv) > 1:
        novel_url = sys.argv[1]

        decoded_data = urllib.parse.unquote(novel_url)
        book_info = json.loads(decoded_data)
        book_link = book_info.get("bookLink")
        book_name = book_info.get("bookName")

        print(f"开始爬取: {book_name}", file=sys.stderr)
        print(f"目标链接: {book_link}", file=sys.stderr)

        # 首先尝试使用cloudscraper
        result = get_chapters(book_link)

        # 如果cloudscraper失败，尝试使用Selenium
        if not result:
            result = get_chapters_with_selenium(book_link)

        # 输出JSON结果
        print(json.dumps(result, ensure_ascii=False))