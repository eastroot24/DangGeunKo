<template>
  <Teleport to="body">
    <div class="popup-bg" @click.self="$emit('close')">
      <div class="coach-modal">
        <div class="coach-header">
          <div>당근코치의 맞춤 피드백</div>
        </div>

        <div class="coach-content">
          <CoachLoading v-if="aiStore.loading" />

          <div v-else>
            <img src="@/assets/img/dgk_coach.png" class="full-coach" alt="당근코치">
            <div class="bubble">
              <div v-html="renderedComment"></div>
              <button v-if="recommendedCourseId" @click="goToCourse" class="go-course-btn">
                추천 코스 상세보기 🏃
              </button>
            </div>
          </div>
        </div>

        <button class="confirm-btn" @click="$emit('close')">고마워요!</button>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { useAIStore } from '@/stores/ai';
import { useCourseStore } from '@/stores/course';
import MarkdownIt from 'markdown-it';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import CoachLoading from './CoachLoading.vue';

const router = useRouter();
const md = new MarkdownIt({ linkify: true, breaks: true });

const props = defineProps({
  user: Object
});

const emit = defineEmits(['close']);

const aiStore = useAIStore();
const courseStore = useCourseStore(); // 오타 수정: couruseStore -> courseStore
const { searchInfo, rankingCourseList } = storeToRefs(courseStore);

const recommendedCourseId = ref(null);

// 1. 프롬프트 생성 함수 (순수하게 문자열만 반환)
const createPrompt = (u, cList) => {
  return `너는 당근코 서비스에서 러너들의 맞춤형 런닝 코치야. 
    [응답 규칙 - 반드시 지킬 것]
    1. 추천 코스의 ID를 반드시 문장 끝에 [ID:숫자] 형식으로 딱 한 번만 포함해줘.
    2. 사용자에게 보여줄 본문 텍스트 안에는 ID나 URL을 직접 노출하지 마.
    
    <유저정보>
    유저 별명: ${u?.nickname || '회원'},
    성별: ${u?.gender || '미설정'},                    
    나이: ${u?.age || '미설정'},                    
    거주 시: ${u?.userCity || '미설정'},                    
    거주 지역(구): ${u?.userDistrict || '미설정'},                    
    선호 거리: ${u?.prefDistance || '미설정'},                    
    런닝 수준: ${u?.prefDifficulty || '런린이'}

    <코스 목록>
    ${JSON.stringify(cList)} 
    
    강조할 부분은 마크다운언어 형식으로 강조해주고, 위 정보들을 바탕으로 코스추천과 따뜻한 피드백을 해줘.`;
};

// 2. 메인 전송 로직
const send = async () => {
  try {
    aiStore.loading = true;
    aiStore.comment = ''; // 이전 답변 초기화

    // A. 검색 조건 설정 (유저 지역 기반)
    searchInfo.value.courseCity = props.user?.userCity || '';
    searchInfo.value.courseDistrict = props.user?.userDistrict || '';

    // B. 데이터 로드 (서버에서 랭킹 정보 가져오기)
    await courseStore.getWeeklyRanking();

    // C. 데이터 정제 (상위 10개)
    const topList = rankingCourseList.value ? rankingCourseList.value.slice(0, 10) : [];

    // D. 프롬프트 생성 및 AI 요청
    const text = createPrompt(props.user, topList);
    await aiStore.sendMemoryMessage(text);

    // E. 답변 처리 및 ID 추출
    let response = aiStore.comment;
    const idMatch = response.match(/\[ID:(\d+)\]/);
    if (idMatch) {
      recommendedCourseId.value = idMatch[1];
      response = response.replace(/\[ID:(\d+)\]/g, ""); // ID 태그 제거
    }
    aiStore.comment = response;

  } catch (error) {
    console.error("AI 코치 요청 중 오류 발생:", error);
    aiStore.comment = "죄송해요, 피드백을 가져오는 중에 문제가 생겼어요. 잠시 후 다시 시도해주세요!";
  } finally {
    aiStore.loading = false;
  }
};

const goToCourse = () => {
  if (recommendedCourseId.value) {
    router.push({ name: 'courseDetail', params: { id: recommendedCourseId.value } });
    emit('close'); // 이동하면서 모달 닫기
  }
};

const renderedComment = computed(() => {
  return md.render(aiStore.comment || '');
});

onMounted(() => {
  send();
});
</script>


<style scoped>
.popup-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, .4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.coach-modal {
  width: 37.5rem;
  background: white;
  border-radius: 1.5rem;
  padding: 2rem;
  box-shadow: 0 0.625rem 1.875rem rgba(0, 0, 0, 0.2);
}

.coach-header {
  margin-bottom: 1.5rem;
  font-weight: 700;
  color: #ff7a00;
  font-size: 1.25rem;
}

.coach-content {
  min-height: 250px;
  display: flex;
  align-items: center;
  justify-content: center;
  /* 로딩 컴포넌트 중앙 정렬을 위해 추가 */
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.full-coach {
  width: 8rem;
  height: auto;
  flex-shrink: 0;
}

.bubble {
  flex: 1;
  background: #fff8f0;
  border-radius: 1.25rem;
  padding: 1.25rem;
  position: relative;
  border: 0.0625rem solid #ffe4c4;
  font-size: 1rem;
  line-height: 1.6;
  color: #444;
  /* 한글 단어가 중간에 끊기지 않도록 설정 */
  word-break: keep-all;
  /* 긴 단어가 있을 경우에만 줄바꿈 허용 */
  overflow-wrap: break-word;
}

.bubble::after {
  content: '';
  position: absolute;
  top: 50%;
  left: -0.625rem;
  transform: translateY(-50%);
  border-top: 0.625rem solid transparent;
  border-bottom: 0.625rem solid transparent;
  border-right: 0.625rem solid #fff8f0;
}

.confirm-btn {
  width: 100%;
  height: 3rem;
  background: #ff7a00;
  color: white;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
  font-weight: 700;
  font-size: 1rem;
}

.go-course-btn {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background: #ff7a00;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-weight: bold;
}

.coach-content>div:not(.loading-container) {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  width: 100%;
}
</style>