<template>
    
<div class="app">
  <div class="login-form">
    <h2 style="font-family: DanggeunkoFont;
    font-size: 40px;
  font-weight: 700;
  color: #ff8a24;
  margin-bottom: 1.5rem;">당근코</h2>

    <input type="email" placeholder="이메일" v-model="id">
    <input type="password" placeholder="비밀번호" v-model="password" @keyup.enter="login">

    <button class="btn-orange" @click="login">로그인</button>
    <div class="to-join">
        처음이신가요? <router-link to="/signup" class="signup-link">회원가입하기</router-link>
    </div>
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

const login = async () => {
  const success = await store.userLogin(id.value, password.value)

  if (success) {
    router.replace('/')
  } else {
    alert("아이디 또는 비밀번호가 올바르지 않습니다.")
  }
}

</script>

<style scoped>
.login-form{
    position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);

  padding: 1.5rem;
  text-align: center;

  width: 100%;
  max-width: 360px;
}

.to-join {
  margin-top: 1rem;
  font-size: 14px;
  color: #52525b;
}

.signup-link,
.signup-link:visited,
.signup-link:hover,
.signup-link:active {
  color: #ff8a24;
  font-weight: 600;
    margin-left: 4px;
  text-decoration: none;
}

</style>