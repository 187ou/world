import io
import sys
import requests
from bs4 import BeautifulSoup
import json

def tmp(input_data):
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36 Edg/140.0.0.0"
    }
    search_url = f"https://www.owlook.com.cn/search?wd={input_data}"
    respone = requests.get(search_url, headers=headers)
    # print(respone.text)
    soup = BeautifulSoup(respone.text, "html.parser")
    all_url = soup.select("a[target='_blank']:not([rel='nofollow'])")

    books = []  # 改为存储字典对象
    for url in all_url:
        # 提取链接
        href = url.get("href")
        # 提取小说名（从文本中分割获取）
        text_parts = url.text.split('--')
        if len(text_parts) >= 2:
            novel_name = text_parts[1]  # 获取中间部分作为小说名
        else:
            novel_name = url.text.strip()  # 如果格式不符合预期，使用全部文本

        if href:
            # 创建字典对象，对应Java的SearchBookVo类
            book = {
                "bookName": novel_name,
                "bookLink": "https://www.owlook.com.cn" + href
            }
            books.append(book)

    return books


if __name__ == "__main__":
    # 解决中文乱码
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

    # 从命令行参数获取数据
    if len(sys.argv) > 1:
        input_data = sys.argv[1]
        result = tmp(input_data)
        # 直接输出JSON数组，不需要indent，确保是紧凑格式
        print(json.dumps(result, ensure_ascii=False))