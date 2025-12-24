<template>
    <div class="c-item">
        <div class="c-top">
            <div class="c-userbox" style="cursor: pointer;">
                <div class="c-user" @click="goToUserProfile(review.nickName)">
                    <img :src="profileImgPreview" />
                    <span class="nick">{{ review.nickName }}</span>
                </div>
                <button v-if="loginUserId !== review.userId" class="follow-btn-sm" :class="{ unfollow: isFollowing }"
                    @click="handleToggleFollow">
                    {{ isFollowing ? "언팔로우" : "팔로우" }}
                </button>
            </div>

            <div class="c-meta">{{ review.userDistrict }} · {{ formatTimeAgo(review.updatedAt) }}</div>
        </div>

        <div v-if="!updateView">
            <div class="stars">
                <span v-for="n in 5" :key="n" :class="{ active: n <= review.rating }">★</span>
            </div>
            <div class="c-content">{{ review.content }}</div>

            <div v-if="loginUserId === review.userId" class="action-btns">
                <button class="edit-btn" @click="updateReviewView">수정</button>
                <button class="del-btn" @click="deleteReview(review.reviewId)">삭제</button>
            </div>
        </div>

        <div v-else class="edit-mode">
            <textarea v-model="review.content"></textarea>
            <div class="stars">
                <span v-for="n in 5" :key="n" @click="review.rating = n"
                    :class="{ active: n <= review.rating }">★</span>
            </div>
            <button class="edit-btn" @click="updateReview">수정 완료</button>
        </div>
    </div>
</template>

<script setup>
import { useReviewStore } from '@/stores/review';
import { useUserStore } from '@/stores/user';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { timeAgo } from '@/utils/timeUtils';
import { useRouter } from 'vue-router';
const router = useRouter()
const props = defineProps({
    review: Object,
})

const userStore = useUserStore()
const reviewStore = useReviewStore()
const { loginUserId, followingList } = storeToRefs(userStore)

// 팔로우 여부 계산: 내 팔로잉 리스트에 이 리뷰 작성자의 ID가 있는지 확인
const isFollowing = computed(() => {
    return followingList.value.some(u => u.userId === props.review.userId)
})
// 유저 프로필 페이지로 이동하는 함수
const goToUserProfile = (nickName) => {
    router.push(`/myInfo/${nickName}`);
};
// 팔로우 토글 핸들러
async function handleToggleFollow() {
    if (isFollowing.value) {
        await userStore.deleteFollow(props.review.userId)
    } else {
        await userStore.addFollow(props.review.userId)
    }
    // 최신 팔로잉 목록 갱신 (반응성 유지)
    await userStore.getFollowing(loginUserId.value)
}

let updateView = ref(false)
const updateReviewView = () => updateView.value = !updateView.value

const deleteReview = async (id) => {
    if (confirm("리뷰를 삭제하시겠습니까?")) await reviewStore.deleteReview(id)
}

const updateReview = async () => {
    await reviewStore.updateReview(props.review)
    updateView.value = false
}

const formatTimeAgo = (dateString) => timeAgo(dateString)

const profileImgPreview = computed(() => {
    const baseUrl = 'http://localhost:8080/profileImg/';
    const defaultImg = 'dgk-default-profile.png';
    if (!props.review?.profileImg) return `${baseUrl}${defaultImg}`;
    return `${baseUrl}${props.review.profileImg}`;
});

</script>

<style scoped>
/* 별점 */
.stars span {
    font-size: 20px;
    cursor: pointer;
    color: #ddd;
}

.stars span.active {
    color: #ffb400;
}

.comment-submit {
    background: #ff7a00;
    color: #fff;
    width: 100%;
    border: none;
    height: 36px;
    border-radius: 10px;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
    margin-top: 8px;
}

/* 댓글 리스트 */
.comment-list {
    padding: 0 18px 90px;
}

.c-item {
    border-bottom: 1px solid #f1f1f1;
    padding: 12px 0;
}

.c-top {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.c-user {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 600;
}

.c-user img {
    width: 30px;
    height: 30px;
    border-radius: 50%;
}

.c-meta {
    font-size: 12px;
    color: #666;
}

.star-show {
    color: #ffb400;
    font-size: 14px;
    margin-top: 2px;
}

.c-content {
    font-size: 14px;
    margin-top: 2px;
}



/* 팔로우 버튼 (작은 사이즈 버전) */
.follow-btn-sm {
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 11px;
    border: none;
    background: #ff7f00;
    color: white;
    cursor: pointer;
    transition: 0.2s;
    display: inlinesss;
}

.follow-btn-sm.unfollow {
    background: #bbb;
}

.action-btns {
    margin-top: 8px;
    display: flex;
    gap: 5px;
}

.action-btns,
.c-content {
    padding: 0.25rem;
}

.c-userbox {
    display: flex;
    align-items: center;
    gap: 12px;
    /* ★ 여기서 유저정보와 팔로우 버튼 사이의 간격을 조절하세요 */
    cursor: default;
    /* 박스 전체가 클릭되는 느낌을 주지 않으려면 기본값으로 */
}
</style>