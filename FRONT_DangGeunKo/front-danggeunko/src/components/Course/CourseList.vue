<template>
        <div class="course-panel" id="coursePanel">
            <div class="course-grid" v-if="courseStore.courseList && courseStore.courseList.length > 0">
                <div class="course-card" v-for="course in courseStore.courseList" :key="course.courseId">
                    <div @click="goDetail(course)">
                        <CourseCard :course="course"></CourseCard>
                    </div>
                    <div class="card-heart" :class="{ active: course.liked }" @click.stop="toggleLike(course)">
                        <span v-if="course.liked"><i class="fi fi-ss-heart"></i></span>
                        <span v-else><i class="fi fi-rs-heart"></i></span>
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
.course-panel {
    background: hsl(0, 0%, 100%);
    border-radius: 20px;
    padding: 8px 2px 6px;
    height: 100%;
    /* 기본 높이 */
    overflow-y: visible;
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
    position: relative;
}

.card-heart {
    position: absolute;
    right: 6px;
    bottom: 80px;
    width: 24px;
    height: 24px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.9);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: #bbbbbb;
    cursor: pointer;
    box-shadow: var(--shadow-soft);
}

.card-heart.active {
    color: var(--orange);
}
</style>