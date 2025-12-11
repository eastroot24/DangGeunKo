<template>
    <div>
        <div class="section-title">
        등록 코스
        <div v-if="allView">
             <div v-for="(course) in store.courseList"
            :key="course.courseId"
            @click="goDetail(course)" >
            <CourseCard :course="course"></CourseCard>
            </div>
        </div>

        <div v-else-if="limitedCourses != null && limitedCourses.length > 0">
            <RouterLink 
                v-if="route.path === '/myInfo'"
                :to="{ name:'myCourseList', query:{ tab:'registered' } }"
                class="more-btn"
            >
                더보기
            </RouterLink>
            <div v-for="(course) in limitedCourses"
            :key="course.courseId"
            @click="goDetail(course)" >
            <CourseCard :course="course"></CourseCard>
            </div>
        </div>
        
        <div v-else>
            <p>아직 등록한 코스가 없어요 ㅜ.ㅜ</p>
        </div>

    </div>
        
    </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/course';
import CourseCard from '../Course/CourseCard.vue';
import { useRouter, useRoute } from 'vue-router';
import { computed, onMounted } from 'vue';

const props = defineProps({
    allView: Boolean,
})

const router = useRouter()
const route = useRoute()
const goDetail = async (course) => {
    try {
        await store.getCourseDetailById(course.courseId); 
    } catch (error) {
        console.error("상세 데이터 로드 실패:", error);
        alert("코스 정보를 가져오는 데 실패했습니다.");
        return; 
    }
    router.push({name: "courseDetail", params: {id: course.courseId}})
}

const store = useCourseStore()

const limitedCourses = computed(() => {
  return store.courseList.slice(0, 3)
})

onMounted(()=>{
    store.getCourseList()
})
</script>

<style scoped>

</style>