<template>
    <div class="profile-card" v-if="user && user.nickname">
        <img :src="profileImgPreview" class="profile-img">

        <div class="profile-info">
            <div class="name-row">
                <b>{{ user.nickname }}</b>
                <button v-if="!isMe" class="follow-toggle-btn" :class="{ unfollow: isFollowing }" @click="toggleFollow">
                    {{ isFollowing ? '언팔로우' : '팔로우' }}
                </button>
            </div>

            <p v-if="isMe" class="email">{{ user.userEmail }}</p>

            <div class="follow-stats">
                <span class="follow" @click="goFollow('following')">
                    <strong>{{ followingCount }}</strong> 팔로잉
                </span> ·
                <span class="follow" @click="goFollow('follower')">
                    <strong>{{ followerCount }}</strong> 팔로워
                </span>
            </div>

            <div v-if="isMe" class="my-actions">
                <button class="profile-btn" @click="isOpen = true">프로필 수정</button>
                <button class="logout-link" @click="logout">로그아웃</button>
            </div>
        </div>

        <div class="coach-btn-wrapper">
            <button class="coach-icon-btn" @click="isCoachOpen = true" title="당근코치 피드백">
                <img src="@/assets/img/dgk_coach_head.png" alt="당근코치">
            </button>
            
            <CoachModal 
                v-if="isCoachOpen" 
                :user="user" 
                @close="isCoachOpen = false" 
            />
        </div>
        <div class="popup-bg" v-if="isOpen">
            <div class="popup">
                <div class="popup-title">비밀번호 확인</div>
                <input type="password" v-model="pwInput" placeholder="비밀번호를 입력하세요" @keyup.enter="checkPassword">
                <div class="error" v-if="pwError">비밀번호가 일치하지 않습니다.</div>
                <button class="btn-orange" @click="checkPassword">확인</button>
                <button class="close-btn" @click="closePopup">취소</button>
            </div>
        </div>

        
        
    </div>
</template>

<script setup>
import { ref, computed, watch } from "vue"
import { useRouter } from "vue-router"
import { useUserStore } from "@/stores/user"
import { storeToRefs } from "pinia"
import CoachModal from "./AICoach/CoachModal.vue"
const userStore = useUserStore();
const { isCoachOpen } = storeToRefs(userStore); 
const props = defineProps({
    user: Object,
    isMe: Boolean
})

const router = useRouter()
const store = useUserStore()
const { followingList, loginUserId } = storeToRefs(store)

// 상태 관리
const isOpen = ref(false)
const pwInput = ref("")
const pwError = ref(false)

const followingCount = ref(0)
const followerCount = ref(0)

const profileImgPreview = computed(() => {
    const baseUrl = 'http://localhost:8080/profileImg/';
    return props.user.profileImg
        ? `${baseUrl}${props.user.profileImg}`
        : `${baseUrl}dgk-default-profile.png`;
})

const isFollowing = computed(() => {
    return followingList.value.some(f => f.userId === props.user.userId)
})

const loadFollowData = async () => {
    if (!props.user?.userId) return
    try {
        await store.getFollowing(props.user.userId)
        followingCount.value = store.followingList.length
        await store.getFollower(props.user.userId)
        followerCount.value = store.followerList.length
        if (loginUserId.value) {
            await store.getFollowing(loginUserId.value)
        }
    } catch (e) {
        console.error("데이터 로드 실패:", e)
    }
}

const toggleFollow = async () => {
    try {
        if (isFollowing.value) {
            await store.deleteFollow(props.user.userId);
        } else {
            await store.addFollow(props.user.userId);
        }
        await store.getFollowing(loginUserId.value);
        await loadFollowData();
    } catch (error) {
        console.error("팔로우 토글 실패:", error);
    }
}

const checkPassword = () => {
    if (pwInput.value === store.user.userPassword) {
        store.verifyPassword()
        closePopup()
        router.push({ name: 'editProfile' })
    } else {
        pwError.value = true
    }
}

const logout = () => {
    if (confirm("로그아웃 하시겠습니까?")) {
        store.userLogout()
    }
}

const closePopup = () => {
    isOpen.value = false
    pwInput.value = ""
    pwError.value = false
}

const goFollow = (type) => {
    router.push({
        name: "follow",
        params: {
            type: type,
            nickname: props.user.nickname
        }
    })
}

watch(() => props.user?.userId, loadFollowData, { immediate: true })
</script>

<style scoped>
/* Profile Card Layout */
.profile-card {
    display: flex;
    align-items: center; /* 세로 중앙 정렬 */
    padding: 1.5rem;
    gap: 1.25rem;
    background: #fff;
    position: relative;
    /* 수평 분할을 위해 justify-content 설정 가능하지만, 
       중앙 영역(profile-info)이 flex:1이므로 자동 밀어내기가 됨 */
}

.profile-img {
    width: 5.625rem;
    height: 5.625rem;
    border-radius: 50%;
    object-fit: cover;
    border: 0.0625rem solid #eee;
    flex-shrink: 0; /* 이미지 크기 유지 */
}

.profile-info { 
    flex: 1; /* 남은 공간을 다 차지하여 코치 버튼을 오른쪽 끝으로 밀어냄 */
}

.profile-info { flex: 1; }

.name-row {
    display: flex;
    align-items: center;
    gap: 0.625rem; /* 10px */
    margin-bottom: 0.25rem; /* 4px */
}

.name-row b { font-size: 1.125rem; /* 18px */ }

/* 코치 버튼을 감싸는 영역 */
.coach-btn-wrapper {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    flex-shrink: 0;
}

.coach-icon-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 0.5rem; /* 터치 영역 확보 */
    display: flex;
    align-items: center;
    transition: transform 0.2s;
}
.coach-icon-btn:hover { transform: scale(1.1); }
.coach-icon-btn img {
    width: 2.5rem; /* 32px */
    height: 2.5rem;
}

.email { font-size: 0.8125rem; color: #888; margin: 0 0 0.5rem 0; }
.follow-stats { font-size: 0.875rem; margin-bottom: 0.75rem; }
.follow { cursor: pointer; color: #555; }
.follow strong { color: #333; }

.my-actions { display: flex; align-items: center; gap: 0.75rem; }

.profile-btn {
    padding: 0.375rem 0.625rem;
    border-radius: 0.5rem;
    border: 0.0625rem solid #ddd;
    background: #fff;
    font-size: 0.75rem;
    cursor: pointer;
}

.follow-toggle-btn {
    padding: 0.25rem 0.75rem;
    border-radius: 0.375rem;
    border: none;
    background: var(--orange);
    color: white;
    font-size: 0.75rem;
    cursor: pointer;
}
.follow-toggle-btn.unfollow { background: #eee; color: #666; }

.logout-link {
    font-size: 0.75rem;
    color: #999;
    background: none;
    border: none;
    cursor: pointer;
    text-decoration: underline;
}

/* 팝업 공통 배경 */
.popup-bg {
    position: fixed;
    top: 0; left: 0; width: 100%; height: 100%;
    background: rgba(0, 0, 0, .4);
    display: flex; justify-content: center; align-items: center;
    z-index: 999;
}

.popup {
    width: 18.75rem; /* 300px */
    background: white;
    border-radius: 0.5rem;
    padding: 1.5rem;
    text-align: center;
}

.close-btn { background: none; border: none; color: #999; margin-top: 0.75rem; cursor: pointer; }
.error { color: #ff4d4f; font-size: 0.75rem; margin-bottom: 0.5rem; }
</style>