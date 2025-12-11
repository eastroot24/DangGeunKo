<template>
  <div>
    <div class="user-card" v-for="u in store.followerList" :key="u.userId">
      <div class="user-left">
        <img src="https://i.imgur.com/7b3cP6e.png" class="user-img">
        <div>
          <div class="user-name">{{ u.nickname }}</div>
          <div class="user-meta">{{ u.region }}</div>
        </div>
      </div>

      <button 
        class="follow-btn" 
        :class="{ unfollow : u.isFollowing }"
        @click="toggleFollow(u)"
      >
        {{ u.isFollowing ? '언팔로우' : '팔로우' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from "vue"
import { useUserStore } from "@/stores/user"

const store = useUserStore()
const myId = store.loginUserId // 로그인 ID

onMounted(async () => {
  await store.getFollower(myId)
})

async function toggleFollow(u) {
  if (u.isFollowing) {
    if(await store.deleteFollow(u.userId)) u.isFollowing = false
  } else {
    if(await store.addFollow(u.userId)) u.isFollowing = true
  }
}
</script>

<style scoped>
.follow-btn{
    padding: 6px 15px;
    border-radius: 6px;
    font-size:14px;
    border:none;
    background:#ff7f00;
    color:white;
    cursor: pointer;
}
.unfollow{
    background:#999;
}
</style>
