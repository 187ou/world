<template>
  <canvas
    ref="canvas"
    width="120"
    height="40"
    class="border rounded cursor-pointer bg-gray-50"
    @click="refreshCaptcha"
    title="点击刷新"
  ></canvas>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const canvas = ref<HTMLCanvasElement|null>(null)
let correctCaptcha = ''

const generateCaptcha = () => {
  if (!canvas.value) return
  const ctx = canvas.value.getContext('2d')
  if (!ctx) return

  ctx.clearRect(0, 0, canvas.value.width, canvas.value.height)

  const chars = '23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjklmnpqrstuvwxyz'
  correctCaptcha = ''
  for (let i = 0; i < 4; i++) {
    correctCaptcha += chars[Math.floor(Math.random() * chars.length)]
  }

  const gradient = ctx.createLinearGradient(0, 0, canvas.value.width, 0)
  gradient.addColorStop(0, '#8c756933')
  gradient.addColorStop(1, '#55311c33')
  ctx.fillStyle = gradient
  ctx.fillRect(0, 0, canvas.value.width, canvas.value.height)

  ctx.font = '22px Arial Bold'
  ctx.textAlign = 'center'
  ctx.textBaseline = 'middle'
  for (let i = 0; i < correctCaptcha.length; i++) {
    ctx.fillStyle = `rgb(${Math.floor(Math.random() * 80 + 50)}, ${Math.floor(Math.random() * 50 + 30)}, ${Math.floor(Math.random() * 30 + 20)})`
    const rotate = (Math.random() * 30 - 15) * Math.PI / 180
    ctx.save()
    ctx.translate(30 + i * 20, 20)
    ctx.rotate(rotate)
    ctx.fillText(correctCaptcha[i], 0, 0)
    ctx.restore()
  }

  ctx.strokeStyle = `rgb(${Math.floor(Math.random() * 100 + 50)}, ${Math.floor(Math.random() * 80 + 30)}, ${Math.floor(Math.random() * 60 + 20)})`
  ctx.lineWidth = 1
  for (let i = 0; i < 3; i++) {
    ctx.beginPath()
    ctx.moveTo(Math.random() * canvas.value.width, Math.random() * canvas.value.height)
    ctx.lineTo(Math.random() * canvas.value.width, Math.random() * canvas.value.height)
    ctx.stroke()
  }

  for (let i = 0; i < 20; i++) {
    ctx.fillStyle = `rgb(${Math.floor(Math.random() * 120 + 30)}, ${Math.floor(Math.random() * 100 + 20)}, ${Math.floor(Math.random() * 80 + 10)})`
    ctx.beginPath()
    ctx.arc(Math.random() * canvas.value.width, Math.random() * canvas.value.height, 1, 0, 2 * Math.PI)
    ctx.fill()
  }
}

const refreshCaptcha = () => {
  generateCaptcha()
}

onMounted(() => {
  generateCaptcha()
})

defineExpose({ refreshCaptcha })
</script>
