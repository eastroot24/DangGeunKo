<template>
        <div class="course-panel" id="coursePanel">
            <div class="course-grid" v-if="store.courseList && store.courseList.length > 0">

                <div v-for="(course, index) in store.courseList" :key="course.courseId"
                    :class="['course-item-wrapper', { 'has-ai-comment': index === 0 }]">

                    <div class="course-card" @click="goDetail(course)">
                        <CourseCard :course="course"></CourseCard>
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

const router = useRouter()
const goDetail = async (course) => {
    try {
        await store.getCourseDetailById(course.courseId);
    } catch (error) {
        console.error("상세 데이터 로드 실패:", error);
        alert("코스 정보를 가져오는 데 실패했습니다.");
        return;
    }
    router.push({ name: "courseDetail", params: { id: course.courseId } })
}


//store
const store = useCourseStore()

const getRankingList = () => {
    store.getWeeklyRanking()
}
const { searchInfo } = storeToRefs(store)

onMounted(() => {
    getRankingList()
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
}

.course-item-wrapper.has-ai-comment {
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