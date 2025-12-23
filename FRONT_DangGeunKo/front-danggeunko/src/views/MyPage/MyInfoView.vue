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
    padding-bottom: 120px;
}

.my-info-scroll::-webkit-scrollbar {
  width: 1.5rem;
}

/* ★ 패널 스크롤바 핸들 (평소 색상) ★ */
.my-info-scroll::-webkit-scrollbar-thumb {
  background: #ff8a24;         /* 주황색 적용 */
  border-radius: 1rem;
  border: 0.6rem solid #fff;      /* 주황색이 너무 답답해 보이지 않게 살짝 여백 */
}

/* 호버 시 조금 더 진한 주황색 */
.my-info-scroll::-webkit-scrollbar-thumb:hover {
  background: #e66e00;
}
</style>