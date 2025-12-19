<template>
    <div class="navbar">
        <RouterLink to="/myInfo" class="nav-item" @click="toggleByTap">MYPAGE</RouterLink>
        <RouterLink to="/course" class="nav-item" @click="toggleByTap">COURSE</RouterLink>
        <RouterLink to="/course/ranking" class="nav-item" @click="toggleByTap">RANKING</RouterLink>
    </div>
</template>

<script setup>
import { useSlidingPanel } from '@/assets/script.js'

const { toggleByTap } = useSlidingPanel()
</script>

<style scoped>
.nav-item {
    text-decoration: none;

}
</style>