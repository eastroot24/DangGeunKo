<template>
    <div>
        <div class="section-title">
            등록 코스
            <div class="course-grid" v-if="allView">
                <div class="course-card" v-for="(course) in courseStore.registCourseList" :key="course.courseId"
                    @click="goDetail(course)">
                    <div @click="goDetail(course)">
                        <CourseCard :course="course"></CourseCard>
                    </div>
                    <div class="card-heart" :class="{ active: course.liked }" @click.stop="toggleLike(course)">
                        {{ course.liked ? '♥' : '♡' }}
                    </div>
                </div>
            </div>

            <div class="course-grid" v-else-if="limitedCourses != null && limitedCourses.length > 0">
                <RouterLink v-if="route.path === '/myInfo'" :to="{ name: 'myCourseList', query: { tab: 'registered' } }"
                    class="more-btn">
                    더보기
                </RouterLink>
                <div class="course-card" v-for="(course) in limitedCourses" :key="course.courseId"
                    @click="goDetail(course)">
                    <div @click="goDetail(course)">
                        <CourseCard :course="course"></CourseCard>
                    </div>
                    <div class="card-heart" :class="{ active: course.liked }" @click.stop="toggleLike(course)">
                        {{ course.liked ? '♥' : '♡' }}
                    </div>
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
import { storeToRefs } from 'pinia';
import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
const courseStore = useCourseStore()
const { loginUserId } = storeToRefs(userStore)
const props = defineProps({
    allView: Boolean,
})

const router = useRouter()
const route = useRoute()
const goDetail = async (course) => {
    try {
        await courseStore.getCourseDetailById(course.courseId);
    } catch (error) {
        console.error("상세 데이터 로드 실패:", error);
        alert("코스 정보를 가져오는 데 실패했습니다.");
        return;
    }
    router.push({ name: "courseDetail", params: { id: course.courseId } })
}

const limitedCourses = computed(() => {
    return courseStore.registCourseList.slice(0, 3)
})

// 찜 하트 토글 함수
const toggleLike = async (course) => { // ⭐️ async 추가
    if (!loginUserId.value) {
        alert("로그인이 필요합니다.");
        router.push({ name: 'login' })
        return;
    }

    try {
        await courseStore.addLike(course.courseId);
        await courseStore.getRegistCourseList()
        await courseStore.getLikeCourseList()
    } catch (error) {
        console.error("찜 토글 중 오류 발생:", error);
        alert("찜 상태 변경에 실패했습니다.");
    }
}

onMounted(() => {
    courseStore.getRegistCourseList()
})
</script>

<style scoped>
.course-panel {
    background: hsl(0, 0%, 100%);
    border-radius: 20px;
    padding: 8px 2px 6px;
    height: 100%;
    /* 기본 높이 */
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