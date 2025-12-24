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
  <!-- STEP 0 : 충돌 연출 -->
  <div v-if="currentStep === 0" class=" block-stage">
    <div
  v-for="(block, idx) in textBlocks"
  :key="idx"
  class="text-block"
  ref="blockRefs"
  :class="{ hit: block.hit }"
  :style="{
    left: block.x + 'px',
    top: block.y + '%',
    '--fly-x': block.flyX,
    '--fly-y': block.flyY,
    '--rotate': block.rotate
  }"
>
      {{ block.text }}
    </div>
  </div>

  <!-- STEP 1 ~ 3 -->
  <div v-else-if="!isLastStep" class="content-card">
    <h2 class="section-title">
      {{ steps[currentStep].title }}
    </h2>
    <p class="section-desc">
      {{ steps[currentStep].desc }}
    </p>
  </div>

  <!-- STEP 4 (마지막) -->
<div v-else class="final-step final-layout">
  <div class="final-text-area">
    <h2 class="final-hero-title">
      당<span style="color:#ffad66">신</span><br />
      근<span style="color:#ffad66">처의</span><br />
      코<span style="color:#ffad66">스</span>
    </h2>

    <p class="final-sub-desc">
      지금 당근코와 함께<br />
      내 주변 러닝 코스를 만나보세요.
    </p>

    <button class="final-primary-button" @click="goHome">
      회원가입 없이 둘러보기
    </button>

    <p class="final-login-text">
      이미 계정이 있으신가요?
      <span class="login-link" @click="goLogin">로그인하기</span>
    </p>
  </div>
</div>

</section>



    <!-- 캐릭터 -->
    <div
  class="character-wrapper"
  :style="{
    left: `${characterX}px`,
    zIndex: characterZ
  }"
>


      <img
  ref="characterRef"
  :src="
    isFinalStep
      ? carrotImages.frame1   // 👈 dgk_running 고정
      : isFrameA
        ? carrotImages.frame1
        : carrotImages.frame2
  "
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
import { ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
  localStorage.setItem('hasVisited', 'true');
  router.replace('/');
};

const goLogin = () => {
  router.push({name : 'login'});
};

const blockRefs = ref([]);

/* =====================
   스텝 데이터
===================== */
const steps = [
  { title: '어디서 뛸지 고민인가요?', desc: '내 주변의 숨은 러닝 코스를 당근코가 찾아드려요.' },
  { title: '로컬 코스 정보', desc: '이웃들의 러닝 코스를 구경해 보세요.' },
  { title: '함께 달리는 즐거움', desc: '혼자보다 함께할 때 더 즐거운 러닝 커뮤니티!' },
  { title: '오늘의 기록 공유', desc: '내가 뛴 코스를 등록하고 이웃과 공유해보세요.' },
  { title: '시작해볼까요?', desc: '지금 당근코와 함께 첫 러닝을 시작해요!' },
];

/* =====================
   상태
===================== */
const wrapperRef = ref(null);
const scrollContainer = ref(null);
const characterRef = ref(null);

const currentStep = ref(0);
const characterX = ref(10);
const isFrameA = ref(true);
const FINAL_STOP_X = -250; // ← 빈세주가 원하는 위치로 조절


/* =====================
   블럭 (STEP 0)
===================== */
const textBlocks = ref([]);

const initBlocks = () => {
  textBlocks.value = [
    {
      text: '어디서 뛸지 고민이신가요?',
      x: 200,
      y: -30,
      hit: false,
      flyX: 0,
      flyY: 0,
      rotate: 0,
    },
    {
      text: '내 주변 코스 찾기가 힘드신가요?',
      x: 360,
      y: 30,
      hit: false,
      flyX: 0,
      flyY: 0,
      rotate: 0,
    },
    {
      text: '당근코와 함께 달려요!',
      x: 540,
      y: 90,
      hit: false,
      flyX: 0,
      flyY: 0,
      rotate: 0,
    },
  ];
};

initBlocks();

/* =====================
   충돌 감지 + 랜덤 튕김
===================== */
watch(characterX, (x) => {
  if (currentStep.value !== 0) return;

  const characterWidth = characterRef.value?.offsetWidth || 0;
  const characterCenter = x + characterWidth / 2;

  textBlocks.value.forEach((block, index) => {
    if (block.hit) return;

    const el = blockRefs.value[index];
    if (!el) return;

    const blockRect = el.getBoundingClientRect();
    const blockCenter = block.x + blockRect.width / 2;

    const distance = Math.abs(characterCenter - blockCenter);

    if (distance < blockRect.width / 2 + 40) {
      block.hit = true;

      const dir = Math.random() > 0.5 ? 1 : -1;
      block.flyX = dir * (200 + Math.random() * 150);
      block.flyY = -(150 + Math.random() * 150);
      block.rotate = dir * (15 + Math.random() * 20);
    }
  });
});


