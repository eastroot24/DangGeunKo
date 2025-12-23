<template>
    <div>

        <!-- 댓글 입력 -->
        <div class="comment-box">
            <textarea v-model="review.content" class="comment-input" placeholder="코스에 대한 리뷰를 남겨주세요."></textarea>
            <div class="stars">
                <span v-for="n in 5" :key="n" @click="review.rating = n"
                    :class="{ active: n <= review.rating }">★</span>
            </div>
            <button @click="addReview" class="btn-orange">등록</button>
        </div>
    </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/course';
import { useReviewStore } from '@/stores/review';
import { useUserStore } from '@/stores/user';
import { storeToRefs } from 'pinia';
import { ref, onMounted, watch } from 'vue'; // onMounted 임포트 추가
import { useRoute, useRouter } from 'vue-router';
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const reviewStore = useReviewStore()
const courseStore = useCourseStore()
const { loginUserId } = storeToRefs(userStore)
//  리뷰 객체
const review = ref({
    courseId: route.params.id,
    userId: loginUserId.value,
    rating: 0,
    content: '',

})

const addReview = async () => {
    // 로그인 정보 확인
    if (loginUserId.value === null) {
        alert('로그인이 필요한 서비스 입니다.')
        router.push({ name: 'login' })
    }
    else {

        // 1. 리뷰를 등록하고
        await reviewStore.addReview(review.value);

        // 2. 리뷰 입력 필드를 초기화
        review.value.rating = 0;
        review.value.content = '';
        // 3. 최신 데이터 갱신 (리뷰 목록 + 코스 상세 정보)
        await reviewStore.getReviewsByCourse();
        await courseStore.getCourseDetailById(review.value.courseId, true)
    }
}






// 별점 선택 이벤트 처리 함수 정의
const handleStarClick = (event) => {
    let selectedValue = event.target.dataset.v;
    if (selectedValue) {
        review.value.rating = parseInt(selectedValue);
        updateStarDisplay(review.value.rating);
    }
};

// 별점 DOM 요소를 업데이트하는 함수
const updateStarDisplay = (currentRating) => {
    // DOM에 직접 접근
    document.querySelectorAll("#starRate span").forEach(s => {
        // 현재 별점보다 data-v 값이 작거나 같으면 active 클래스를 부여
        s.classList.toggle("active", parseInt(s.dataset.v) <= currentRating);
    });
};


watch(() => route.params.id, (newId) => {
    if (newId) {
        review.value.courseId = newId;
    }
});

// 3. 컴포넌트가 DOM에 마운트된 후에만 이벤트 리스너 부착
onMounted(() => {
    /* 별점 선택 */
    // DOM 요소가 존재할 때 이벤트 리스너를 부착
    document.querySelectorAll("#starRate span").forEach(star => {
        // 이미 위에 정의된 handleStarClick 함수를 리스너로 사용
        star.addEventListener("click", handleStarClick);
    });

    // 초기 별점(0) 상태 반영
    updateStarDisplay(review.value.rating);
});

</script>

<style scoped>
/* 댓글 입력칸 */
.comment-box {
    padding: 12px;
    background: #fff;
    border-top: 1px solid #eee;
    border-bottom: 1px solid #eee;
    margin-bottom: 16px;
}

.comment-input {
    width: 95%;
    padding: 10px;
    border-radius: 10px;
    border: 1px solid #ddd;
    font-size: 12px;
    margin-bottom: 10px;
}

/* 별점 */
.stars span {
    font-size: 20px;
    cursor: pointer;
    color: #ddd;
}

.stars span.active {
    color: #ffb400;
}

</style>