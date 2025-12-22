<template>
  <div class="body">
    <div class="app">
      <div v-if="!isMyPage">
        <CourseMap />
      </div>
      <div
        v-if="showOverlayBackdrop"
        class="overlay-backdrop"
        @click="goHome"
      ></div>
      <RouterView />
      <div v-if="!isOnBoarding"><CousreFooter></CousreFooter></div>
      
    </div>

  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import CousreFooter from '@/components/Footer/CousreFooter.vue';
import CourseMap from '@/components/Main/CourseMap.vue';

const route = useRoute();
const router = useRouter();

const hiddenPaths = [
  'myinfo',
  'login',
  'signup',
  'editProfile',
  'myCourseList',
  'follow',
  'onboarding'
];

const hiddenAllPaths = [
  'onboarding',
]

// 현재 경로(route.path)에 위 키워드 중 하나라도 포함되어 있는지 확인
const isMyPage = computed(() => {
  return hiddenPaths.some(path => route.path.includes(path));
});

const isOnBoarding = computed(() => {
  return hiddenAllPaths.some(path => route.path.includes(path));
});

const showOverlayBackdrop = computed(() => {
  return !isMyPage.value && route.path !== '/';
});

const goHome = () => {
  router.replace('/');
};

</script>

<style scoped>
  .overlay-backdrop {
  position: fixed;
  inset: 0;
  z-index: 10;          /* Map 위, RouterView 아래 */
  background: transparent;
}
</style>