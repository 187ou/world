<template>
  <a-menu mode="horizontal" class="border-b border-gray-300">
    <a-menu-item key="file" @click="goHome">文件</a-menu-item>
    <a-sub-menu key="home" title="开始">
      <a-menu-item-group title="剪贴板">
        <a-menu-item key="paste">粘贴</a-menu-item>
        <a-menu-item key="cut">剪切</a-menu-item>
        <a-menu-item key="copy">复制</a-menu-item>
      </a-menu-item-group>
      <a-menu-item-group title="字体">
        <a-menu-item key="bold">加粗</a-menu-item>
        <a-menu-item key="italic">倾斜</a-menu-item>
        <a-menu-item key="underline">下划线</a-menu-item>
      </a-menu-item-group>
      <a-menu-item-group title="段落">
        <a-menu-item key="bullets">项目符号</a-menu-item>
        <a-menu-item key="numbering">编号</a-menu-item>
        <a-menu-item key="alignment">对齐方式</a-menu-item>
      </a-menu-item-group>
      <a-menu-item-group title="样式">
        <a-menu-item key="styles">样式</a-menu-item>
      </a-menu-item-group>
      <a-menu-item-group title="编辑">
        <a-menu-item key="find">查找</a-menu-item>
        <a-menu-item key="replace">替换</a-menu-item>
        <a-menu-item key="select">选择</a-menu-item>
      </a-menu-item-group>
    </a-sub-menu>
    <a-sub-menu key="insert" title="插入">
      <a-menu-item key="pages">页面</a-menu-item>
      <a-menu-item key="tables">表格</a-menu-item>
      <a-menu-item key="pictures">图片</a-menu-item>
      <a-menu-item key="shapes">形状</a-menu-item>
      <a-menu-item key="charts">图表</a-menu-item>
      <a-menu-item key="header">页眉和页脚</a-menu-item>
      <a-menu-item key="text-box">文本框</a-menu-item>
      <a-menu-item key="symbols">符号</a-menu-item>
    </a-sub-menu>
    <a-sub-menu key="design" title="设计">
      <a-menu-item key="themes">主题</a-menu-item>
      <a-menu-item key="colors">颜色</a-menu-item>
      <a-menu-item key="fonts">字体</a-menu-item>
      <a-menu-item key="paragraph-spacing">段落间距</a-menu-item>
      <a-menu-item key="effects">效果</a-menu-item>
      <a-menu-item key="page-color">页面颜色</a-menu-item>
    </a-sub-menu>
    <a-sub-menu key="layout" title="版式">
      <a-menu-item key="margins">页边距</a-menu-item>
      <a-menu-item key="orientation">方向</a-menu-item>
      <a-menu-item key="size">纸张大小</a-menu-item>
      <a-menu-item key="columns">分栏</a-menu-item>
      <a-menu-item key="breaks">分隔符</a-menu-item>
      <a-menu-item key="line-numbers">行号</a-menu-item>
      <a-menu-item key="hyphenation">断字</a-menu-item>
    </a-sub-menu>
    <a-sub-menu key="references" title="引用">
      <a-menu-item key="toc">目录</a-menu-item>
      <a-menu-item key="footnotes">脚注</a-menu-item>
      <a-menu-item key="citations">引文与书目</a-menu-item>
      <a-menu-item key="captions">题注</a-menu-item>
      <a-menu-item key="index">索引</a-menu-item>
      <a-menu-item key="table-of-authorities">引文目录</a-menu-item>
    </a-sub-menu>
    <a-sub-menu key="mailings" title="邮寄">
      <a-menu-item key="envelopes">信封</a-menu-item>
      <a-menu-item key="labels">标签</a-menu-item>
      <a-menu-item key="mail-merge">邮件合并</a-menu-item>
    </a-sub-menu>
    <a-sub-menu key="review" title="审核">
      <a-menu-item key="proofing">校对</a-menu-item>
      <a-menu-item key="comments">批注</a-menu-item>
      <a-menu-item key="tracking">修订</a-menu-item>
      <a-menu-item key="changes">更改</a-menu-item>
      <a-menu-item key="compare">比较</a-menu-item>
      <a-menu-item key="protect">保护</a-menu-item>
    </a-sub-menu>
    <a-sub-menu key="view" title="视图">
      <a-menu-item key="print-layout">打印布局</a-menu-item>
      <a-menu-item key="web-layout">Web 版式</a-menu-item>
      <a-menu-item key="outline">大纲</a-menu-item>
      <a-menu-item key="draft">草稿</a-menu-item>
      <a-menu-item key="ruler">标尺</a-menu-item>
      <a-menu-item key="gridlines">网格线</a-menu-item>
      <a-menu-item key="navigation-pane">导航窗格</a-menu-item>
    </a-sub-menu>
    <a-sub-menu key="help" title="帮助">
      <a-menu-item key="search">搜索</a-menu-item>
      <a-menu-item key="feedback">反馈</a-menu-item>
      <a-menu-item key="updates">检查更新</a-menu-item>
    </a-sub-menu>
    <a-sub-menu key="man" title="Man">
      <a-menu-item key="picture" @click="handleAuthorImageClick">作者图片</a-menu-item>
      <a-menu-item key="phone" @click="handleAuthorBGMClick">作者电话</a-menu-item>
    </a-sub-menu>
  </a-menu>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
// 顶部菜单栏逻辑
const handleAuthorImageClick = () => {
  window.open('https://ts2.tc.mm.bing.net/th/id/OIP-C.5k1S6d07N77EBKMDT7BaiAHaHx?rs=1&pid=ImgDetMain&o=7&rm=3', '_blank');
};

const handleAuthorBGMClick = () => {
  window.open('https://www.thepaper.cn/newsDetail_forward_22986505', '_blank');
};

const router = useRouter();
const goHome = () => {
  router.push('/');
};
</script>
