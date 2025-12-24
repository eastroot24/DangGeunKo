<template>
    <div class="course-detail-container">
        <div class="header-row">
            <button class="back-btn" @click="goBack">
                <i class="fi fi-rs-angle-left"></i>
            </button>
            <div class="course-name">{{ course.courseName }}</div>
            <div v-if="course.userId === loginUserId" class="admin-btns">
                <button class="mini-btn update" @click="goUpdate">수정</button>
                <button class="mini-btn delete" @click="deleteCourse">삭제</button>
            </div>
        </div>

        <div class="content">
            <div class="map-section">
                <StaticCourseMap :points="course.coursePoints || []" />
            </div>

            <div class="combined-info-section">
                <div class="info-left-author">
                    <div class="author-info" v-if="writer">
                        <span class="author-name">{{ writer.nickname }}</span>
                        <div class="post-time">{{ writer.userCity }} {{ writer.userDistrict }} · {{
                            formatTimeAgo(course.updatedAt) }}
                        </div>
                    </div>
                </div>

                <div class="info-right-address">
                    <div class="address-group">
                        <div class="marker-circle">
                            <i class="fi fi-rs-marker"></i>
                        </div>
                        <div class="address-text">
                            <div>출발 - {{ course.startAddress }}</div>
                            <div>도착 - {{ course.endAddress }}</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="tag-list">
                <div class="tag">{{ course.courseDistrict }}</div>
                <div class="tag">{{ course.difficulty }}</div>
                <div class="tag">횡단보도 {{ course.hasCrosswalk ? "있음" : "없음" }}</div>
                <div class="tag">공중화장실 {{ course.hasToilet ? "있음" : "없음" }}</div>
                <div class="tag wide-tag">
                    {{ course.distanceKm }}km / {{ course.duration_min }}시간 / {{ course.coursePace }} 페이스
                </div>
            </div>

            <div class="description-text">
                {{ course.description || 'Content' }}
            </div>
        </div>
    </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/course';
import { useUserStore } from '@/stores/user';
import { storeToRefs } from 'pinia';
import { onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import StaticCourseMap from '../Main/StaticCourseMap.vue';
import { timeAgo } from '@/utils/timeUtils';
const route = useRoute();
const router = useRouter()
const courseStore = useCourseStore()
const userStore = useUserStore()

const { course } = storeToRefs(courseStore)
const { loginUserId } = storeToRefs(userStore)
const writer = ref(null)
const props = {
    courseId: Number,
}
const formatTimeAgo = (dateString) => {
    if (!dateString) return '';
    return timeAgo(dateString);
};
// 유저 정보를 가져오는 함수 수정
const fetchWriterInfo = async () => {
    if (course.value && course.value.userId) {
        try {
            // Store의 getUserById가 전역 user를 바꾸므로, 
            // 반환값을 직접 받아서 writer ref에 넣어줍니다.
            const userData = await userStore.getUserById(course.value.userId)
            if (userData) {
                writer.value = userData
            }
        } catch (error) {
            console.error("작성자 정보 로드 실패:", error)
        }
    }
}

const goUpdate = () => {
    router.push({ name: 'courseUpdate', params: course.value.courseId })
}

const deleteCourse = async () => {
    if (confirm("정말 코스를 삭제하시겠어요?")) {
        const isSuccess = await courseStore.deleteCourseById(course.value.courseId)

        if (isSuccess) {
            alert('성공적으로 코스가 삭제되었습니다!')
            router.back()
        } else {
            alert('코스 삭제를 실패했습니다')
        }
    }
}

// 찜 하트 토글 함수
const toggleLike = async (courseData) => {
    if (!loginUserId.value) {
        alert("로그인이 필요합니다.");
        router.push({ name: 'login' })
        return;
    }

    try {
        await courseStore.addLike(courseData.courseId);
        const res = await courseStore.getCourseDetailById(courseData.courseId, true)
    } catch (error) {
        console.error("찜 토글 중 오류 발생:", error);
        alert("찜 상태 변경에 실패했습니다.");
    }
}
watch(course, async (newCourse) => {
    if (newCourse && newCourse.userId) {
        await fetchWriterInfo()
    }
}, { immediate: true })
watch(() => route.params.id, (newId) => {
    if (newId) {
        courseStore.getCourseDetailById(newId);
    }
});
onMounted(async () => {
    await courseStore.getCourseDetailById(route.params.id)
})

watch(course, () => {
    console.log('course:', course.value)
    console.log('coursePoints:', course.value?.coursePoints)
})

</script>

<style scoped>
/* 좋아요 + 댓글 + 조회수 */
.stats {
    display: flex;
    align-items: center;
    gap: 14px;
    margin-bottom: 20px;
}

.stats div {
    font-size: 13px;
    display: flex;
    align-items: center;
    gap: 4px;
    position: relative;
}

.heart-btn {
    position: absolute;
    top: 1px;
    left: 2px;
    right: 0px;
    bottom: 80px;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: var(--orange);
    border: none;
}

.heart-btn.active {
    color: var(--orange);
}

.content {
    padding: 1.5rem 2.3rem 0.1rem;
}

.course-name{
    font-size: 24px;
    font-weight: 700;
}
</style>