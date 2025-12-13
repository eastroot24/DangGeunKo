<template>
    <div class="course-panel-wrapper">
        <div class="course-panel" id="coursePanel">
            <div class="course-grid" v-if="courseStore.courseList && courseStore.courseList.length > 0">
                <div class="course-card"
                v-for="course in courseStore.courseList"
                :key="course.courseId"
                 >
                 <div @click="goDetail(course)">
                    <CourseCard :course="course"></CourseCard>
                 </div>
                    <div class="card-heart"
                    :class="{ active: course.isLiked }" 
                    @click.stop="toggleLike(course)" >
                    {{ liked.courseId === course.courseId && liked.userId === loginUserId && liked.isLiked ? '♥' : '♡' }}
                    </div>
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



const router = useRouter()
const courseStore = useCourseStore()
const userStore = useUserStore()
const { loginUserId } = storeToRefs(userStore)

const liked = ref(null)
// 찜 하트 토글 함수
const toggleLike = async (course) => { // ⭐️ async 추가
    if (!loginUserId.value) {
        alert("로그인이 필요합니다.");
        router.push({name: 'login'})
        return; 
    }
    
    try {
        // ⭐️ await을 사용하여 서버 응답을 기다립니다.
        const response = await courseStore.addLike(course.courseId, loginUserId.value);
        liked.value = response
        // UI 상태 업데이트는 store.toggleCourseLike 내부에서 처리됩니다.
        
    } catch (error) {
        console.error("찜 토글 중 오류 발생:", error);
        alert("찜 상태 변경에 실패했습니다.");
    }
}

const goDetail = async (course) => {
    try {
        await courseStore.getCourseDetailById(course.courseId); 
    } catch (error) {
        console.error("상세 데이터 로드 실패:", error);
        alert("코스 정보를 가져오는 데 실패했습니다.");
        return; 
    }
    router.push({name: "courseDetail", params: {id: course.courseId}})
}

onMounted(()=>{
    courseStore.getCourseList()
})

</script>

<style scoped>

    .course-panel {
      background: hsl(0, 0%, 100%);
      border-radius: 20px;
      padding: 8px 2px 6px;
      height: 100%; /* 기본 높이 */
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