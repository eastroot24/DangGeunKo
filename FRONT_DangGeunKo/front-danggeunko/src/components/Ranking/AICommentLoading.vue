<template>
  <div class="loading-container">
    <div class="track-area">
      <div class="character-mover">
        <img :src="currentImg" alt="running" class="loading-character" />
      </div>

      <div class="gauge-bar-bg">
        <div class="gauge-bar-fill"></div>
      </div>
    </div>
    <p class="loading-text">코스를 분석하고 있습니다...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import runImg from '@/assets/img/dgk_running.png';
import runReverseImg from '@/assets/img/dgk_running_reverse.png';

const currentImg = ref(runImg);
let timer = null;

onMounted(() => {
  timer = setInterval(() => {
    currentImg.value = currentImg.value === runImg ? runReverseImg : runImg;
  }, 200);
});

onUnmounted(() => {
  if (timer) clearInterval(timer);
});
</script>

<style scoped>
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  width: 100%;
}

.track-area {
  width: 280px; /* 로딩 바 길이를 조금 더 늘림 */
  position: relative;
}

.character-mover {
  width: 100%;
  /* 캐릭터 크기에 맞춰 이동 종료 지점 조정 */
  animation: move-right 2s linear infinite;
  display: flex;
}

.loading-character {
  height: 8.5rem; /* 요청하신 크기 */
  display: block;
  /* 이미지가 이동 영역을 벗어나지 않도록 설정 */
}

.gauge-bar-bg {
  width: 100%;
  height: 10px;
  background: #eee;
  border-radius: 5px;
  overflow: hidden;
  margin-top: -10px; /* 캐릭터 발바닥에 가깝게 붙임 */
  position: relative;
  z-index: 1;
}

.gauge-bar-fill {
  height: 100%;
  background: #ff7a00;
  border-radius: 5px;
  animation: fill-bar 2s linear infinite;
}

/* ⭐ 핵심 수정: 캐릭터의 너비를 고려한 이동 거리 계산 */
@keyframes move-right {
  0% { 
    transform: translateX(0); 
  }
  100% { 
    /* 8.5rem 높이일 때 보통 너비는 100px~120px 내외입니다. */
    /* calc(100% - 캐릭터의 실제너비)로 맞춰야 끝까지 갑니다. */
    transform: translateX(calc(100% - 60px)); 
  }
}

@keyframes fill-bar {
  0% { width: 0%; }
  100% { width: 100%; }
}

.loading-text {
  margin-top: 20px;
  font-size: 14px;
  color: #ff7a00;
  font-weight: bold;
  animation: fade 2s infinite;
}

@keyframes fade {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}
</style>