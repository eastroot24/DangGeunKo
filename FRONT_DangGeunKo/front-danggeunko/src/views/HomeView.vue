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

const hiddenPaths = [
  'myinfo',
  'login',
  'signup',
  'editProfile',
  'myCourseList',
  'follow',
];

// 현재 경로(route.path)에 위 키워드 중 하나라도 포함되어 있는지 확인
const isMyPage = computed(() => {
  return hiddenPaths.some(path => route.path.includes(path));
});

</script>

<style scoped></style>