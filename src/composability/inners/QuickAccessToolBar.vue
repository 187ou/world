<template>
  <div class="flex items-center p-2 bg-white border-b border-gray-300">
    <!-- 文件操作按钮 -->
    <ToolButton title="新建" class="mr-2">
      <FileAddOutlined />
    </ToolButton>
    <ToolButton title="打开" class="mr-2">
      <FolderOpenOutlined />
    </ToolButton>
    <ToolButton title="保存" class="mr-2">
      <SaveOutlined />
    </ToolButton>

    <!-- 编辑操作按钮 -->
    <ToolButton title="撤销" class="mr-2">
      <UndoOutlined />
    </ToolButton>
    <ToolButton title="重做" class="mr-2">
      <RedoOutlined />
    </ToolButton>

    <!-- 其他操作按钮 -->
    <ToolButton title="打印" class="mr-2">
      <PrinterOutlined />
    </ToolButton>
    <ToolButton title="共享" class="mr-2">
      <ShareAltOutlined />
    </ToolButton>

    <!-- 缩放控制 -->
    <ZoomControl
      :zoom="editorStore.zoom"
      @update:zoom="updateZoom"
      class="mr-2"
    />

    <!-- 字体控制 -->
    <FontSelector v-model="editorStore.fontFamily" />
    <FontSizeSelector v-model="editorStore.fontSize" />

    <!-- 文本格式按钮 -->
    <FormatButton
      title="加粗"
      :is-active="editorStore.isBold"
      @click="editorStore.toggleBold()"
      class="mr-1"
    >
      <BoldOutlined />
    </FormatButton>
    <FormatButton
      title="倾斜"
      :is-active="editorStore.isItalic"
      @click="editorStore.toggleItalic()"
      class="mr-1"
    >
      <ItalicOutlined />
    </FormatButton>
    <ToolButton title="下划线" class="mr-1">
      <UnderlineOutlined />
    </ToolButton>
    <ToolButton title="删除线" class="mr-1">
      <StrikethroughOutlined />
    </ToolButton>
    <ToolButton title="下标" class="mr-1">
      <SubnodeOutlined />
    </ToolButton>
    <ToolButton title="上标" class="mr-1">
      <VerticalAlignTopOutlined />
    </ToolButton>
    <ToolButton title="文本突出显示颜色" class="mr-1">
      <HighlightOutlined />
    </ToolButton>
    <ToolButton title="字体颜色" class="mr-1">
      <FontColorsOutlined />
    </ToolButton>

    <!-- 对齐按钮 -->
    <ToolButton title="左对齐" class="mr-1">
      <AlignLeftOutlined />
    </ToolButton>
    <ToolButton title="居中对齐" class="mr-1">
      <AlignCenterOutlined />
    </ToolButton>
    <ToolButton title="右对齐" class="mr-1">
      <AlignRightOutlined />
    </ToolButton>
    <ToolButton title="两端对齐" class="mr-1">
      <PicCenterOutlined />
    </ToolButton>

    <!-- 其他格式按钮 -->
    <ToolButton title="行距" class="mr-1">
      <LineHeightOutlined />
    </ToolButton>
    <ToolButton title="项目符号" class="mr-1">
      <UnorderedListOutlined />
    </ToolButton>
    <ToolButton title="编号" class="mr-1">
      <OrderedListOutlined />
    </ToolButton>
    <ToolButton title="多级列表" class="mr-1">
      <MenuOutlined />
    </ToolButton>
    <ToolButton title="减少缩进量" class="mr-1">
      <RollbackOutlined />
    </ToolButton>
    <ToolButton title="增加缩进量" class="mr-1">
      <ArrowRightOutlined />
    </ToolButton>
    <ToolButton title="排序" class="mr-1">
      <SortAscendingOutlined />
    </ToolButton>
    <ToolButton title="显示/隐藏编辑标记" class="mr-1">
      <EyeOutlined />
    </ToolButton>
  </div>
</template>

<script setup lang="ts">
import { useEditorStore } from '@/stores/editorStore'
import {
  SaveOutlined,
  UndoOutlined,
  RedoOutlined,
  PrinterOutlined,
  BoldOutlined,
  ItalicOutlined,
  UnderlineOutlined,
  AlignLeftOutlined,
  AlignCenterOutlined,
  AlignRightOutlined,
  UnorderedListOutlined,
  OrderedListOutlined,
  FileAddOutlined,
  FolderOpenOutlined,
  ShareAltOutlined,
  StrikethroughOutlined,
  SubnodeOutlined,
  HighlightOutlined,
  FontColorsOutlined,
  PicCenterOutlined,
  LineHeightOutlined,
  SortAscendingOutlined,
  EyeOutlined,
  MenuOutlined,
  RollbackOutlined,
  ArrowRightOutlined,
  VerticalAlignTopOutlined,
} from '@ant-design/icons-vue';

// 组件导入
import ToolButton from '@/components/inners/ToolButton.vue';
import FontSelector from '@/components/inners/FontSelector.vue';
import FontSizeSelector from '@/components/inners/FontSizeSelector.vue';
import FormatButton from '@/components/inners/FormatButton.vue';
import ZoomControl from '@/components/inners/ZoomControl.vue';

const editorStore = useEditorStore();

const updateZoom = (newZoom: number) => {
  editorStore.zoom = newZoom;
};
</script>
