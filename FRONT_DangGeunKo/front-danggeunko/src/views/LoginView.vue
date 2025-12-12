<template>
    
<div class="app">
    <h2>로그인</h2>

    <input type="email" placeholder="이메일" v-model="id">
    <input type="password" placeholder="비밀번호" v-model="password">

    <button class="login-btn" @click="login">로그인</button>

    <div class="to-join">
        처음이신가요? <router-link to="/signup">회원가입하기</router-link>
    </div>
</div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const id = ref("")
const password = ref("")
const store = useUserStore()

const login = function(){
  store.userLogin(id.value, password.value)
  router.push('/') 
}


</script>

<style scoped>

</style>