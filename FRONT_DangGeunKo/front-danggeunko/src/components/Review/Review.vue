<template>
    <div>
        <ReviewForm></ReviewForm>
        <div v-if="reviewList && reviewList.length > 0">
            <div class="review-card"
             v-for="review in reviewList"
            >
                <ReviewCard :review="review"></ReviewCard>
            </div>
        </div>
        <div v-else>
            <p>아직 리뷰가 없습니다...</p>
        </div>
    </div>
</template>

<script setup>
import ReviewCard from './ReviewCard.vue';
import ReviewForm from './ReviewForm.vue';
import { useReviewStore } from '@/stores/review';
import { storeToRefs } from 'pinia';
import { onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const store = useReviewStore()

const { reviewList } = storeToRefs(store);

onMounted(() => {
    store.searchInfo.courseId = route.params.id;
    // await를 사용하여 데이터 로딩이 완료될 때까지 기다리는 것이 좋습니다.
    store.getReviewsByCourse(); 
});

// 2. Pinia Store의 reviewList 상태를 감시
watch(
     () => store.reviewList, // 감시할 대상: Store의 reviewList (getter 함수 사용)
    (newReviews) => {
        console.log('리뷰 목록이 업데이트됨!');
         reviewList.value = newReviews; // 변경된 새로운 목록을 로컬 ref에 할당
    },
    { deep: true } // 객체 내부 변경까지 감시할 필요는 없지만, 안전을 위해 추가 가능
);

</script>

<style scoped>

</style>