/* =====================
   STEP 변경 감지 (초기화)
===================== */
watch(currentStep, (step) => {
  if (step === 0) {
    initBlocks();
  }
});

/* =====================
   캐릭터 z-index
===================== */
const characterZ = computed(() =>
  currentStep.value === 0 ? 30 : 5
);

const isLastStep = computed(
  () => currentStep.value === steps.length - 1
);

/* =====================
   이미지
===================== */
import frame1 from '@/assets/img/dgk_running.png';
import frame2 from '@/assets/img/dgk_running_reverse.png';

const carrotImages = { frame1, frame2 };

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
   스크롤 처리
===================== */

const setInitialCharacterPosition = () => {
  if (!characterRef.value) return;

  const characterWidth = characterRef.value.offsetWidth;
  const startX = -characterWidth - 100; // 👈 네가 의도한 시작 위치

  characterX.value = startX;
};

const handleScroll = (event) => {
  const { scrollLeft, clientWidth } = event.target;

  const stepIndex = Math.min(
    Math.floor(scrollLeft / clientWidth),
    steps.length - 1
  );

  currentStep.value = stepIndex;

  const characterWidth = characterRef.value?.offsetWidth || 0;

  // ✅ 마지막 스텝: 달리던 모션 유지 → 위치 고정
  if (stepIndex === steps.length - 1) {
    characterX.value = FINAL_STOP_X;
    return;
  }

  const stepScrollLeft = scrollLeft - stepIndex * clientWidth;
  const stepProgress = stepScrollLeft / clientWidth;

  const CONTAINER_WIDTH = 800;

  const startX =
    stepIndex === 0
      ? -characterWidth - 100   // ← 첫 페이지 출발 위치
      : -characterWidth / 2;    // ← 나머지 페이지

  const endX = CONTAINER_WIDTH - characterWidth / 7;

  characterX.value = startX + stepProgress * (endX - startX);
};

const isFinalStep = computed(
  () => currentStep.value === steps.length - 1
);


/* =====================
   라이프사이클
===================== */
onMounted(async () => {
  animationInterval = setInterval(() => {
    if (currentStep.value === steps.length - 1) return; // ⛔ 멈춤
    isFrameA.value = !isFrameA.value;
  }, 150);

  wrapperRef.value?.addEventListener('wheel', handleWheel, { passive: false });

  await nextTick();
  setInitialCharacterPosition();
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
  top: 50%;
  z-index: 10;
  pointer-events: none;
  transition: left 0.1s linear;
  transform: translateY(-50%); /* X는 JS에서 처리하니 Y만 */
}

.carrot-character {
  width: 58rem;
  height: 58rem;
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

.block-stage {
  position: relative;
  width: 800px;
  height: 400px;
}

.text-block {
  position: absolute;
  padding: 1rem 1.5rem;
  background: white;
  border-radius: 16px;
  font-weight: 800;
  font-size: 1rem;
  color: #18181b;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  transition:
    transform 0.6s ease,
    opacity 0.6s ease;
}

.text-block.hit {
  transform: translate(
      calc(var(--fly-x) * 1px),
      calc(var(--fly-y) * 1px)
    )
    rotate(calc(var(--rotate) * 1deg));
  opacity: 0;
}

/* ===== 마지막 페이지 레이아웃 ===== */
.final-layout {
  position: relative;
  width: 100%;
  height: 100%;
}

/* 텍스트 영역 */
.final-text-area {
  position: absolute;
  right: 0px;
  top: 50%;
  transform: translateY(-50%);
  text-align: left;
}

/* 메인 타이틀 */
.final-hero-title {
  font-family: DanggeunkoFont;
  font-size: 5rem;
  line-height: 1.4;
  color: #ff8a24;
  margin-bottom: 1.5rem;
}

/* 서브 설명 */
.final-sub-desc {
  font-size: 0.95rem;
  color: #2f2f2f;
  line-height: 1.6;
  margin-bottom: 1.75rem;
}

/* 메인 CTA */
.final-primary-button {
  width: 100%;
  background-color: #ff7a1a;
  color: #fff;
  font-size: 1rem;
  font-weight: 700;
  padding: 0.9rem 1.5rem;
  border-radius: 999px;
  border: none;
  cursor: pointer;
  margin-bottom: 0.75rem;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.final-primary-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 122, 26, 0.35);
}

/* 로그인 텍스트 */
.final-login-text {
  font-size: 0.75rem;
  color: #888;
}

.login-link {
  margin-left: 4px;
  color: #ff7a1a;
  font-weight: 600;
  cursor: pointer;
}


</style>
