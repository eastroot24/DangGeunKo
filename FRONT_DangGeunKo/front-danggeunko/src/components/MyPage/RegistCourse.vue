<template>
    <div>
        <div class="section-title">
        등록 코스
        <RouterLink 
                v-if="route.path === '/myInfo'"
                :to="{ name:'myCourseList', query:{ tab:'registered' } }"
                class="more-btn"
            >
                더보기
            </RouterLink>
    </div>
        <CourseCard @click="goDetail"></CourseCard>
    </div>
</template>

<script setup>
import CourseCard from '../Course/CourseCard.vue';

import { useRouter, useRoute } from 'vue-router';
const router = useRouter()
const route = useRoute()
const goDetail = () => {
    router.push({name: "courseDetail"})
}
</script>

<style scoped>

</style>