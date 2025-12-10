<template>
    <div>
        <div class="content">
            <div class="course-name">{{course.courseName}}</div>
            <!-- 지도 -->
            <div class="map-area">지도 영역</div>
        <div class="sub-info">출발 · 종료 : {{course.startAddress}} – {{course.endAddress}}</div>

        <div class="tag-list">
            <div class="tag">{{course.courseCity}}</div>
            <div class="tag">{{course.courseDistrict}}</div>
            <div class="tag">{{course.difficulty}}</div>
            <div class="tag">화장실 여부 : {{course.hasToilet}}</div>
            <div class="tag">{{course.distanceKm}}km / {{ course.duration_min }}시간 / {{course.coursePace}} 페이스</div>
        </div>

        <div class="desc">
            {{course.description}}
        </div>
    </div>
    </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/course';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const store = useCourseStore()


const course = ref({})
onMounted(() => {
    store.getCourseDetailById(route.params.id)
    course.value = store.course
})

</script>

<style scoped>

</style>