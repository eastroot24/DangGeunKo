<template>
    <div class="course-panel-wrapper">
        <div class="course-panel" id="coursePanel">
            <div class="course-grid" v-if="store.courseList && store.courseList.length > 0">
                <div class="course-card"
                v-for="course in store.courseList"
                :key="course.courseId"
                 @click="goDetail(course)">
                    <CourseCard :course="course"></CourseCard>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import CourseCard from './CourseCard.vue';
import { useCourseStore } from '@/stores/course';
import { onMounted } from 'vue';

const router = useRouter()
const goDetail = (course) => {
    router.push({name: "courseDetail", params: {id: course.courseId}})
}

const store = useCourseStore()
onMounted(()=>{
    store.getCourseList()
})

</script>

<style scoped>

    .course-panel {
      background: hsl(0, 0%, 100%);
      border-radius: 20px;
      padding: 8px 2px 6px;
      height: 360px; /* 기본 높이 */
      overflow-y: auto;
      transition: height 0.1s ease-out;
    }

    .course-grid {
      display: grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      gap: 10px;
      padding: 4px 10px 10px;
    }

    .course-card {
      background: #fff;
      border-radius: 16px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
      overflow: hidden;
      font-size: 11px;
    }

</style>