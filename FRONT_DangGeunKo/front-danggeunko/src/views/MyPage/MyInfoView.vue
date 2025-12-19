<template>
    <div v-if="targetUser.userId">
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