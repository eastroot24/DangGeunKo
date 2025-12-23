<template>
    <div class="follow-container">
        <button class="back-btn" @click="goBack">
      <i class="fi fi-rs-angle-left"></i>
    </button>
        <div class="tabs">
            <div class="tab-item" :class="{ active: currentType === 'following' }" @click="changeTab('following')">
                <strong>{{ followingCount }}</strong> 팔로잉
            </div>
            <div class="tab-item" :class="{ active: currentType === 'follower' }" @click="changeTab('follower')">
                <strong>{{ followerCount }}</strong> 팔로워
            </div>
        </div>
        <div class="user-list">
            <div class="search-bar">
                <FollowSearchBar v-model="searchQuery"></FollowSearchBar>
            </div>
            <div class="user-card" v-for="u in filteredList" :key="u.userId">
                <div class="user-left" @click="goToProfile(u.nickname)" style="cursor:pointer">
                    <img :src="u.profileImg ? `http://localhost:8080/profileImg/${u.profileImg}` : 'http://localhost:8080/profileImg/dgk-default-profile.png'"
                        class="user-img">
                    <div>
                        <div class="user-name">{{ u.nickname || '이름 없음' }}</div>
                        <div class="user-meta">{{ u.userDistrict || u.userCity || '지역 정보 없음' }}</div>
                    </div>
                </div>
                <button v-if="Number(u.userId) !== Number(store.loginUserId)" class="follow-btn"
                    :class="{ unfollow: u.isFollowing }" @click="toggleFollow(u)">
                    {{ u.isFollowing ? "언팔로우" : "팔로우" }}
                </button>
            </div>
            <div v-if="filteredList.length === 0" class="empty-msg">
                {{ searchQuery ? `'${searchQuery}'에 대한 검색 결과가 없습니다.` : '목록이 비어 있습니다.' }}
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, computed, ref, watch } from "vue"
import { useRoute, useRouter } from "vue-router"
import { useUserStore } from "@/stores/user"
import { storeToRefs } from "pinia"
import FollowSearchBar from "@/components/MyPage/FollowSearchBar.vue"

const store = useUserStore()
const route = useRoute()
const router = useRouter()
const { loginUserId } = storeToRefs(store)
const currentType = ref(route.params.type)
const nicknameParam = computed(() => route.params.nickname)
const searchQuery = ref("")
// 상태 관리
const targetUserId = ref(null) // ref로 변경
const followingCount = ref(0)
const followerCount = ref(0)
const localUserList = ref([])

const goBack = () => {
  router.back()
}
const filteredList = computed(() => {
    if (!searchQuery.value.trim()) {
        return localUserList.value;
    }
    return localUserList.value.filter(u =>
        u.nickname.includes(searchQuery.value)
    );
});

const loadData = async () => {
    try {
        // 1. 닉네임으로 유저 정보 먼저 가져오기
        const userData = await store.getUserByNickname(nicknameParam.value)
        if (!userData) {
            console.error("유저를 찾을 수 없습니다.")
            return
        }
        targetUserId.value = userData.userId

        // 2. 내 팔로잉 상태 로드 (버튼 체크용)
        await store.getFollowing(store.loginUserId)

        // 3. 대상 유저의 팔로잉/팔로워 데이터 로드
        await store.getFollowing(targetUserId.value)
        followingCount.value = store.followingList.length

        await store.getFollower(targetUserId.value)
        followerCount.value = store.followerList.length

        // 4. 현재 탭에 맞는 리스트를 로컬 변수에 할당
        if (currentType.value === 'following') {
            localUserList.value = [...store.followingList]
        } else {
            localUserList.value = [...store.followerList]
        }

        // 5. 스토어 상태를 다시 내 목록으로 복구 (UI 유지용)
        await store.getFollowing(store.loginUserId)
    } catch (err) {
        console.error("데이터 로딩 중 에러:", err)
    }
}

onMounted(loadData)

// 탭 변경 시 로직 수정 (userId 대신 nickname 유지)
const changeTab = (type) => {
    currentType.value = type
    router.replace({
        name: 'follow',
        params: { type, nickname: nicknameParam.value }
    })
    loadData()
}

const goToProfile = (nickname) => {
    router.push(`/myInfo/${nickname}`)
}

async function toggleFollow(u) {
    try {
        if (u.isFollowing) {
            await store.deleteFollow(u.userId)
            u.isFollowing = false
            if (targetUserId.value === loginUserId.value) {
                followingCount.value--
            }
        } else {
            await store.addFollow(u.userId)
            u.isFollowing = true
            if (targetUserId.value === loginUserId.value) {
                followingCount.value++
            }

        }
        await store.getFollowing(store.loginUserId)
    } catch (err) {
        console.error("팔로우 토글 에러:", err)
    }
}

watch(() => route.params.nickname, loadData)
</script>

<style scoped>
.tabs {
    display: flex;
    border-bottom: 1px solid #eee;
    margin-bottom: 10px;
}

.tab-item {
    flex: 1;
    text-align: center;
    padding: 12px;
    cursor: pointer;
    color: #999;
}

.tab-item.active {
    color: #ff7f00;
    border-bottom: 2px solid #ff7f00;
    font-weight: bold;
}

.user-card {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px;
    border-bottom: 1px solid #f9f9f9;
}

.user-left {
    display: flex;
    align-items: center;
    gap: 12px;
}

.user-img {
    width: 45px;
    height: 45px;
    border-radius: 50%;
    object-fit: cover;
}

.user-name {
    font-size: 15px;
    font-weight: 600;
}

.user-meta {
    font-size: 12px;
    color: #888;
}

.follow-btn {
    padding: 6px 14px;
    border-radius: 6px;
    font-size: 13px;
    border: none;
    background: #ff7f00;
    color: white;
    cursor: pointer;
}

.unfollow {
    background: #eee;
    color: #666;
}

.empty-msg {
    text-align: center;
    padding: 40px;
    color: #ccc;
}
</style>