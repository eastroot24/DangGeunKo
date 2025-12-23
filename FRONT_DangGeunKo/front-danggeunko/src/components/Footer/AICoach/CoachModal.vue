<template>
  <Teleport to="body">
    <div class="popup-bg" @click.self="$emit('close')">
      <div class="coach-modal">
        <div class="coach-header">
          <div>당근코치의 맞춤 피드백</div>
        </div>
        
        <div class="coach-content">
          <CoachLoading v-if="aiStore.loading"></CoachLoading>
          <div v-else>
            <img src="@/assets/img/dgk_coach.png" class="full-coach">
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
const router = useRouter()
const md = new MarkdownIt({
    linkify: true,
    breaks: true,
})
// 부모로부터 유저 정보를 받음
const props = defineProps({
  user: Object
});
const aiStore = useAIStore()
const user = ref(props.user)
const recommendedCourseId = ref(null);
const couruseStore = useCourseStore();
const {courseList} = storeToRefs(couruseStore)
const prompt = () => {
    // 1. ref 객체에서 실제 값(value)을 꺼내야 합니다.
    const u = user.value; 
    const cList = courseList.value;

    // 2. 만약 u나 cList가 비어있을 경우를 대비해 콘솔로 먼저 확인해보세요.
    console.log("프롬프트 생성 데이터:", u, cList);

   return `너는 당근코 서비스에서 러너들의 맞춤형 런닝 코치야. 
    [응답 규칙 - 반드시 지킬 것]
    1. 추천 코스의 ID를 반드시 문장 끝에 [ID:숫자] 형식으로 딱 한 번만 포함해줘.
    2. 사용자에게 보여줄 본문 텍스트 안에는 ID나 URL을 직접 노출하지 마.
    <유저정보>
    유저 별명: ${u?.nickname || '회원'},
    성별: ${u?.gender || '미설정'},                    
    나이: ${u?.age || '미설정'},                    
    거주 시: ${u?.userCity || '미설정'},                    
    거주 지역(구): ${u?.district || '미설정'},                    
    선호 거리: ${u?.prefDistance || '미설정'},                    
    런닝 수준: ${u?.prefDifficulty || '런린이'},      

    <코스 목록>
    ${JSON.stringify(cList)} 
    강조할 부분은 마크다운언어 형식으로 강조해주고,
    위 정보들을 바탕으로 코스추천, 피드백을 해줘.`;
}

const send = async () => {
    const text = prompt();
    await aiStore.sendMemoryMessage(text);
    
    // AI의 원본 답변
    let response = aiStore.comment;
    
    // 1. Course ID 추출 및 제거
    const idMatch = response.match(/\[ID:(\d+)\]/);
    if (idMatch) {
        recommendedCourseId.value = idMatch[1];
        // 사용자에게 보이지 않게 [ID:숫자] 삭제
        response = response.replace(/\[ID:(\d+)\]/g, "");
    }

    // 태그들이 삭제된 깨끗한 텍스트만 저장하여 화면에 표시
    aiStore.comment = response
};

// 코스로 이동하는 함수
const goToCourse = () => {
    if (recommendedCourseId.value) {
        router.push({ name: 'courseDetail', params: { id: recommendedCourseId.value } });
    }
};


//  마크다운을 HTML로 변환하는 변수
const renderedComment = computed(() => {
    return md.render(aiStore.comment || '');
});
// 최종 테스트 때 열거임 다시!!!!!!!!
onMounted(async () => {
    await couruseStore.getCourseList()

    if (props.user) {
        await send();
    }
})
// 부모에게 창을 닫으라고 신호를 보냄
defineEmits(['close']);
</script>


<style scoped>
.popup-bg {
  position: fixed;
  top: 0; left: 0; width: 100vw; height: 100vh;
  background: rgba(0, 0, 0, .4);
  display: flex; justify-content: center; align-items: center;
  z-index: 1000;
}
.coach-modal {
  width: 37.5rem; background: white; border-radius: 1.5rem;
  padding: 2rem; box-shadow: 0 0.625rem 1.875rem rgba(0,0,0,0.2);
}
.coach-header { margin-bottom: 1.5rem; font-weight: 700; color: #ff7a00; font-size: 1.25rem; }
.coach-content {
  min-height: 250px; 
  display: flex;
  align-items: center;
  justify-content: center; /* 로딩 컴포넌트 중앙 정렬을 위해 추가 */
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}
.full-coach { width: 8rem; height: auto; flex-shrink: 0; }
.bubble {
  flex: 1; background: #fff8f0; border-radius: 1.25rem; padding: 1.25rem;
  position: relative; border: 0.0625rem solid #ffe4c4; font-size: 1rem;
  line-height: 1.6; color: #444;
}
.bubble::after {
  content: ''; position: absolute; top: 50%; left: -0.625rem;
  transform: translateY(-50%);
  border-top: 0.625rem solid transparent;
  border-bottom: 0.625rem solid transparent;
  border-right: 0.625rem solid #fff8f0;
}

.confirm-btn {
  width: 100%; height: 3rem; background: #ff7a00; color: white;
  border: none; border-radius: 0.5rem; cursor: pointer; font-weight: 700; font-size: 1rem;
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
.coach-content > div:not(.loading-container) {
  display: flex;
  align-items: center;
  gap: 1.5rem;       
  width: 100%;        
}
</style>