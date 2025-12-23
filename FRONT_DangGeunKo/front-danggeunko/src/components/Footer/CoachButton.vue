<template>
    <div class="coach-btn-wrapper">
            <button class="coach-icon-btn" @click="isCoachOpen = true" title="당근코치 피드백">
                <img src="@/assets/img/dgk_coach_head.png" alt="당근코치">
            </button>
            
            <CoachModal 
                v-if="isCoachOpen" 
                :user="user" 
                @close="isCoachOpen = false" 
            />
        </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user';
import { ref, watch } from 'vue';
import { storeToRefs } from 'pinia';
import CoachModal from './AICoach/CoachModal.vue';
const userStore = useUserStore()
const { isCoachOpen } = storeToRefs(userStore); 
const user = ref(null)

watch(userStore.loginUserId, ()=>{
   userStore.getUserById(userStore.loginUserId)
   user.value = userStore.user
})
</script>

<style scoped>
.coach-btn-wrapper {
    position: absolute;
    margin-bottom: 0.45rem;
    right: 24px;  /* 우측 여백 */
    bottom: 180px; /* 하단 여백 (NavBar 높이를 고려하여 조정) */
    z-index: 999;  /* 다른 요소보다 위에 보이게 설정 */
}

.coach-icon-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 0;
    transition: transform 0.2s;
}

.coach-icon-btn:hover {
    transform: scale(1.1); /* 호버 시 살짝 커지는 효과 */
}

.coach-icon-btn img {
    width: 60px;  /* 이미지 크기가 너무 작거나 큰지 확인 */
    height: auto;
}
</style>