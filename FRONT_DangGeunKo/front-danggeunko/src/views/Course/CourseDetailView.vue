<template>
    <div>
        <CourseDetail :courseId="route.params.id"></CourseDetail>
        <Review :courseId="route.params.id"></Review>
    </div>
</template>

<script setup>
import CourseDetail from '@/components/Course/CourseDetail.vue';
import Review from '@/components/Review/Review.vue';
import { useRoute } from 'vue-router';

const route = useRoute()

</script>

<style scoped></style>