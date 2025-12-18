<template>
  <div class="body">
    <div class="app">
      <div v-if="!isMyPage">
        <CourseMap />
      </div>
      <RouterView />

      <CousreFooter></CousreFooter>
    </div>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import CousreFooter from '@/components/Footer/CousreFooter.vue';
import CourseMap from '@/components/Main/CourseMap.vue';

const route = useRoute();

// 현재 경로가 마이페이지인지 확인
const isMyPage = computed(() => route.path.includes('myInfo')
  || route.path.includes('login') || route.path.includes('signup'));

</script>

<style scoped></style>