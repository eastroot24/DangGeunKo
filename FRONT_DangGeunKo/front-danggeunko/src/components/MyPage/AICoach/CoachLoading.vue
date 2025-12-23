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
      <div class="progress-bar" :style="{ width: progress + '%' }"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';

// 이미지 경로 설정
const img1 = '/src/assets/img/dgk_coach_running.png';
const img2 = '/src/assets/img/dgk_coach_running_reverse.png';

const progress = ref(0);
const isStepAlternate = ref(false); 
let progressInterval = null;
let animationInterval = null;

const currentImg = computed(() => isStepAlternate.value ? img2 : img1);

onMounted(() => {
  // 1. 게이지 바 업데이트
  progressInterval = setInterval(() => {
    if (progress.value < 100) {
      progress.value += 1.75;
    } else {
      clearInterval(progressInterval);
    }
  }, 30);

  // 2. 사진 전환 (발동작 교체)
  // 이동 속도와 발 바뀜 속도를 맞추기 위해 0.15초~0.2초 정도가 적당합니다.
  animationInterval = setInterval(() => {
    isStepAlternate.value = !isStepAlternate.value;
  }, 200);
});

onUnmounted(() => {
  if (progressInterval) clearInterval(progressInterval);
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

/* 캐릭터가 이동할 수 있는 가로 통로 */
.track-area {
  width: 100%;
  max-width: 31.25rem; /* 모달 너비 내에서 적절히 조절 */
  height: 12.5rem;
  position: relative;
  overflow: hidden; /* 영역 밖으로 나가면 숨김 */
  margin-bottom: 1rem;
  border-bottom: 2px solid #eee; /* 바닥선 표시 */
}

/* 왼쪽 끝에서 오른쪽 끝으로 이동하는 래퍼 */
.character-moving-wrapper {
  position: absolute;
  bottom: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  /* 2초 동안 왼쪽에서 오른쪽으로 무한 반복 이동 */
  animation: move-right 2s infinite linear;
}

.running-athlete {
  width: 100px;
  height: auto;
}

/* 가로 이동 애니메이션 */
@keyframes move-right {
  0% { left: -120px; }   /* 왼쪽 밖에서 시작 */
  100% { left: 100%; }    /* 오른쪽 밖으로 완전히 나감 */
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
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #ffb347, #ff7a00);
  transition: width 0.05s linear;
}
</style>