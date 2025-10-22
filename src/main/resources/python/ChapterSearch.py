import io
import json
import os
import sys
import time
import urllib

import cloudscraper
from bs4 import BeautifulSoup


def get_chapters(input_data):
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36 Edg/140.0.0.0"
    }

    # 使用cloudscraper绕过Cloudflare防护
    scraper = cloudscraper.create_scraper()

    try:
        # 设置10秒的超时
        response = scraper.get(input_data, headers=headers, timeout=10)
        response.encoding = 'utf-8'

        """ # 创建保存HTML文件的目录
        if not os.path.exists('html_pages'):
            os.makedirs('html_pages')

        # 生成文件名（使用时间戳避免重复）
        timestamp = int(time.time())
        filename = f"html_pages/novel_page_{timestamp}.html"

        # 保存HTML内容到文件
        with open(filename, 'w', encoding='utf-8') as f:
            f.write(response.text)

        print(f"HTML内容已保存到: {filename}", file=sys.stderr) """

        soup = BeautifulSoup(response.text, "html.parser")

        # 提取小说基本信息
        base_url = soup.find('input', {'id': 'url'})['value']
        novel_name = soup.find('input', {'id': 'novels_name'})['value']

        # 方法1：直接选择所有li下的a标签
        chapter_links = soup.select('li a')
        if not chapter_links:  # 如果li下没有a标签
            chapter_links = soup.select('dd a')  # 则尝试爬取dd标签下的a标签

        # 需要先处理每个li
        # chapter_links = soup.find_all('li').find_all('a')

        chapters = []
        for i, link in enumerate(chapter_links[:100]):  # Limit to the first 100 chapters
            chapter_href = link.get('href')
            chapter_name = link.get_text(strip=True)

            # 过滤掉非章节链接
            if not chapter_href or not chapter_name:
                continue
            # 过滤掉请假和通知章节
            if '请假' in chapter_name or '通知' in chapter_name:
                continue

            chapter_url = urllib.parse.urljoin(base_url, chapter_href)
            formatted_url = f"https://www.owlook.com.cn/owllook_content?url={urllib.parse.quote(chapter_url)}&name={urllib.parse.quote(chapter_name)}&chapter_url={urllib.parse.quote(base_url)}&novels_name={urllib.parse.quote(novel_name)}"

            # 只记录章节标题和章节链接
            chapter = {
                "chapterName": chapter_name,
                "chapterLink": formatted_url
            }
            chapters.append(chapter)

            # 限制只爬取前100个章节
            if len(chapters) >= 100:
                break

        return chapters

    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        return []


if __name__ == "__main__":
        # 解决中文乱码
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

    if len(sys.argv) > 1:
        novel_url = sys.argv[1]

        book_link = urllib.parse.unquote(novel_url)

        # 首先尝试使用cloudscraper
        result = get_chapters(book_link)

        # 输出JSON结果
        print(json.dumps(result, ensure_ascii=False))