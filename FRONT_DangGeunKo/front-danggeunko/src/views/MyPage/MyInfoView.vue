<template>
    <div class="my-info-scroll" v-if="targetUser.userId">
        <Profile :user="targetUser" :isMe="isMe"></Profile>
        <RegistCourse :targetUserId="targetUser.userId"></RegistCourse>
        <LikeCourse :targetUserId="targetUser.userId"></LikeCourse>
    </div>
    <div v-else>
        로딩 중..
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { storeToRefs } from 'pinia';
import Profile from '@/components/MyPage/Profile.vue';
import RegistCourse from '@/components/MyPage/RegistCourse.vue';
import LikeCourse from '@/components/MyPage/LikeCourse.vue';

const route = useRoute();
const userStore = useUserStore();
const { loginUserId } = storeToRefs(userStore);

const targetUser = ref({});
const isMe = computed(() => loginUserId.value === targetUser.value.userId);

const loadProfile = async () => {
    const nickname = route.params.nickname;
    // 1. 닉네임으로 대상 유저 정보 가져오기
    const userData = await userStore.getUserByNickname(nickname);
    if (userData) {
        targetUser.value = userData;
    }
};

onMounted(() => {
    loadProfile()
});
watch(() => route.params.nickname, loadProfile); // 닉네임 변경 시 재로드
</script>

<style>
.my-info-scroll{
    height: 100vh;
    overflow-y: auto;
    padding-bottom: 100px;
}

.my-info-scroll::-webkit-scrollbar {
  width: 0.8rem;
}

/* ★ 패널 스크롤바 핸들 (평소 색상) ★ */
.my-info-scroll::-webkit-scrollbar-thumb {
  background: #ff8a24;         /* 주황색 적용 */
  border-radius: 1rem;
  border: 0.2rem solid #f5f5f5;      /* 주황색이 너무 답답해 보이지 않게 살짝 여백 */
}

/* 호버 시 조금 더 진한 주황색 */
.my-info-scroll::-webkit-scrollbar-thumb:hover {
  background: #e66e00;
}
/* 섹션 전체 레이아웃 */
.course-section {
    margin-bottom: 40px;
    padding: 0 16px;
}

/* 타이틀 + 더보기 바 */
.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center; /* 끝 정렬보다 중앙 정렬이 더 깔끔합니다 */
    margin-bottom: 4px
}

.section-title {
    font-size: 24px;
    font-weight: 800;
    color: #333;
    padding: 6px 16px;
    display: inline-block;
}

.more-btn {
    font-size: 13px;
    color: #888;
    text-decoration: none;
    font-weight: 600;
    transition: color 0.2s;
}

.more-btn:hover {
    color: #ff7a00;
}

/* 카드 컨테이너 (회색 배경 박스) */
.course-container {
    background-color: #fff;
    border-radius: 24px;
    padding: 4px; /* 상하좌우 동일하게 패딩 부여 */
    box-shadow: 0 4px 20px rgba(0,0,0,0.05);
}

/* 카드 그리드 정렬 */
.course-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px; /* 카드 사이 간격 */
    align-items: start; /* 카드가 아래로 늘어지지 않게 방지 */
}

/* 개별 카드 감싸는 영역 */
.course-card-wrapper {
    position: relative; /* 하트의 기준점이 됨 */
    display: flex;
    flex-direction: column;
}

/* 카드 내부 콘텐츠 (이미지 포함) */
.card-content {
    width: 100%;
}

/* 하트 위치: 카드의 우측 하단 고정 */
.card-heart {
    position: absolute;
    /* 고정 px 대신 이미지 하단 근처로 위치 조정 */
    bottom: 63px; /* Title과 Updated 텍스트가 시작되기 전 지점 */
    right: 5px;
    z-index: 10;
    font-size: 22px;
    color: rgba(200, 200, 200, 0.8);
    cursor: pointer;
    transition: transform 0.2s;
}
.card-heart i {
    margin-top: 5px;
    font-size: 15px;
}
.card-heart:hover {
    transform: scale(1.1);
}

.card-heart.active {
    color: #ff4b4b;
}

/* 데이터 없을 때 */
.empty-msg {
    text-align: center;
    padding: 50px 0;
    color: #bbb;
    font-size: 14px;
}

/* ------------------------------------------- */
/*  CourseCard 내부 컴포넌트 스타일 강제 조정 */
/* ------------------------------------------- */
.course-card-wrapper :deep(.course-card-image) {
    width: 100%;
    aspect-ratio: 1 / 1; /* 정사각형 비율 유지 */
    object-fit: cover;
    border-radius: 20px;
}
</style>