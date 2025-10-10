<template>
  <div class="w-60 bg-gradient-to-b from-white to-yellow-50/50 border-l border-blue-200 p-4 overflow-y-auto">
    <a-tabs>
      <a-tab-pane key="1" tab="格式">
        <a-form layout="vertical">
          <a-form-item label="字体">
            <a-input />
          </a-form-item>
          <a-form-item label="字号">
            <a-input-number />
          </a-form-item>
          <a-form-item label="颜色">
            <!-- <a-color-picker /> -->
          </a-form-item>
          <a-form-item label="效果">
            <a-checkbox-group>
              <a-checkbox value="bold">加粗</a-checkbox>
              <a-checkbox value="italic">倾斜</a-checkbox>
              <a-checkbox value="underline">下划线</a-checkbox>
            </a-checkbox-group>
          </a-form-item>
        </a-form>
      </a-tab-pane>
      <a-tab-pane key="2" tab="段落">
        <a-form layout="vertical">
          <a-form-item label="对齐方式">
            <a-radio-group>
              <a-radio value="left">左对齐</a-radio>
              <a-radio value="center">居中</a-radio>
              <a-radio value="right">右对齐</a-radio>
              <a-radio value="justify">两端对齐</a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="间距">
            <a-input-number addon-after="磅" />
          </a-form-item>
        </a-form>
      </a-tab-pane>
      <a-tab-pane key="3" tab="页面设置">
        <a-form layout="vertical">
          <a-form-item label="页边距">
            <a-input-group compact>
              <a-input style="width: 25%" addon-before="上" />
              <a-input style="width: 25%" addon-before="下" />
              <a-input style="width: 25%" addon-before="左" />
              <a-input style="width: 25%" addon-before="右" />
            </a-input-group>
          </a-form-item>
          <a-form-item label="方向">
            <a-radio-group>
              <a-radio value="portrait">纵向</a-radio>
              <a-radio value="landscape">横向</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-form>
      </a-tab-pane>
      <a-tab-pane key="4" tab="导航">
        <h3 class="font-bold mb-2 text-blue-800">导航</h3>
        <a-tree :tree-data="treeData" />
      </a-tab-pane>
      <a-tab-pane key="5" tab="样式">
        <h3 class="font-bold mb-2 text-blue-800">样式</h3>
        <a-list :data-source="styles">
          <template #renderItem="{ item }">
            <a-list-item>{{ item }}</a-list-item>
          </template>
        </a-list>
      </a-tab-pane>
      <a-tab-pane key="6" tab="批注">
        <h3 class="font-bold mb-2 text-blue-800">批注</h3>
        <a-list :data-source="comments">
          <template #renderItem="{ item }">
            <a-list-item>{{ item }}</a-list-item>
          </template>
        </a-list>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const treeData = ref([
  {
    title: '示例文档标题',
    key: '0',
    children: [
      { title: '第一节：引言', key: '0-0' },
      { title: '第二节：数据表格', key: '0-1' },
      { title: '第三节：图片和形状', key: '0-2' },
      { title: '第四节：图表', key: '0-3' },
    ],
  },
]);

const styles = ref([
  '普通',
  '无间隔',
  '标题 1',
  '标题 2',
  '标题 3',
  '标题',
  '副标题',
  '引用',
  '强调引用',
  '列表段落',
]);

const comments = ref([
  '批注 1: 引言部分很棒！',
  '批注 2: 检查表格中的数据。',
  '批注 3: 图片占位符很合适。',
]);
</script>
