<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-slate-100 dark:from-slate-900 dark:to-blue-950/30 flex flex-col">
    <!-- 顶部导航栏 -->
    <header class="w-full py-6 px-4 sm:px-6 lg:px-8 transition-all duration-300">
      <div class="max-w-7xl mx-auto flex justify-between items-center">
        <a href="/" class="flex items-center space-x-2 group">
          <div class="h-8 w-8 rounded-md bg-blue-400 flex items-center justify-center shadow-md group-hover:shadow-blue-200/50 dark:shadow-blue-900/30 transition-all">
            <span class="text-white font-bold">W</span>
          </div>
          <span class="text-xl font-bold text-slate-800 dark:text-white">World</span>
        </a>
        <button class="p-2 rounded-full hover:bg-blue-100 dark:hover:bg-blue-900/20 transition-colors">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-slate-600 dark:text-blue-200" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </button>
      </div>
    </header>

    <!-- 主要内容区 -->
    <main class="flex-grow flex flex-col items-center justify-center px-4 sm:px-6 lg:px-8 py-12 relative">
      <div class="absolute -bottom-1 -left-1 w-64 h-64 bg-blue-400/10 rounded-full blur-3xl animate-pulse"></div>
      <div class="absolute -top-1 -right-1 w-64 h-64 bg-blue-600/10 rounded-full blur-3xl animate-pulse" style="animation-delay: 1s"></div>

      <div class="text-center max-w-3xl relative z-10">
        <div class="relative inline-block mb-8">
          <span class="text-[clamp(8rem,20vw,16rem)] font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-500 to-blue-300 tracking-tight animate-float">404</span>
          <div class="absolute -bottom-6 left-1/2 transform -translate-x-1/2 w-3/4 h-1 bg-blue-300/30 dark:bg-blue-500/30 rounded-full"></div>
        </div>

        <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-slate-800 dark:text-white mb-6 leading-tight">
          Page not found
        </h1>

        <p class="text-[clamp(1rem,2vw,1.25rem)] text-slate-600 dark:text-blue-100/80 mb-8 max-w-2xl mx-auto">
          Sorry, the page you are looking for doesn't exist or has been moved.
        </p>

        <div class="flex flex-col sm:flex-row gap-4 justify-center">
          <button
            @click="goToHome"
            class="btn text-lg px-8 py-6 rounded-full font-medium text-white bg-blue-400 hover:bg-blue-500 transition-all duration-300 transform hover:-translate-y-1 active:translate-y-0"
          >
            Return to Home
          </button>
          <button
            @click="goSupport"
            class="btn btn-outline border-blue-200 dark:border-blue-800 text-blue-700 dark:text-blue-300 hover:bg-blue-50 dark:hover:bg-blue-900/20 text-lg px-8 py-6 rounded-full hover:shadow-md transition-all duration-300"
          >
            Contact Support
          </button>
        </div>
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="w-full py-6 px-4 sm:px-6 lg:px-8 border-t border-blue-100 dark:border-blue-900/30">
      <div class="max-w-7xl mx-auto flex flex-col sm:flex-row justify-between items-center">
        <p class="text-slate-500 dark:text-blue-200/60 text-sm">
          © 2025 Man. What can I say.
        </p>
        <div class="flex space-x-6 mt-4 sm:mt-0">
          <a href="/" class="text-slate-500 dark:text-blue-200/60 hover:text-primary transition-colors">
            Terms
          </a>
          <a href="/" class="text-slate-500 dark:text-blue-200/60 hover:text-primary transition-colors">
            Privacy
          </a>
          <a href="/" class="text-slate-500 dark:text-blue-200/60 hover:text-primary transition-colors">
            Help
          </a>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import router from '@/router'
import { onMounted } from 'vue';

const goToHome = () => {
  router.push('/home')
};

const goSupport = () => {
  router.push('/')
};

onMounted(() => {
  document.body.classList.add('page-loaded');
});
</script>

<style scoped lang="scss">
.page-loaded {
  opacity: 1;
  transition: opacity 0.5s ease-in-out;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-12px); }
}

.animate-float {
  animation: float 6s ease-in-out infinite;
}
</style>
