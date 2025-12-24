<template>
  <div class="loading-container">
    <div class="loading-content-wrapper">
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
  align-items: center;
  /* 수직 중앙 정렬 */
  justify-content: center;
  /* 수평 중앙 정렬 */
  width: 100%;
  height: 100%;
  /* 부모 높이 전체 차지 */
  min-height: 300px;
  /* 최소 높이 확보 */
}

.loading-content-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 280px;
  /* 로딩 영역 전체 너비 고정 */
}

.track-area {
  width: 100%;
  position: relative;
  margin-bottom: 10px;
  /* 텍스트와의 간격 */
}

.character-mover {
  width: 100%;
  /* 수직 중앙 정렬 관련 flex 제거 - 캐릭터는 애니메이션으로만 이동 */
  animation: move-right 2s linear infinite;
}

.loading-character {
  height: 8.5rem;
  display: block;
}

.gauge-bar-bg {
  width: 100%;
  height: 10px;
  background: #eee;
  border-radius: 5px;
  overflow: hidden;
  margin-top: -10px;
  /* 캐릭터 발바닥에 붙임 */
}

.gauge-bar-fill {
  height: 100%;
  background: #ff7a00;
  border-radius: 5px;
  animation: fill-bar 2s linear infinite;
}

@keyframes move-right {
  0% {
    transform: translateX(0);
  }

  100% {
    transform: translateX(calc(100% - 100px));
  }

  /* 캐릭터 너비에 맞춰 조정 */
}

@keyframes fill-bar {
  0% {
    width: 0%;
  }

  100% {
    width: 100%;
  }
}

.loading-text {
  margin-top: 15px;
  font-size: 14px;
  color: #ff7a00;
  font-weight: bold;
  animation: fade 2s infinite;
}

@keyframes fade {

  0%,
  100% {
    opacity: 1;
  }

  50% {
    opacity: 0.5;
  }
}
</style>