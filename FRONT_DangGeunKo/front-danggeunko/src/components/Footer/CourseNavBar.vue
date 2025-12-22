<template>
    <div class="navbar">
        
        <RouterLink :to='myInfoPath' class="nav-item" @click="toggleByTap">
            <i class="fi fi-rs-user"></i><div>MYPAGE</div>
        </RouterLink>
        <RouterLink to="/course" class="nav-item" @click="toggleByTap">
            <i class="fi fi-rs-map-marker"></i><div>COURSE</div></RouterLink>
        <RouterLink to="/course/ranking" class="nav-item" @click="toggleByTap">
            <i class="fi fi-rs-trophy"></i>
            <div>RANKING</div></RouterLink>
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
    // 1. 로그인 여부 우선 확인
    if (!isLoggedIn.value) return '/login';
    
    // 2. 닉네임 유무 확인
    const nickname = user.value?.nickname;
    return nickname ? `/myinfo/${nickname}` : '/login';
});

// 컴포넌트가 마운트될 때 유저 정보가 없다면 불러오도록 설정 
onMounted(async () => {
    if (userStore.loginUserId && !user.value?.nickname) {
        await userStore.getUserById(userStore.loginUserId);
    }
});
</script>

<style scoped>
.nav-item {
    text-decoration: none;

}

.router-link-active {
    font-weight: bold;
    color: #ff8a24;
}
</style>