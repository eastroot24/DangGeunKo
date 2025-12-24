<template>
        <div class="course-panel" id="coursePanel">
            <div class="course-grid" v-if="rankingCourseList && rankingCourseList.length > 0">

                <div v-for="(course, index) in rankingCourseList" :key="course.courseId"
                    :class="['course-item-wrapper', { 'has-ai-comment': index === 0 }]">

                    <div class="course-card" @click="goDetail(course)">
                        <CourseCard :course="course"></CourseCard>
                        
                    </div>
                     <div class="card-heart" :class="{ active: course.liked }" @click.stop="toggleLike(course)">
                        <span v-if="course.liked"><i class="fi fi-ss-heart"></i></span>
                        <span v-else><i class="fi fi-rs-heart"></i></span>
                    </div>

                    <div v-if="index === 0" class="ai-comment-container">
                        <AIcomment :course="course"></AIcomment>
                    </div>

                </div>

            </div>
        </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import CourseCard from '../Course/CourseCard.vue';
import { useCourseStore } from '@/stores/course';
import { onMounted, ref, watch } from 'vue';
import AIcomment from '../Ranking/AIcomment.vue';
import { storeToRefs } from 'pinia';
import { useUserStore } from '@/stores/user';
const courseStore = useCourseStore()
const router = useRouter()
const userStore = useUserStore()
const {loginUserId} = storeToRefs(userStore)
const {rankingCourseList} = storeToRefs(courseStore)
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

const toggleLike = async (course) => {
    if (!loginUserId.value) {
        alert("로그인이 필요합니다.");
        router.push({ name: 'login' });
        return;
    }

    try {
        await courseStore.addLike(course.courseId);
        await courseStore.getWeeklyRanking(); 

    } catch (error) {
        console.error("찜 토글 중 오류 발생:", error);
    }
}


const getRankingList =  () => {
     courseStore.getWeeklyRanking()
}
const { searchInfo } = storeToRefs(courseStore)

onMounted( async() => {
   await courseStore.getWeeklyRanking()

})

watch(
    searchInfo,
    () => {
        getRankingList()
    },
    { deep: true }
)


</script>

<style scoped>

/* 일반적인 코스 카드 Wrapper */
.course-item-wrapper {
    display: block;
    position: relative; /* ⭐️ 추가: absolute 버튼의 기준점 */
}

.course-item-wrapper.has-ai-comment {
    position: relative;
    grid-column: span 2;
    display: flex;
    gap: 10px;
    padding-right: 0;
    margin-bottom: 14px;
}

.course-item-wrapper.has-ai-comment .course-card {
    flex-basis: 50%;
    flex-shrink: 0;
    margin-right: 0;


    border: 3px solid #ff7a00;
    box-shadow: 0 4px 12px rgba(255, 122, 0, 0.2);
}
.has-ai-comment .card-heart {
    left: calc(50% - 35px); /* 1등 카드는 왼쪽 영역(50%)의 오른쪽 끝에 위치 */
    right: auto;
}
.ai-comment-container {
    flex-grow: 1;
    flex-basis: 50%;
    background: #fff;
    border-radius: 16px;
    padding: 0;
    overflow: hidden;
}

.rank-banner {
    background: #ffe8c4;
    padding: 8px 12px;
    font-size: 12px;
    border-top-left-radius: 16px;
    border-top-right-radius: 16px;
    margin-bottom: 0;
    font-weight: 700;
    color: #ff7a00;
}

</style>