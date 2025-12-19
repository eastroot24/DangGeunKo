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

        <div class="popup-bg" v-if="isOpen">
            <div class="popup">
                <div class="popup-title">비밀번호 확인</div>
                <input type="password" v-model="pwInput" placeholder="비밀번호를 입력하세요" @keyup.enter="checkPassword">
                <div class="error" v-if="pwError">비밀번호가 일치하지 않습니다.</div>
                <button class="confirm-btn" @click="checkPassword">확인</button>
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

const props = defineProps({
    user: Object,    // 조회 대상 유저 객체
    isMe: Boolean    // 본인 여부 (MyInfoView에서 계산해서 넘겨줌)
})

const router = useRouter()
const store = useUserStore()
const { followingList, loginUserId } = storeToRefs(store)

// 상태 관리
const isOpen = ref(false)
const pwInput = ref("")
const pwError = ref(false)

// 팔로잉/팔로워 숫자 (해당 유저의 데이터)
const followingCount = ref(0)
const followerCount = ref(0)

// 프로필 이미지 경로 계산
const profileImgPreview = computed(() => {
    const baseUrl = 'http://localhost:8080/uploads/';
    return props.user.profileImg
        ? `${baseUrl}${props.user.profileImg}`
        : `${baseUrl}dgk-default-profile.png`;
})

// 내(로그인유저)가 이 유저를 팔로우 중인지 확인
const isFollowing = computed(() => {
    return followingList.value.some(f => f.userId === props.user.userId)
})

// 팔로우/팔로워 정보 로드 (대상 유저가 바뀔 때마다 실행)
const loadFollowData = async () => {
    if (!props.user?.userId) return

    try {
        // 1. 타인의 팔로잉/팔로워 목록을 가져옴 
        // 주의: 이 함수들이 실행되면 store.followingList가 이 유저의 리스트로 바뀜
        await store.getFollowing(props.user.userId)
        followingCount.value = store.followingList.length

        await store.getFollower(props.user.userId)
        followerCount.value = store.followerList.length

        // 2. 핵심: 다시 "내(로그인한 유저)" 팔로잉 목록으로 복구시킴
        // 그래야 isFollowing (computed)이 내 리스트를 보고 정확히 판단함
        if (loginUserId.value) {
            await store.getFollowing(loginUserId.value)
        }
    } catch (e) {
        console.error("데이터 로드 실패:", e)
    }
}

// 팔로우 토글
const toggleFollow = async () => {
    try {
        if (isFollowing.value) {
            await store.deleteFollow(props.user.userId);
        } else {
            await store.addFollow(props.user.userId);
        }
        await store.getFollowing(loginUserId.value);

        // 해당 유저의 팔로워 숫자도 다시 로드
        await loadFollowData();
    } catch (error) {
        console.error("팔로우 토글 실패:", error);
    }
}

// 비밀번호 확인 로직 (본인용)
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
        router.replace('/')
    }
}

const closePopup = () => {
    isOpen.value = false
    pwInput.value = ""
    pwError.value = false
}

// Profile.vue 내부
const goFollow = (type) => {
    router.push({
        name: "follow",
        params: {
            type: type, // 'following' 또는 'follower'
            nickname: props.user.nickname
        }
    })
}

// 대상 유저 ID가 변경되면 팔로우 정보를 다시 가져옴
watch(() => props.user?.userId, loadFollowData, { immediate: true })
</script>

<style scoped>
.profile-card {
    display: flex;
    align-items: center;
    padding: 24px;
    gap: 20px;
    background: #fff;
}

.profile-img {
    width: 90px;
    height: 90px;
    border-radius: 50%;
    object-fit: cover;
    border: 1px solid #eee;
}

.profile-info {
    flex: 1;
}

.name-row {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 4px;
}

.name-row b {
    font-size: 18px;
}

.email {
    font-size: 13px;
    color: #888;
    margin: 0 0 8px 0;
}

.follow-stats {
    font-size: 14px;
    margin-bottom: 12px;
}

.follow {
    cursor: pointer;
    color: #555;
}

.follow strong {
    color: #333;
}

.my-actions {
    display: flex;
    align-items: center;
    gap: 12px;
}

.profile-btn {
    padding: 6px 14px;
    border-radius: 8px;
    border: 1px solid #ddd;
    background: #fff;
    font-size: 12px;
    cursor: pointer;
}

.follow-toggle-btn {
    padding: 4px 12px;
    border-radius: 6px;
    border: none;
    background: #ff7a00;
    color: white;
    font-size: 12px;
    cursor: pointer;
}

.follow-toggle-btn.unfollow {
    background: #eee;
    color: #666;
}

.logout-link {
    font-size: 12px;
    color: #999;
    background: none;
    border: none;
    cursor: pointer;
    text-decoration: underline;
}

/* 팝업 스타일 */
.popup-bg {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, .4);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 100;
}

.popup {
    width: 300px;
    background: white;
    border-radius: 16px;
    padding: 24px;
    text-align: center;
}

.popup input {
    width: 100%;
    padding: 12px;
    margin: 16px 0 8px;
    border: 1px solid #ddd;
    border-radius: 8px;
    box-sizing: border-box;
}

.confirm-btn {
    width: 100%;
    height: 40px;
    background: #ff7a00;
    color: white;
    border: none;
    border-radius: 8px;
    margin-top: 10px;
    cursor: pointer;
}

.close-btn {
    background: none;
    border: none;
    color: #999;
    margin-top: 12px;
    cursor: pointer;
}

.error {
    color: #ff4d4f;
    font-size: 12px;
    margin-bottom: 8px;
}
</style>