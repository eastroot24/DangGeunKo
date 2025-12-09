<template>
    <div>
        <h2>랭킹뷰</h2>
        <hr>
        <h2>필터영역</h2>
        <hr>
        <CourseList></CourseList>
        <AIcomment></AIcomment>
    </div>
</template>

<script setup>
import CourseList from '@/components/Course/CourseList.vue';
import AIcomment from '@/components/Ranking/AIcomment.vue';
</script>

<style lang="scss" scoped>

</style>