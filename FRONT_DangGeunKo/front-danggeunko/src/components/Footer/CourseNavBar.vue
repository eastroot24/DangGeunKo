<template>
    <div class="navbar">
        
        <RouterLink
  v-if="myInfoPath"
  :to="myInfoPath"
  class="nav-item"
  @click="toggleByTap"
>
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
import { computed, onMounted, ref, watch } from 'vue';

  
const { toggleByTap } = useSlidingPanel()  

const userStore = useUserStore()
// 로그인한 유저의 닉네임이 바뀔 때마다 자동으로 경로를 업데이트
const myInfoPath = computed(() => {
  if (!userStore.isLoggedIn) return '/login';
  if (!userStore.user?.nickname) return ''; // 아직 로딩 중
  return `/myInfo/${userStore.user.nickname}`;
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
    color: #ff8a24;
}
</style>