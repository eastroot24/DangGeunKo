<template>
        <div class="course-panel" id="coursePanel">
            <div class="course-grid" v-if="courseStore.courseList && courseStore.courseList.length > 0">
                <div class="course-card" v-for="course in courseStore.courseList" :key="course.courseId">
                    <div @click="goDetail(course)">
                        <CourseCard :course="course"></CourseCard>
                    </div>
                    <div class="card-heart" :class="{ active: course.liked }" @click.stop="toggleLike(course)">
                        {{ course.liked ? '♥' : '♡' }}
                    </div>
                </div>
            </div>
        </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import CourseCard from './CourseCard.vue';
import { useCourseStore } from '@/stores/course';
import { computed, onMounted, ref } from 'vue';
import { useUserStore } from '@/stores/user';
import { storeToRefs } from 'pinia';
import { useMapStore } from '@/stores/map';
import { onBeforeRouteLeave } from 'vue-router'

// 코스 탭을 완전히 벗어날 때만 초기화
onBeforeRouteLeave((to, from, next) => {
    if (!to.name?.startsWith('course')) {
        courseStore.resetSearchInfo()
    }
    next()
})

const router = useRouter()
const courseStore = useCourseStore()
const userStore = useUserStore()
const mapStore = useMapStore()
const { loginUserId } = storeToRefs(userStore)
// 찜 하트 토글 함수
const toggleLike = async (course) => { // ⭐️ async 추가
    if (!loginUserId.value) {
        alert("로그인이 필요합니다.");
        router.push({ name: 'login' })
        return;
    }

    try {
        await courseStore.addLike(course.courseId);

    } catch (error) {
        console.error("찜 토글 중 오류 발생:", error);
        alert("찜 상태 변경에 실패했습니다.");
    }
}

const goDetail = async (course) => {
    // 1. 지도를 해당 코스의 첫 좌표로 이동 (옵션)
    // 서버가 course 객체에 startLat/startLng를 포함한다고 가정
    if (course.startLat && course.startLng) {
        mapStore.setMapCenter({
            lat: course.startLat,
            lng: course.startLng,
            zoom: 13 // 상세 코스이므로 줌 레벨을 높게 설정
        });
    }

    // 2. 상세 페이지로 이동
    router.push({ name: "courseDetail", params: { id: course.courseId } })
}

onMounted(() => {
    courseStore.getCourseList()
})

</script>

<style scoped>

</style>