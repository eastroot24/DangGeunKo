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
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const store = useReviewStore()


const reviewList = ref([])
onMounted(() => {
    store.searchInfo.courseId = route.params.id
    store.getReviewsByCourse()
    reviewList.value = store.reviewList
})
</script>

<style scoped>

</style>