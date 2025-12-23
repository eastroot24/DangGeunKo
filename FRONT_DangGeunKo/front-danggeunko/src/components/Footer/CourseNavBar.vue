<template>
    <div class="navbar">

        <RouterLink :to="myInfoPath" class="nav-item" @click="toggleByTap">
            <i class="fi fi-rs-user" style="font-size: 1.7rem;""></i>
            <div>MYPAGE</div>
        </RouterLink>
        <RouterLink to="/course" class="nav-item" @click="toggleByTap">
            <i class="fi fi-rs-map-marker " style="font-size: 1.7rem;""></i>
            <div>COURSE</div>
        </RouterLink>
        <RouterLink to="/course/ranking" class="nav-item" @click="toggleByTap">
            <i class="fi fi-rs-trophy" style="font-size: 1.7rem;"></i>
            <div>RANKING</div>
        </RouterLink>
    </div>
</template>

<script setup>
import { useSlidingPanel } from '@/assets/script.js'
import { useUserStore } from '@/stores/user';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref, watch } from 'vue';


const { toggleByTap } = useSlidingPanel()
const userStore = useUserStore()
const { user, isLoggedIn } = storeToRefs(userStore)
// 로그인한 유저의 닉네임이 바뀔 때마다 자동으로 경로를 업데이트
const myInfoPath = computed(() => {
    if (!userStore.isLoggedIn) {
        return '/login';
    }
    if (!userStore.user?.nickname) {
        return '/';
    }

    return `/myInfo/${userStore.user.nickname}`;
});


// 로그인 상태(isLoggedIn)가 true로 변하는 순간 유저 정보를 가져오도록 감시
watch(() => userStore.isLoggedIn, async (newVal) => {
    if (newVal && userStore.loginUserId && !userStore.user?.nickname) {
        await userStore.getUserById(userStore.loginUserId);
    }
}, { immediate: true });
</script>

<style scoped>
.nav-item {
    text-decoration: none;
    font-size: 1rem;

}

.router-link-active {
    font-weight: bold;
    color: #ff8a24;
}
</style>