<template>
    <div class="course-section">
        <div class="section-header">
            <h2 class="section-title">찜 코스</h2> <RouterLink 
                v-if="!allView && limitedCourses.length > 0 && route.path.toLowerCase().includes('/myinfo')"
                :to="{ name: 'myCourseList', params: { nickname: route.params.nickname }, query: { tab: 'registered' } }"
                class="more-btn"
            >
                더보기
            </RouterLink>
        </div>

        <div class="course-container" v-if="limitedCourses.length > 0">
            <div class="course-grid">
                <div class="course-card-wrapper" v-for="course in (allView ? courseStore.likeCourseList : limitedCourses)" :key="course.courseId">
                    <div class="card-content" @click="goDetail(course)">
                        <CourseCard :course="course"></CourseCard>
                    </div>
                    <div class="card-heart" :class="{ active: course.liked }" @click.stop="toggleLike(course)">
                        <i :class="course.liked ? 'fi fi-ss-heart' : 'fi fi-rs-heart'"></i>
                    </div>
                </div>
            </div>
        </div>

        <div class="empty-msg" v-else>
            <p>아직 코스가 없어요 ㅜ.ㅜ</p>
        </div>
    </div>
</template>
<script setup>
import { useCourseStore } from '@/stores/course';
import CourseCard from '../Course/CourseCard.vue';
import { useRouter, useRoute } from 'vue-router';
import { computed, onMounted, watch } from 'vue';
import { storeToRefs } from 'pinia';
import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
const courseStore = useCourseStore()
const { loginUserId } = storeToRefs(userStore)
const props = defineProps({
    targetUserId: Number,
    allView: Boolean,
})
const loadData = async () => {
    if (props.targetUserId) {
        await courseStore.getLikeCourseList(props.targetUserId)
    }
}
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
    return courseStore.likeCourseList.slice(0, 3)
})

// 찜 하트 토글 함수
const toggleLike = async (course) => {
    if (!loginUserId.value) {
        alert("로그인이 필요합니다.");
        router.push({ name: 'login' });
        return;
    }

    try {
        await courseStore.addLike(course.courseId);

        // UI 즉시 업데이트 (allView 여부와 상관없이 스토어의 리스트들 업데이트)
        const updateInList = (list) => {
            const found = list.find(c => c.courseId === course.courseId);
            if (found) found.liked = !found.liked;
        };

        updateInList(courseStore.registCourseList);
        updateInList(courseStore.likeCourseList);

        // 서버 데이터 재동기화
        await Promise.all([
            courseStore.getRegistCourseList(props.targetUserId),
            courseStore.getLikeCourseList(props.targetUserId)
        ]);
    } catch (error) {
        console.error("찜 토글 중 오류:", error);
    }
};
watch(() => props.targetUserId, loadData)

onMounted(loadData)
</script>

<style scoped>

</style>