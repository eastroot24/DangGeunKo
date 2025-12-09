<template>
    
<div class="app">
    <h2>로그인</h2>

    <input type="email" placeholder="이메일">
    <input type="password" placeholder="비밀번호">

    <button class="login-btn" @click="goHome">로그인</button>

    <div class="to-join">
        처음이신가요? <router-link to="/signup">회원가입하기</router-link>
    </div>
</div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const goHome = () => {
    router.push('/') 
}
</script>

<style scoped>

</style>