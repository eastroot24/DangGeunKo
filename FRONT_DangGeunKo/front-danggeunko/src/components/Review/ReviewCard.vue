<template>
    <div>
         <div class="c-item">
            <div class="c-top">
                <div class="c-user">
                    <img src="" />
                    {{ review.nickName }}
                </div>
                <div class="c-meta">{{ review.region }} · {{formatTimeAgo(review.updatedAt)}}</div>
            </div>
            <!-- 업데이트 x일때만 보여줄거야 -->
            <div v-if="updateView === false">
                <div class="stars">
                <span 
                    v-for="n in 5" 
                    :key="n" 
                    :class="{ active: n <= review.rating }"
                >★</span>
                </div>
                <div class="c-content">{{ review.content }}</div>
                <!-- 본인일 경우만 노출 -->
                <div v-if="loginUserId === review.userId" >
                    <button class="edit-btn" @click="updateReviewView()">수정</button>
                    <button class="del-btn" @click="deleteReview(review.reviewId)">삭제</button>
                </div>
            </div>
            <div v-if="updateView === true">
                <textarea v-model="review.content"> </textarea>
                <div class="stars">
            <span 
                v-for="n in 5" 
                :key="n" 
                @click="review.rating = n" 
                :class="{ active: n <= review.rating }"
            >★</span>
             </div>
                <button class="edit-btn" @click="updateReview">수정 완료</button>
            </div>
            
        </div>
    </div>
</template>

<script setup>
import { useReviewStore } from '@/stores/review';
import { useUserStore } from '@/stores/user';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { timeAgo } from '@/utils/timeUtils';
    const userStore = useUserStore()
    const reviewStore = useReviewStore()
    const {loginUserId} = storeToRefs(userStore)

    //수정 창 보여줄지 말지
    let updateView = ref(false)

    const updateReviewView = () =>{
        updateView.value = !updateView.value
    }

    const props = defineProps({
        review: Object,
    })
    const deleteReview = async (id) =>{
       await reviewStore.deleteReview(id)
    }
    const updateReview = async () => {
        await reviewStore.updateReview(props.review)
        updateView.value = !updateView.value
    }

const formatTimeAgo = (dateString) => {
    return timeAgo(dateString);
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


</script>

<style  scoped>
    /* 별점 */
    .stars span{
        font-size:20px; cursor:pointer; color:#ddd;
    }
    .stars span.active{ color:#ffb400; }

    .comment-submit{
        background:#ff7a00; color:#fff; width:100%; border:none; height:36px;
        border-radius:10px; font-size:13px; font-weight:600; cursor:pointer; margin-top:8px;
    }
/* 댓글 리스트 */
    .comment-list{ padding:0 18px 90px;}
    .c-item{ border-bottom:1px solid #f1f1f1; padding:12px 0;}
    .c-top{ display:flex; align-items:center; justify-content:space-between;}
    .c-user{ display:flex; align-items:center; gap:6px;font-size:13px;font-weight:600;}
    .c-user img{ width:30px;height:30px;border-radius:50%;}
    .c-meta{ font-size:10px;color:#666;}
    .star-show{ color:#ffb400;font-size:14px;margin-top:2px;}

    .c-content{ font-size:12px; padding-left:36px; margin-top:2px;}
    .edit-btn,.del-btn{
        font-size:10px;border-radius:10px;border:none;padding:2px 6px;cursor:pointer;
    }
    .edit-btn{ background:#ededed;}
    .del-btn{ background:#ff4e4e; color:#fff; }

</style>