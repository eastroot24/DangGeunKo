<template>
    <div class="app">

    <div class="top-bar">
        <div class="back" @click="goMyInfo">←</div>
        <div class="title">팔로잉/팔로워</div>
    </div>

    <div class="follow-tabs">
        <div 
            :class="{ active: tab === 'following' }" 
            @click="tab = 'following'"
        >
            10 팔로잉
        </div>
        <div 
            :class="{ active: tab === 'follower' }"
            @click="tab = 'follower'"
        >
            99 팔로워
        </div>
    </div>

    <FollowSearchBar></FollowSearchBar>

    <div class="user-list">

        <Following v-if="tab === 'following'" />
        <Follower v-else />
    </div>
    </div>
</template>

<script setup>
    import { ref } from "vue"
import { useRouter } from "vue-router"
import Following from '@/components/MyPage/Following.vue';
import Follower from '@/components/MyPage/Follower.vue';
import FollowSearchBar from "@/components/MyPage/FollowSearchBar.vue";

import { useRoute } from "vue-router"
const route = useRoute()

const tab = ref(route.query.type || "following")

const router = useRouter()
const goMyInfo = () => router.push({name:"myInfo"})
</script>

<style scoped>
.follow-tabs{
    display:flex;justify-content:center;gap:30px;margin-top:12px;font-size:13px;
}
.follow-tabs div{cursor:pointer;font-weight:600;}
.follow-tabs .active{color:#ff7a00;border-bottom:2px solid #ff7a00;padding-bottom:3px;}
</style>