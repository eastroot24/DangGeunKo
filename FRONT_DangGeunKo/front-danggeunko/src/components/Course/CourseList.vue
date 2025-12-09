<template>
    <div>
        <h2>코스카드 리스트</h2>
        <div>
            <CourseCard @click="goDetail"></CourseCard>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import CourseCard from './CourseCard.vue';
const router = useRouter()
const goDetail = () => {
    router.push({name: "courseDetail"})
}
</script>

<style lang="scss" scoped>

</style>