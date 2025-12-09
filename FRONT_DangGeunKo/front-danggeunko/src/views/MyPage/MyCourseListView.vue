<template>
    <div>
        <h2>등록/찜 페이지 입니다</h2>
        <CourseSearchBar></CourseSearchBar>
        <CourseFilter></CourseFilter>
        <RegistCourse></RegistCourse>
        <LikeCourse></LikeCourse>
    </div>
</template>

<script setup>
import CourseSearchBar from '@/components/Main/CourseSearchBar.vue';
import CourseFilter from '@/components/CourseFilter.vue';
import RegistCourse from '@/components/MyPage/RegistCourse.vue';
import LikeCourse from '@/components/MyPage/LikeCourse.vue';
</script>

<style scoped>

</style>