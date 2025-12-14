<template>
    <div>
        <div class="content">
            <!-- 수정 삭제 버튼 -->
             <div v-if="course.userId === loginUserId">
                <button @click="goUpdate">수정</button>
                <button @click="deleteCourse">삭제</button>
             </div>
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
        <div class="stats">
            <div>
                <div class="card-heart"
                    :class="{ active: course.liked }" 
                    @click.stop="toggleLike(course)" >
                    {{ course.liked ? '♥' : '♡' }}
                </div>
                <span>{{ course.likeCnt || 0 }}</span> 
            </div>
            <div>💬 {{ course.reviewCount || 0 }}</div>
            <div>👁 {{ course.viewCnt }}</div>
        </div>
    </div>
    </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/course';
import { useUserStore } from '@/stores/user';
import { storeToRefs } from 'pinia';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter()
const courseStore = useCourseStore()
const userStore = useUserStore()

const { course } = storeToRefs(courseStore) 
const { loginUserId } = storeToRefs(userStore)
// const course = ref({})

const goUpdate = () => {
    router.push({name: 'courseUpdate', params: course.value.courseId})
}

const deleteCourse = async () => { 
    if(confirm("정말 코스를 삭제하시겠어요?")){
       const isSuccess = await courseStore.deleteCourseById(course.value.courseId) 
       
       if(isSuccess){ 
           alert('성공적으로 코스가 삭제되었습니다!')
           router.back()
       }else{
           alert('코스 삭제를 실패했습니다')
       }
    }
}

// 찜 하트 토글 함수
const toggleLike = async (course) => { 
    if (!loginUserId.value) {
        alert("로그인이 필요합니다.");
        router.push({name: 'login'})
        return; 
    }
    
    try {
        await courseStore.addLike(course.courseId);
        await courseStore.getCourseDetailById(course.courseId, true)
        
    } catch (error) {
        console.error("찜 토글 중 오류 발생:", error);
        alert("찜 상태 변경에 실패했습니다.");
    }
}


onMounted(async ()=>{
    await courseStore.getCourseDetailById(route.params.id)
})

</script>

<style scoped>
    /* 좋아요 + 댓글 + 조회수 */
    .stats{
        display:flex; align-items:center; gap:14px; margin-bottom:20px; 
    }
    .stats div{ font-size:13px; display:flex; align-items:center; gap:4px;  position: relative;}
    .card-heart {
      position: absolute;
      top:1px;
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
      cursor: pointer;
    }

    .card-heart.active {
      color: var(--orange);
    }

</style>