<template>
    <div>
        <h2>Course</h2>
       <ul>
      <li v-for="course in store.CourseList" :key="course.id">
        <h3>{{ course.coursename}}</h3>
        <p>{{ course.description }}</p>
        <span>{{ course.distanceKm }} km</span>
      </li>
    </ul>
    </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/course';
import { onMounted } from 'vue';

const store = useCourseStore()

onMounted(()=>{
    store.getCourseList()
})

</script>

<style lang="scss" scoped>

</style>