<template>
    <div class="firstDesc-wrapper">
        <div class="firstDesc">
            <span class="bubble-speech">이 코스는 많은 사람들에게 인기를 끌었고, 초보자들에게 추천해요!</span>
            <img style="height: 100px;" class="dkImg" src="@/assets/img/dgk.png" alt="당근코 캐릭터">
        </div>
    </div>
</template>

<script setup>
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
    flex-direction: column; /* 내부 요소 세로 정렬 */
    align-items: center; /* 이미지와 텍스트 가운데 정렬 */
    text-align: center;
    font-size: 14px;
    flex-shrink: 0; 
    /* width는 부모 .ai-comment-container가 이미 조정했으므로 여기서는 너비 고정 제거 */
}

.dkImg {
    margin-top: 5px; /* 말풍선과 캐릭터 사이 간격 */
}

/* 2. 말풍선 스타일 (HTML 파일의 .bubble-speech) */
.bubble-speech {
    background: white;
    border: 2px solid #ff7a00;
    border-radius: 10px;
    padding: 10px;
    position: relative; /* 꼬리를 위한 기준점 */
    line-height: 1.4;
    font-size: 13px; /* 글꼴 크기 조정 */
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