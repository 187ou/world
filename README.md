# World

This template should help get you started developing with Vue 3 in Vite.

## 项目技术栈

见项目依赖包版本信息

## 项目操作手册

运行该项目需要安装node环境，建议使用node版本>=18，运行项目需要安装依赖包，npm install一下即可，详情可以参考网上的一下操作就不详讲。
注意是先安装node环境，确保node安装成功，然后进入项目目录，运行npm install安装依赖包，然后运行npm run dev启动项目，打开浏览器访问即可看到项目首页。

## 项目结构

views主界面框架

types为封装类型

filters封装过滤器

composability为布局组件

api为接口请求，用于封装接口请求

components为复用组件或者拆分组件

assets用于存放静态资源，比如图片，字体等等

stores基于pinia状态管理，用于管理全局状态

router为路由配置，用于配置路由，路径都在这里

utils为工具类，用于封装一些常用的方法，比如http请求等

test为本人测试使用，可以忽略

## 运行项目

指令就是npm run dev即可

最后运行项目不会指令的话，点开package.json文件，找到scripts下的dev，运行npm run dev即可


## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Type Support for `.vue` Imports in TS

TypeScript cannot handle type information for `.vue` imports by default, so we replace the `tsc` CLI with `vue-tsc` for type checking. In editors, we need [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) to make the TypeScript language service aware of `.vue` types.

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Type-Check, Compile and Minify for Production

```sh
npm run build
```

### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```
