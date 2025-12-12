
<template>
    <div class="body">
      <div class="app">
        <button @click="logout">로그아웃</button>
      <CourseMap></CourseMap>
        <RouterView></RouterView>
      <CousreFooter></CousreFooter>
      </div>
      
    </div>
</template>

<script setup>
import CousreFooter from '@/components/Footer/CousreFooter.vue';
import CourseMap from '@/components/Main/CourseMap.vue';
import { useUserStore } from '@/stores/user';




const store = useUserStore()
const logout = () => {
  store.userLogout()
}

</script>

<style src="@/assets/style.css">
</style>