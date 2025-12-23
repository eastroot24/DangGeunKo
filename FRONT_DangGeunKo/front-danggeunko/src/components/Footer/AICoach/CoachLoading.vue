<template>
  <div class="loading-container">
    <div class="track-area">
      <div class="character-moving-wrapper">
        <img :src="currentImg" alt="running coach" class="running-athlete">
        <div class="shadow"></div>
      </div>
    </div>
    
    <p class="loading-text">당근코치가 열심히 코스를 분석 중입니다...</p>
    
    <div class="progress-container">
      <div class="progress-bar"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';

// 이미지 경로
import img1 from '@/assets/img/dgk_coach_running.png';
import img2 from '@/assets/img/dgk_coach_running_reverse.png';

const isStepAlternate = ref(false); 
let animationInterval = null;

const currentImg = computed(() => isStepAlternate.value ? img2 : img1);

onMounted(() => {
  // 발동작 교체 (이미지 스위칭)
  animationInterval = setInterval(() => {
    isStepAlternate.value = !isStepAlternate.value;
  }, 200);
});

onUnmounted(() => {
  if (animationInterval) clearInterval(animationInterval);
});
</script>

<style scoped>
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  width: 100%;
}

.track-area {
  width: 100%;
  max-width: 31.25rem;
  height: 12rem; /* 높이 약간 조정 */
  position: relative;
  overflow: hidden;
  margin-bottom: 1rem;
  border-bottom: 2px solid #eee;
}

/* 캐릭터 이동: 2초 주기로 무한 반복 */
.character-moving-wrapper {
  position: absolute;
  bottom: 5px;
  display: flex;
  flex-direction: column;
  align-items: center;
  animation: move-right 2s infinite linear;
}

.running-athlete {
  width: 100px;
  height: auto;
}

@keyframes move-right {
  0% { left: -100px; } 
  100% { left: 100%; }
}

.shadow {
  width: 50px;
  height: 6px;
  background: rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  margin-top: -5px;
}

.loading-text {
  font-weight: 800;
  color: #ff7a00;
  margin-bottom: 1.5rem;
  font-size: 1.1rem;
}

.progress-container {
  width: 80%;
  height: 12px;
  background-color: #eee;
  border-radius: 10px;
  overflow: hidden;
  position: relative;
}

/* ⭐ 핵심: 게이지 바 무한 반복 애니메이션 */
.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #ffb347, #ff7a00);
  /* 캐릭터 이동 시간(2s)과 동일하게 설정하여 동기화 */
  animation: fill-infinite 2s infinite linear;
  width: 0;
}

@keyframes fill-infinite {
  0% { 
    width: 0%; 
  }
  100% { 
    width: 100%; 
  }
}
</style>