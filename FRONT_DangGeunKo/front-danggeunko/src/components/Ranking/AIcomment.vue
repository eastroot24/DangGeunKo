<template>
    <div class="firstDesc-wrapper">
        <div class="firstDesc">
            <span v-if="aiStore.loading">코스를 분석하고 있습니다...</span>
            <span v-else class="bubble-speech" v-html="renderedComment"></span>
            <img style="height: 100px;" class="dkImg" src="@/assets/img/dgk.png" alt="당근코 캐릭터">
        </div>
    </div>
</template>

<script setup>
import { useAIStore } from '@/stores/ai';
import MarkdownIt from 'markdown-it';
import { computed, onMounted, ref } from 'vue';

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
                   강조할 부분은 마크다운언어 형식으로 강조해주고,
                   답변 예시: 2줄로 ~한 이유로 런린이/러너/런고수 분들에게 추천드려요!
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
/* AI Comment가 렌더링될 내부 컨테이너 */
.firstDesc-wrapper {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 100%;
    padding: 10px;
}

/* ⭐ 1. 설명 영역 컨테이너 스타일 (HTML 파일의 .firstDesc) */
.firstDesc {
    display: flex;
    flex-direction: column;
    /* 내부 요소 세로 정렬 */
    align-items: center;
    /* 이미지와 텍스트 가운데 정렬 */
    text-align: center;
    font-size: 14px;
    flex-shrink: 0;
    /* width는 부모 .ai-comment-container가 이미 조정했으므로 여기서는 너비 고정 제거 */
}

.dkImg {
    margin-top: 5px;
    /* 말풍선과 캐릭터 사이 간격 */
}

/* 마크다운 내부 요소 스타일링 */
.bubble-speech :deep(strong) {
    color: #ff7a00;
    font-weight: bold;
}

.bubble-speech :deep(a) {
    color: #007bff;
    text-decoration: underline;
    word-break: break-all;
    /* 긴 링크가 말풍선을 삐져나가지 않게 설정 */
}

/* 2. 말풍선 스타일 (HTML 파일의 .bubble-speech) */
.bubble-speech {
    background: white;
    border: 2px solid #ff7a00;
    border-radius: 10px;
    padding: 10px;
    position: relative;
    /* 꼬리를 위한 기준점 */
    line-height: 1.4;
    font-size: 13px;
    /* 글꼴 크기 조정 */
    font-weight: 500;
    text-align: left;
    width: 90%;

}

/* 말풍선 꼬리 - 외부 테두리 (.bubble-speech::after) */
.bubble-speech::after {
    content: '';
    position: absolute;
    right: 100%;
    top: 50%;
    transform: translateY(-50%);
    border: 10px solid transparent;
    border-right-color: #ff7a00;
    border-left: 0;
}

.bubble-speech::before {
    content: '';
    position: absolute;
    right: calc(100% - 2px);
    top: 50%;
    transform: translateY(-50%);
    border: 9px solid transparent;
    border-right-color: white;
    border-left: 0;
}
</style>