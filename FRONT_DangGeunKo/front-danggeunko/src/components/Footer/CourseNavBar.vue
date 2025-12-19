<template>
    <div class="navbar">
        <RouterLink :to='myInfoPath' class="nav-item" @click="toggleByTap">MYPAGE</RouterLink>
        <RouterLink to="/course" class="nav-item" @click="toggleByTap">COURSE</RouterLink>
        <RouterLink to="/course/ranking" class="nav-item" @click="toggleByTap">RANKING</RouterLink>
    </div>
</template>

<script setup>
import { useSlidingPanel } from '@/assets/script.js'
import { useUserStore } from '@/stores/user';
import { computed, onMounted, ref, watch } from 'vue';
  
const { toggleByTap } = useSlidingPanel()  

const userStore = useUserStore()
// 로그인한 유저의 닉네임이 바뀔 때마다 자동으로 경로를 업데이트
const myInfoPath = computed(() => {
    const nickname = userStore.user?.nickname;
    // 닉네임이 있으면 해당 경로로, 없으면 기본 마이페이지나 로그인으로 안내
    return nickname ? `/myinfo/${nickname}` : '/login';
});

// 컴포넌트가 마운트될 때 유저 정보가 없다면 불러오도록 설정 
onMounted(() => {
    if (userStore.loginUserId && !userStore.user.nickname) {
        userStore.getUserById(userStore.loginUserId);
    }
});
watch()
</script>

<style scoped>
.nav-item {
    text-decoration: none;

}

.router-link-active {
    font-weight: bold;
    color: #ff7f00;
}
</style>