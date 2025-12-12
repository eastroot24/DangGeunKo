
<template>
    <div class="body">
      <div class="app">
        <button @click="logout">로그아웃</button>
      <Map />
        <RouterView></RouterView>
      <Footer />
      </div>
      
    </div>
</template>

<script setup>

import Map from './components/Main/CourseMap.vue';
import Footer from './components/Footer/CousreFooter.vue';
import { useUserStore } from './stores/user';


const store = useUserStore()
const logout = () => {
  store.userLogout()
}

</script>

<style src="@/assets/style.css">
</style>