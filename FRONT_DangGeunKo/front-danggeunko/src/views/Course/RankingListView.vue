<template>
    <div>
        <h2>랭킹뷰</h2>
        <hr>
        <CourseFilter></CourseFilter>
        <hr>
        <CourseList></CourseList>
        <AIcomment></AIcomment>
    </div>
</template>

<script setup>
import CourseList from '@/components/Course/CourseList.vue';
import CourseFilter from '@/components/CourseFilter.vue';
import AIcomment from '@/components/Ranking/AIcomment.vue';
</script>

<style lang="scss" scoped>

</style>