<template>
  <div ref="wrapperRef" class="onboarding-wrapper">
    <!-- 스크롤 컨테이너 (실제 스크롤은 여기서만 발생) -->
    <div
      ref="scrollContainer"
      class="onboarding-container"
      @scroll="handleScroll"
    >
      <!-- 스크롤 거리만 늘리는 트랙 -->
      <div
        class="scroll-track"
        :style="{ width: `${steps.length * 100}vw` }"
      ></div>
    </div>

    <!-- 고정된 콘텐츠 영역 -->
    <section class="step-section">
  <!-- 일반 러닝 구간 (1 ~ 4) -->
  <div v-if="!isLastStep" class="content-card">
    <span class="step-badge">STEP 0{{ currentStep + 1 }}</span>
    <h2 class="section-title">
      {{ steps[currentStep].title }}
    </h2>
    <p class="section-desc">
      {{ steps[currentStep].desc }}
    </p>
  </div>

  <!-- 마지막 구간 전용 레이아웃 -->
  <div v-else class="final-step">
    <h2 class="final-title">
      러닝을 시작할 준비가 되셨나요?
    </h2>
    <p class="final-desc">
      지금 당근코와 함께<br />
      내 주변 러닝 코스를 만나보세요.
    </p>

    <button class="start-button" @click="goHome">
      회원가입 없이 둘러보기
    </button>
  </div>
</section>


    <!-- 캐릭터 -->
    <div v-if="currentStep < steps.length - 1"
      class="character-wrapper"
      :style="{ left: `${characterX}px` }"
    >
      <img
      ref="characterRef"
        :src="isFrameA ? carrotImages.frame1 : carrotImages.frame2"
        alt="당근코"
        class="carrot-character"
      />
      <div class="character-shadow"></div>
    </div>

    <!-- 인디케이터 -->
    <div class="fixed-ui footer">
      <div class="progress-indicator">
        <div
          v-for="(_, idx) in steps"
          :key="idx"
          :class="['dot', { active: currentStep === idx }]"
        ></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
    localStorage.setItem('hasVisited', 'true');
  router.replace('/');
};

const isLastStep = computed(
  () => currentStep.value === steps.length - 1
);

/* =====================
   이미지
===================== */
import frame1 from '@/assets/img/dgk_running.png';
import frame2 from '@/assets/img/dgk_running_reverse.png';

const carrotImages = {
  frame1,
  frame2,
};

const characterRef = ref(null);

/* =====================
   스텝 데이터 (구간 5개)
===================== */
const steps = [
  { title: '어디서 뛸지 고민인가요?', desc: '내 주변의 숨은 러닝 코스를 당근코가 찾아드려요.' },
  { title: '로컬 코스 정보', desc: '실제 러너들이 추천하는 검증된 코스를 확인하세요.' },
  { title: '함께 달리는 즐거움', desc: '혼자보다 함께할 때 더 즐거운 러닝 커뮤니티!' },
  { title: '오늘의 기록 공유', desc: '러닝 기록을 등록하고 이웃과 소통해보세요.' },
  { title: '시작해볼까요?', desc: '지금 당근코와 함께 첫 러닝을 시작해요!' },
];

/* =====================
   상태
===================== */
const wrapperRef = ref(null);
const scrollContainer = ref(null);

const currentStep = ref(0);
const characterX = ref(10);
const isFrameA = ref(true);

let animationInterval = null;

/* =====================
   휠 → 가로 스크롤
===================== */
const handleWheel = (event) => {
  if (!scrollContainer.value) return;
  scrollContainer.value.scrollLeft += event.deltaY;
  event.preventDefault();
};

/* =====================
   스크롤 처리 (핵심)
===================== */
const handleScroll = (event) => {
  const { scrollLeft, clientWidth } = event.target;

  const stepIndex = Math.min(
    Math.floor(scrollLeft / clientWidth),
    steps.length - 1
  );
  currentStep.value = stepIndex;

  // 마지막 스텝이면 캐릭터 처리 중단
  if (stepIndex === steps.length - 1) {
    characterX.value = 90;
    return;
  }

  const stepScrollLeft = scrollLeft - stepIndex * clientWidth;
  const stepProgress = stepScrollLeft / clientWidth;

  const CONTAINER_WIDTH = 800;
const characterWidth = characterRef.value?.offsetWidth || 0;


const startX = -characterWidth / 2;
const endX = CONTAINER_WIDTH - (characterWidth / 7);

characterX.value =
  startX + stepProgress * (endX - startX);

};


/* =====================
   라이프사이클
===================== */
onMounted(() => {
  animationInterval = setInterval(() => {
    isFrameA.value = !isFrameA.value;
  }, 150);

  wrapperRef.value?.addEventListener('wheel', handleWheel, { passive: false });
});

onUnmounted(() => {
  clearInterval(animationInterval);
  wrapperRef.value?.removeEventListener('wheel', handleWheel);
});
</script>

<style scoped>
.onboarding-wrapper {
  position: relative;
  width: 800px;
  height: 100vh;
  margin: 0 auto;         /* 가운데 */
  overflow: hidden;
  background-color: #fff;
}

/* 스크롤 컨테이너 */
.onboarding-container {
  width: 100%;
  height: 100%;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;
}
.onboarding-container::-webkit-scrollbar {
  display: none;
}

/* 스크롤 거리 확보용 */
.scroll-track {
  height: 100%;
}

/* 고정된 콘텐츠 */
.step-section {
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 20;
}

.content-card {
  text-align: center;
  max-width: 520px;
  padding: 2rem;
}

.step-badge {
  display: inline-block;
  background-color: #f97316;
  color: #fff;
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
  font-size: 0.75rem;
  font-weight: 800;
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 1.75rem;
  font-weight: 900;
  margin-bottom: 1rem;
  color: #18181b;
}

.section-desc {
  font-size: 1.125rem;
  color: #71717a;
  line-height: 1.6;
}

/* 캐릭터 */
.character-wrapper {
  position: absolute;     /* ✅ fixed -> absolute */
  top: 70%;
  z-index: 10;
  pointer-events: none;
  transition: left 0.1s linear;
  transform: translateY(-50%); /* X는 JS에서 처리하니 Y만 */
}

.carrot-character {
  width: 12rem;
  height: 12rem;
  object-fit: contain;
}

.character-shadow {
  width: 60%;
  height: 10px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 50%;
  margin: -15px auto 0;
  filter: blur(4px);
}

/* 인디케이터 */
.fixed-ui.footer {
  position: fixed;
  bottom: 40px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  z-index: 50;
}

.progress-indicator {
  display: flex;
  gap: 0.75rem;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #e4e4e7;
  transition: all 0.3s ease;
}

.dot.active {
  background-color: #f97316;
  transform: scale(1.5);
}

.final-step {
  text-align: center;
  max-width: 520px;
  padding: 2rem;
}

.final-title {
  font-size: 2rem;
  font-weight: 900;
  color: #18181b;
  margin-bottom: 1rem;
}

.final-desc {
  font-size: 1.125rem;
  color: #52525b;
  line-height: 1.6;
  margin-bottom: 2.5rem;
}

.start-button {
  background-color: #f97316;
  color: #fff;
  font-size: 1rem;
  font-weight: 700;
  padding: 0.875rem 1.5rem;
  border-radius: 12px;
  border: none;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.start-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(249, 115, 22, 0.35);
}

</style>
