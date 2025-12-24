<template>
    <div class="firstDesc-wrapper">
        <div class="firstDesc">
            <AICommentLoading v-if="aiStore.loading" />

            <div v-else class="content-container">
                <div class="bubble-speech">
                    <div class="summary-title">당근코 한 줄 요약✨</div>
                    <span v-html="renderedComment"></span>
                </div>

                <div class="character-wrapper">
                    <img class="dkImg" src="@/assets/img/dgk.png" alt="당근코 캐릭터">
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import { useAIStore } from '@/stores/ai';
import MarkdownIt from 'markdown-it';
import { computed, onMounted, ref } from 'vue';
import AICommentLoading from './AICommentLoading.vue';

const md = new MarkdownIt({
    linkify: true,
    breaks: true,
})

const props = defineProps({
    course: Object,
})

const aiStore = useAIStore()
const course = ref(props.course)
const prompt = () => {
    return `이 코스는 주간별 유저들에게 가장 인기를 받은 코스야. 다음과 같은 코스 정보를 바탕으로 왜 인기를 끌었고, 런린이, 러너, 런고수 등급 중 어느 정도의 수준에 추천하는지 그리고 특징에 대해 간략히설명해줘
                   코스이름: ${course.value.courseName}
                   코스 지역(시/도): ${course.value.courseCity}
                   코스 지역(구/군): ${course.value.courseDistrict}
                   시작 주소: ${course.value.startAddress}
                   종료 주소: ${course.value.endAddress}
                   거리: ${course.value.distance_km}
                   소요 시간: ${course.value.durationMin}
                   페이스: ${course.value.paceMin}
                   러닝 유형: ${course.value.courseType}
                   난이도: ${course.value.difficulty}
                   코스 설명: ${course.value.description}
                   횡단보도 유무: ${course.value.hasCrosswalk}
                   공중화장실 유무: ${course.value.hasToilet}
                   별점 평균: ${course.value.avgRating}
                   조회수: ${course.value.viewCnt}
                   강조할 부분은 마크다운언어 형식으로 강조해주고, 너비
                   답변 총 분량은 2줄로 ~한 이유로 런린이/러너/런고수 분들에게 추천드려요!
                   `
}
const send = async () => {
    const text = prompt();
    await aiStore.sendMemoryMessage(text);
}

// ⭐ 마크다운을 HTML로 변환하는 변수
const renderedComment = computed(() => {
    return md.render(aiStore.comment || '');
});

//최종 테스트 때 열거임 다시!!!!!!!!
onMounted(async () => {
    if (props.course) {
        await send();
    }
})
</script>

<style scoped>
/* 래퍼: 전체 영역 확보 및 중앙 정렬 */
.firstDesc-wrapper {
    width: 100%;
    display: flex;
    padding: 10px;
    box-sizing: border-box;
}

.firstDesc {
    width: 100%;
}

/* 컨테이너: 말풍선과 캐릭터를 세로로 배치 */
.content-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    gap: 5px;
}

/* 말풍선 제목 스타일 */
.summary-title {
    font-size: 18px;
    font-weight: 800;
    margin-bottom: 8px;
    color: #333;
}

/* 1. 말풍선 스타일 */
.bubble-speech {
    background: white;
    border: 2px solid #ff7a00;
    border-radius: 20px;
    padding: 18px;
    position: relative;
    line-height: 1.6;
    font-size: 15px;
    font-weight: 600;
    text-align: center;
    width: 90%;
    filter: drop-shadow(2px 4px 8px rgba(0, 0, 0, 0.1));
    margin-bottom: 25px;
    /* 꼬리 공간 확보 */

    /* 한글 줄바꿈 최적화 */
    word-break: keep-all;
    overflow-wrap: break-word;
}

/* 말풍선 꼬리 - 테두리 */
.bubble-speech::after {
    content: '';
    position: absolute;
    top: 100%;
    left: 40px;
    border-top: 20px solid #ff7a00;
    border-right: 15px solid transparent;
    border-left: 0px solid transparent;
}

/* 말풍선 꼬리 - 내부 채우기 */
.bubble-speech::before {
    content: '';
    position: absolute;
    top: calc(100% - 2px);
    left: 42px;
    border-top: 18px solid white;
    border-right: 13px solid transparent;
    border-left: 0px solid transparent;
    z-index: 1;
}

/* 2. 캐릭터 스타일 */
.character-wrapper {
    align-self: flex-start;
    margin-left: 10px;
    margin-top: -15px;
    /* 말풍선과 밀착 */
}

.dkImg {
    height: 10.9rem;
    width: auto;
    object-fit: contain;
}

/* 마크다운 내부 강조 스타일 */
.bubble-speech :deep(strong) {
    color: #ff7a00;
    font-weight: 800;
}

/* 로딩 컴포넌트 레이아웃 */
.firstDesc :deep(.loading-container) {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 200px;
    /* 로딩 바 높이 고정 */
}
</style>